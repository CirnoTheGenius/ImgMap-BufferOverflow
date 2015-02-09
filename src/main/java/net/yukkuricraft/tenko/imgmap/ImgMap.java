package net.yukkuricraft.tenko.imgmap;

import net.yukkuricraft.tenko.imgmap.command.ClearMapCommand;
import net.yukkuricraft.tenko.imgmap.command.DrawImageCommand;
import net.yukkuricraft.tenko.imgmap.command.DrawYTVideoCommand;
import net.yukkuricraft.tenko.imgmap.command.GetMapCommand;
import net.yukkuricraft.tenko.imgmap.ffmpeg.Provider;
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

		getCommand("drawimage").setExecutor(new DrawImageCommand());
		getCommand("clearmap").setExecutor(new ClearMapCommand());
		getCommand("drawytvideo").setExecutor(new DrawYTVideoCommand());
		getCommand("getmap").setExecutor(new GetMapCommand());
		getCommand("fixmap").setExecutor(new ClearMapCommand());

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
		return allowVideos && ffmpegProvider.isAvailable();
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
		if(os.contains("win") || os.contains("Windows")){
			getLogger().info("Detected Windows environment.");
			provider = new Provider(this, Provider.OS.WIN);
		} else if(os.contains("nix") || os.contains("aix") || os.contains("nux")){
			getLogger().info("Detected *nix environment.");
			provider = new Provider(this, Provider.OS.NIX);
		} else if(os.contains("mac") || os.contains("osx")){
			// Unfortunately, I don't know the full specifications of OSX.
			getLogger().info("Detected OSX/Mac environment. ImgMap does not officially support OSX/Mac; videos may or may not work.");
			provider = new Provider(this, Provider.OS.OSX);
		} else {
			getLogger().warning("Detected unknown environment. Failed to provide suitable FFmpeg interface for " + os + ". Videos have been disabled.");
			getLogger().warning("Please post back onto the ImgMap plugin page with your OS. Depending on demand, official support may be provided. Otherwise, you can try tricking ImgMap into loading a provider by restarting the server with \"-Dos.name=[win32, nix, osx]\", however, this is highly UNRECOMMENDED since if other plugins use the same method of OS detection, it will cause them to see the fake OS.");
			provider = new Provider(this, null);
		}

		ffmpegProvider = provider;
	}

}