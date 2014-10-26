package net.yukkuricraft.tenko.imgmap.nms;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.util.WeakHashMap;
import java.util.logging.Logger;

public class NMSHelper {

	private static final Logger LOGGER = Logger.getLogger("ImgMap");
	private static Abstraction TRUE_HELPER; // I call it "TRUE_HELPER" because NMSHelper is like a mask for it.
	private static final WeakHashMap<Player, ProxyChannel> cache = new WeakHashMap<Player, ProxyChannel>();

	static {
		if(TRUE_HELPER == null){
			if(Bukkit.getVersion().contains("Spigot") && (Bukkit.getVersion().contains("1.7.9") || Bukkit.getVersion().contains("1.7.10"))){
				try {
					Class<?> klass = SpigotProtocolFix.getSpigotClass();
					TRUE_HELPER = (Abstraction)klass.newInstance();
				} catch (ReflectiveOperationException e){
					LOGGER.warning("Failed to provide a NMS helper for Spigot 1.7-1.8's protocol hack.");
					LOGGER.warning("Full CraftServer package name: " + Bukkit.getServer().getClass().getPackage().getName());
				}
			} else {
				String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
				try{
					Class<?> klass = Class.forName("net.yukkuricraft.tenko.imgmap.nms." + version + ".AbstractionImpl");
					TRUE_HELPER = (Abstraction)klass.newInstance();
				} catch (ReflectiveOperationException e) {
					LOGGER.warning("Could not find a suitable implementation for handling NMS version " + version + ".");
					LOGGER.warning("Full CraftServer package name: " + Bukkit.getServer().getClass().getPackage().getName());
				}
			}
		}
	}

	public static MapRenderer getDefaultRenderer(short id, World world){
		return TRUE_HELPER.getDefaultRenderer(id, world);
	}

	public static Object getMapPacket(int id, byte[] data){
		return TRUE_HELPER.getPacketData(id, data);
	}

	public static ProxyChannel getChannel(Player player){
		ProxyChannel channel = cache.get(player);
		if(channel != null){
			return channel;
		} else {
			channel = TRUE_HELPER.newChannel(player);
			cache.put(player, channel);
			return channel;
		}
	}

}