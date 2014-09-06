package net.yukkuricraft.tenko.imgmap.ffmpeg;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class Provider {

	/*
	"-i", "http://www.ytapi.com/?vid=" + YTAPIVideoObj.this.id + "&format=direct&itag=160", "-r", "10", "-threads", "0", "-vf", "scale=128:128,format=rgb8,format=rgb24", "-y",
	 */

	protected Logger logger = Logger.getLogger("ImgMap-FFmpegProv");
	private final File FFMPEG_EXE;

	public Provider(File ffmpeg){
		this.FFMPEG_EXE = ffmpeg;
	}

	public static final String[] DEFAULT_FFMPEG_ARGS = {
			"-i", "http://www.ytapi.com/?vid=$VID_ID$&format=direct&itag=160",
			"-r", "10",
			"-threads", "0",
			"-vf", "scale=128:128",
			"-y"
	};

	private final String[] generateDefault(String vid, File o){
		String[] args = Arrays.copyOf(DEFAULT_FFMPEG_ARGS, DEFAULT_FFMPEG_ARGS.length);
		args[1] = args[1].replace("$VID_ID$", vid);

		ArrayUtils.add(args, o.getAbsolutePath()); // Append the file output destination.
		return args;
	}

	final String[] win32Args(String video_id, File output){
		String[] defArgs = generateDefault(video_id, output);
		ArrayUtils.add(defArgs, 0, getExecutablePath());
		return defArgs;
	}

	final String[] nixArgs(String video_id, File output){
		String[] defArgs = generateDefault(video_id, output);
		ArrayUtils.add(defArgs, 0, "./ffmpeg");
		return defArgs;
	}

	public boolean isAvailable(){
		return FFMPEG_EXE.exists();
	}

	public String getExecutablePath(){
		return FFMPEG_EXE.getAbsolutePath();
	}

	public abstract boolean execute(String video_id);

}
