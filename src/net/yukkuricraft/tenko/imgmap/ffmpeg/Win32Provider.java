package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.File;

public class Win32Provider extends Provider {

	public Win32Provider(ImgMap plugin){
		super(plugin, new File(plugin.getDataFolder(), "ffmpeg.exe"));
	}

	@Override
	public void execute(String video, File file){
		startProcess(buildProcess().command(generateArguments(video, file, video.indexOf('.') == -1)));
	}

}