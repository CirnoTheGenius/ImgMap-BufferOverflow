package net.yukkuricraft.tenko.imgmap;

import net.yukkuricraft.tenko.imgmap.ffmpeg.NixProvider;
import net.yukkuricraft.tenko.imgmap.ffmpeg.Provider;
import net.yukkuricraft.tenko.imgmap.ffmpeg.UnknownProvider;
import net.yukkuricraft.tenko.imgmap.ffmpeg.Win32Provider;
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
	private static boolean allowVideos;
	private static Provider ffmpegProvider;

	@Override
	public void onEnable(){
		videos_dir = dir("localVideos");
		images_dir = dir("localImages");

		this.saveDefaultConfig();
		allowVideos = this.getConfig().getBoolean("AllowVideos");

		setupFFmpeg();
	}

	public static File getLVideosDirectory(){
		return videos_dir;
	}

	public static File getLImagesDirectory(){
		return images_dir;
	}

	/**
	 * Well, it's not really "streaming"...
	 */
	public static boolean isVideoStreamingEnabled(){
		return allowVideos;
	}

	public static Provider getFFmpegProvider(){
		return ffmpegProvider;
	}

	private final File dir(String name){
		File file = new File(this.getDataFolder(), name);
		if(!file.exists()){
			file.mkdirs();
		}

		return file;
	}

	public void setupFFmpeg(){
		Provider provider;
		String os = System.getProperty("os.name");
		if(os.contains("win")){
			getLogger().info("Detected Windows environment.");
			provider = new Win32Provider(this);
		} else if(os.contains("nix") || os.contains("aix") || os.contains("nux")){
			getLogger().info("Detected *nix environment.");
			provider = new NixProvider(this);
		} else if(os.contains("mac") || os.contains("osx")){
			// Unfortunately, I don't know the full specifications of OSX.
			getLogger().info("Detected OSX/Mac environment. ImgMap does not officially support OSX/Mac; videos may or may not work.");
			provider = new NixProvider(this); // Let's assume it's a *nix environment since OSX is built upon Linux.
		} else {
			getLogger().warning("Detected unknown environment. Failed to provide suitable FFmpeg interface.");
			provider = new UnknownProvider();
		}

		ffmpegProvider = provider;
	}

}