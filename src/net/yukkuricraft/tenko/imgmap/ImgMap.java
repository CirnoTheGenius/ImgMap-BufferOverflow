package net.yukkuricraft.tenko.imgmap;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * 9/3/14 - 9:39 PM
 * IntelliJ finally decides to mark "src" as the source folder.
 *
 * Just as a forewarning:
 * This project is littered with other silly comments.
 */
public class ImgMap extends JavaPlugin {

	//I always disliked using more than 1 static variable...
	private static File videos_dir;
	private static File images_dir;

	@Override
	public void onEnable(){
		videos_dir = dir("localVideos");
		images_dir = dir("localImages");
	}

	public static File getLVideosDirectory(){
		return videos_dir;
	}

	public static File getLImagesDirectory(){
		return images_dir;
	}

	private final File dir(String name){
		File file = new File(this.getDataFolder(), name);
		if(!file.exists()){
			file.mkdir();
		}

		return file;
	}

}