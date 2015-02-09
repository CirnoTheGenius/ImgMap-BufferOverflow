package net.yukkuricraft.tenko.imgmap2;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ImgMap extends JavaPlugin {

	private static ImgMap singleton;

	@Override
	public void onEnable(){
		singleton = this;
	}

	@Override
	public void onDisable(){
		singleton = null; // Properly dispose of the singleton.
	}

	public static ImgMap getSingleton(){
		return singleton;
	}

	// Logging helper methods.
	// Only use severe for catastrophic errors i.e we did a stack overflow or something.
	public static void logThrowable(Level level, String message, Throwable throwable){
		singleton.getLogger().log(level, message, throwable);
	}

	public static void logThrowable(Level level, String message){
		singleton.getLogger().log(level, message);
	}

}