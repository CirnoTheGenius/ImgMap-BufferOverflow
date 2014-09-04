package net.yukkuricraft.tenko.imgmap;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * 9/3/14 - 9:39 PM
 * IntelliJ finally decides to mark "src" as the source folder.
 *
 * Just as a forewarning:
 * This project is littered with other silly comments.
 */
public class ImgMap extends JavaPlugin {

	private static ImgMap instance; // *gasp* SINGLETON PATTERN! HERESY!

	@Override
	public void onEnable(){
		//This would be really odd if this were to happen.
		if(instance != null){
			getLogger().log(Level.WARNING, "ImgMap appears to either be reloading or a strange bug happened.");
			getLogger().log(Level.WARNING, "Do note that reloading plugins in general can have unexpected results.");
			getLogger().log(Level.WARNING, "Otherwise, if you are not reloading, please copy and paste this information into the BukkitDev comments.:");
			getLogger().log(Level.WARNING, "\"instance\" was " + instance.toString() + " and is it enabled: " + instance.isEnabled());
		}
		instance = this;
	}

	public void onDisable(){
		instance = null;
	}

	public static ImgMap getInstance(){
		return instance;
	}

	public static void log(Level level, String message){
		getInstance().getLogger().log(level, message);
	}

	public static void log(Level level, String message, Throwable t){
		getInstance().getLogger().log(level, message, t);
	}

}