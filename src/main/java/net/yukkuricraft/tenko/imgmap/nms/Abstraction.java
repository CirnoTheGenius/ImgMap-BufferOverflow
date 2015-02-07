package net.yukkuricraft.tenko.imgmap.nms;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;

import java.util.logging.Logger;

public interface Abstraction {

	public static final Logger LOGGER = Logger.getLogger("ImgMap-Abstractions");

	public MapRenderer getDefaultRenderer(short id, World world);

	public Object getPacketData(int id, byte[] data);

	public ProxyChannel newChannel(Player player);

}
