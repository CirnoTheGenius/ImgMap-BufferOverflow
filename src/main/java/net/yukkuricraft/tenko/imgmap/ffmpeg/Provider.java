package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import net.yukkuricraft.tenko.imgmap.helper.YouTubeAPIRegex;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Provider {

	private final static String[] FFMPEG_ARGUMENTS = {
			"<insert program dir here>",
			"-i", "<insert url>",
			"-r", "29.97",
			"-threads", "0",
			"-vf", "scale=128:128",
			"-y", "<insert output>"
	};

	private File ffmpegExecutable;
	private File pluginDirectory;

	public Provider(ImgMap plugin, OS os){
		pluginDirectory = plugin.getDataFolder();
		ffmpegExecutable = new File(pluginDirectory, "ffmpeg" + os.getExtension());
		FFMPEG_ARGUMENTS[0] = "ffmpeg" + os.getExtension();
	}

	public String[] buildArguments(String url){
		String[] arguments = Arrays.copyOf(FFMPEG_ARGUMENTS, FFMPEG_ARGUMENTS.length);
		File output;

		if(url.contains("/")){ // This is an external URL.
			arguments[2] = "\"" + url + "\"";
			output = new File(pluginDirectory, url.substring(url.lastIndexOf("/") + 1) + ".gif");
			arguments[arguments.length - 1] = "\"" + output.getAbsolutePath() + "\"";
		} else { // Assume YouTube
			arguments[2] = "\"" + YouTubeAPIRegex.getDirectLinks(url).get(0) + "\"";
			output = new File(pluginDirectory, url + ".gif");
			arguments[arguments.length - 1] = "\"" + output.getAbsolutePath()+ "\"";
		}

		return arguments;
	}

	public ProcessBuilder buildProcess(String[] command){
		ProcessBuilder builder = new ProcessBuilder();
		builder.inheritIO();
		builder.command(command);
		builder.directory(pluginDirectory);
		return builder;
	}

	public void execute(String url){
		try{
			buildProcess(buildArguments(url)).start();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public boolean isAvailable(){
		return !ffmpegExecutable.exists();
	}

	public enum OS {

		WIN, OSX, NIX;

		public String getExtension(){
			if(this == WIN){
				return ".exe";
			} else if(this == OSX || this == NIX){
				return "";
			}

			return ".invalidOS";
		}

	}

}
