package net.yukkuricraft.tenko.imgmap.nms.unknown;

import net.yukkuricraft.tenko.imgmap.nms.Abstraction;
import net.yukkuricraft.tenko.imgmap.nms.ProxyChannel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

//Hacky attempt to support more than what I officially support.
//This is probably bad for production servers.
public class AbstractionImpl implements Abstraction {

	private final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private boolean canGenerate = false;

	private Class<?> craftItemStack;
	private Class<?> craftWorld;
	private Class<?> craftMapRenderer;
	private Class<?> nmsItemStack;
	private Class<?> nmsItemWorldMap;

	private Constructor<?> c_craftMapRenderer;
	private Method m_asNMSCopy;
	private Method m_getItem;
	private Method m_getSavedMap;
	private Method m_getHandle;

	private Class<?> nmsPacket;
	private Constructor<?> c_nmsPacket;

	//MY EYES! THEY BURN!
	{
		try{
			craftItemStack = Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack");
			craftWorld = Class.forName("org.bukkit.craftbukkit." + VERSION + ".CraftWorld");
			craftMapRenderer = Class.forName("org.bukkit.craftbukkit." + VERSION + ".map.CraftMapRenderer");
			nmsItemStack = Class.forName("net.minecraft.server." + VERSION + ".ItemStack");
			nmsItemWorldMap = Class.forName("net.minecraft.server." + VERSION + ".ItemWorldMap");
			nmsPacket = Class.forName("net.minecraft.server." + VERSION + ".PacketPlayOutMap");

			m_asNMSCopy = craftItemStack.getDeclaredMethod("asNMSCopy");
			m_asNMSCopy.setAccessible(true);

			m_getItem = nmsItemStack.getDeclaredMethod("getItem");
			m_getItem.setAccessible(true);

			m_getSavedMap = nmsItemWorldMap.getDeclaredMethod("getSavedMap", nmsItemStack, Class.forName("net.minecraft.server." + VERSION + ".World"));
			m_getSavedMap.setAccessible(true);

			m_getHandle = craftWorld.getDeclaredMethod("getHandle");
			m_getHandle.setAccessible(true);

			c_craftMapRenderer = craftMapRenderer.getConstructor(Class.forName("org.bukkit.craftbukkit." + VERSION + ".map.CraftMapView"), Class.forName("net.minecraft.server." + VERSION + ".WorldMap"));
			c_craftMapRenderer.setAccessible(true);

			c_nmsPacket = nmsPacket.getConstructor(int.class, byte[].class);
			c_nmsPacket.setAccessible(true);
		} catch (ReflectiveOperationException e){
			canGenerate = false;
		}
	}

	@Override
	public MapRenderer getDefaultRenderer(short id, World world){
		if(canGenerate){
			try {
				org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.MAP);
				bukkitStack.setDurability(id);
				Object nmsStack = m_asNMSCopy.invoke(bukkitStack);
				Object nmsMap = nmsItemWorldMap.cast(m_getItem.invoke(nmsStack));
				return (MapRenderer)c_craftMapRenderer.newInstance(null, m_getSavedMap.invoke(nmsMap, nmsStack, m_getHandle.invoke(world)));
			} catch (ReflectiveOperationException e){
				canGenerate = false;
				return null;
			}
		} else {
			return null;
		}
	}

	public ProxyChannel getChannel(Player player){
		return null; // TODO: Implement this later.
	}

	@Override
	public Object getPacketData(int id, byte[] data){
		if(canGenerate){
			try{
				return c_nmsPacket.newInstance(id, data);
			} catch (ReflectiveOperationException e) {
				canGenerate = false;
				return null;
			}
		} else {
			return null;
		}
	}

}