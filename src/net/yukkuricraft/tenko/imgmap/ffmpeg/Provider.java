package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class Provider {

	/*
	"-i", "http://www.ytapi.com/?vid=" + YTAPIVideoObj.this.id + "&format=direct&itag=160",
	"-r", "10", "-threads", "0", "-vf", "scale=128:128,format=rgb8,format=rgb24", "-y",
	 */

	protected Logger logger = Logger.getLogger("ImgMap-FFmpegProv");
	private final File FFMPEG_EXE;
	protected final WeakReference<ImgMap> plugin;

	public Provider(ImgMap plugin, File ffmpeg){
		this.FFMPEG_EXE = ffmpeg;
		this.plugin = new WeakReference<ImgMap>(plugin);
	}

	public static final String[] DEFAULT_FFMPEG_ARGS = {
			"<insert program dir here>",
			"-i", "http://www.ytapi.com/?vid=$VID_ID$&format=direct&itag=160",
			"-r", "10",
			"-threads", "0",
			"-vf", "scale=128:128",
			"-y", "<insert stuffs>"
	};

	private final String[] generateDefault(String vid, File o){
		String[] args = Arrays.copyOf(DEFAULT_FFMPEG_ARGS, DEFAULT_FFMPEG_ARGS.length);
		args[2] = args[2].replace("$VID_ID$", vid);
		args[args.length-1] = o.getAbsolutePath();
		return args;
	}

	protected final String[] win32Args(String video_id, File output){
		String[] defArgs = generateDefault(video_id, output);
		defArgs[0] = getExecutablePath();
		return defArgs;
	}

	protected final String[] nixArgs(String video_id, File output){
		String[] defArgs = generateDefault(video_id, output);
		defArgs[0] = "./ffmpeg";
		return defArgs;
	}

	public boolean isAvailable(){
		return FFMPEG_EXE.exists();
	}

	public String getExecutablePath(){
		return FFMPEG_EXE.getAbsolutePath();
	}

	public abstract File execute(String video_id, File target);

}
