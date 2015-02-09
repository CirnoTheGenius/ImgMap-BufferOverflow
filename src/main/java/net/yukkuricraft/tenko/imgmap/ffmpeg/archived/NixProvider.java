package net.yukkuricraft.tenko.imgmap.ffmpeg.archived;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.File;

public class NixProvider extends Provider {

	public NixProvider(ImgMap plugin){
		super(plugin, new File(plugin.getDataFolder(), "ffmpeg"));
		DEFAULT_FFMPEG_ARGS[0] = "./ffmpeg"; // Special case for *nix OS.
	}

	@Override
	public void execute(String video, File file){
		startProcess(buildProcess().command(generateArguments(video, file, !video.contains("."))));
	}

}