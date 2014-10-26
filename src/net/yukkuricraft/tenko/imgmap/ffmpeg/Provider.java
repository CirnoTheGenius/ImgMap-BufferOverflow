package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Provider {

	protected final Logger logger = Logger.getLogger("ImgMap-FFmpegProv");
	protected final WeakReference<ImgMap> plugin;

	private final File FFMPEG_EXECUTABLE;
	// Hooray for scope violation!
	protected final String[] DEFAULT_FFMPEG_ARGS = {
			"<insert program dir here>",
			"-i", "<insert url>",
			"-r", "10",
			"-threads", "0",
			"-vf", "scale=128:128",
			"-y", "<insert output>"
	};

	public Provider(ImgMap plugin, File ffmpeg){
		this.plugin = new WeakReference<ImgMap>(plugin);
		FFMPEG_EXECUTABLE = ffmpeg;
	}

	public abstract void execute(String url, File output);

	public String[] generateArguments(String string, File output, boolean ytVideo){
		String[] copy = Arrays.copyOf(DEFAULT_FFMPEG_ARGS, DEFAULT_FFMPEG_ARGS.length);
		copy[0] = FFMPEG_EXECUTABLE.getAbsolutePath();
		if(ytVideo){
			// 240P MP4/V only (I don't use FLV because reasons. No seriously I don't have a valid reason not to).
			// Why the lowest quality? Lighter on server + we need to convert it to 128x128 anyways.
			// ex. URL: http://www.ytapi.com/api/wO4pNpZ9syY/direct/133/
			copy[2] = "http://www.ytapi.com/api/" + string + "/direct/133/";
		} else {
			copy[2] = string;
		}
		copy[copy.length - 1] = output.getAbsolutePath();
		return copy;
	}

	public ProcessBuilder buildProcess(){
		if(plugin.get() == null){
			throw new IllegalStateException();
		}

		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(plugin.get().getDataFolder());
		builder.redirectErrorStream(true);
		return builder;
	}

	public boolean isAvailable(){
		return FFMPEG_EXECUTABLE.exists();
	}

	public void startProcess(ProcessBuilder builder){
		try{
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String l;
			while((l = reader.readLine()) != null){
				logger.log(Level.INFO, l);
			}
			reader.close();
		} catch (IOException e){
			logger.log(Level.SEVERE, "Failed to start process with command " + builder.command().toString(), e);
		}
	}

}