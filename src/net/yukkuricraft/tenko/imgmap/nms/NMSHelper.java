package net.yukkuricraft.tenko.imgmap.nms;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.map.MapRenderer;

import java.util.logging.Logger;

public class NMSHelper {

	private static final Logger LOGGER = Logger.getLogger("ImgMap");
	private static Abstraction TRUE_HELPER; // I call it "TRUE_HELPER" because NMSHelper is like a mask for it.

	static {
		if(TRUE_HELPER == null){
			String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			try{
				Class<?> klass = Class.forName("net.yukkuricraft.tenko.nms." + version + ".AbstractionImpl");
				TRUE_HELPER = (Abstraction)klass.newInstance();
			} catch (Throwable t){
				LOGGER.warning("Could not find a suitable implementation for handling NMS.");
			}
		}
	}

	public MapRenderer getDefaultRenderer(World world){
		return TRUE_HELPER.getDefaultRenderer(world);
	}

	public interface Abstraction {

		public MapRenderer getDefaultRenderer(World world);

	}


}