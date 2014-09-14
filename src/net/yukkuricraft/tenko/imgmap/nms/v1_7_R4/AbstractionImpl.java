package net.yukkuricraft.tenko.imgmap.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.ItemWorldMap;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutMap;
import net.minecraft.util.io.netty.channel.Channel;
import net.yukkuricraft.tenko.imgmap.nms.Abstraction;
import net.yukkuricraft.tenko.imgmap.nms.ProxyChannel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.logging.Level;

public class AbstractionImpl implements Abstraction {

	@Override
	public MapRenderer getDefaultRenderer(short id, World world){
		org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.MAP);
		bukkitStack.setDurability(id);
		ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
		// https://github.com/Bukkit/CraftBukkit/blob/61e57e1196fc917d0e8f9c5f1ee48818a85fd5b9/src/main/java/org/bukkit/craftbukkit/map/CraftMapRenderer.java
		// It _was_ stored in the past; it was just never used, passed, or interacted with.
		// Don't ask why.
		return new CraftMapRenderer(null, ((ItemWorldMap)nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld)world).getHandle()));
	}

	@Override
	public Object getPacketData(int id, byte[] data){
		return new PacketPlayOutMap(id, data, (byte)0);
	}

	@Override
	public ProxyChannel getChannel(Player player){
		if(!(player instanceof CraftPlayer)){
			Abstraction.LOGGER.log(Level.WARNING, "Detected Non-CraftBukkit player! Kinda odd that this plugin still functions.");
			return null;
		}

		Object netty = ((CraftPlayer)player).getHandle().playerConnection.networkManager;
		try{
			Field f = netty.getClass().getDeclaredField("m");
			f.setAccessible(true);
			final Channel channel = (Channel)f.get(netty);
			return new ProxyChannel(){

				private WeakReference<Channel> oneechan = new WeakReference<Channel>(channel);

				@Override
				public void sendPacket(Object o){
					if(isOpen() && o instanceof Packet){
						oneechan.get().write(o);
					}
				}

				@Override
				public void flush(){
					if(isOpen()){
						oneechan.get().flush();
					}
				}

				@Override
				public boolean isOpen(){
					return oneechan.get() != null && oneechan.get().isOpen();
				}

			};
		} catch (ReflectiveOperationException e){
			e.printStackTrace();
			return null;
		}
	}

}