package net.yukkuricraft.tenko.imgmap.nms.v1_6_R3;

import net.minecraft.server.v1_6_R3.*;
import net.yukkuricraft.tenko.imgmap.nms.Abstraction;
import net.yukkuricraft.tenko.imgmap.nms.ProxyChannel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_6_R3.map.CraftMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.lang.ref.WeakReference;
import java.util.logging.Level;

public class AbstractionImpl implements Abstraction {

	@Override
	public MapRenderer getDefaultRenderer(short id, World world){
		org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.MAP);
		bukkitStack.setDurability(id);
		ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
		return new CraftMapRenderer(null, ((ItemWorldMap)nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld)world).getHandle()));
	}

	@Override
	public Object getPacketData(int id, byte[] data){
		return new Packet131ItemData((short)0, (short)id, data);
		//If anyone is willing to help, what was the packet ID/construction for map packets
		//back in 1.6.4? I'm assuming it's 131.
	}

	@Override
	public ProxyChannel newChannel(final Player player){
		if(!(player instanceof CraftPlayer)){
			Abstraction.LOGGER.log(Level.WARNING, "Detected Non-CraftBukkit player! Kinda odd that this plugin still functions.");
			return null;
		}

		// ABSOLUTELY unsafe proxy object. Can someone who remembers 1.6.4 fix this or tell me if this even works?
		return new ProxyChannel(){

			private WeakReference<PlayerConnection> connection = new WeakReference<PlayerConnection>(((CraftPlayer)player).getHandle().playerConnection);

			@Override
			public void sendPacket(Object o){
				if(isOpen() && o instanceof Packet){
					connection.get().sendPacket((Packet)o);
				}
			}

			@Override
			public void flush(){
				return; // Prior to 1.6.x, all packet sending was sync iirc.
			}

			@Override
			public boolean isOpen(){
				return connection.get() != null && !connection.get().disconnected;
			}

		};
	}

}