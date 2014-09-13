package net.yukkuricraft.tenko.imgmap.ffmpeg;

import java.io.File;

public class UnknownProvider extends Provider {

	public UnknownProvider(){
		super(null, null);
	}

	@Override
	public File execute(String id, File target){
		return null;
	}

	@Override
	public boolean isAvailable(){
		return false;
	}

	@Override
	public String getExecutablePath(){
		return "";
	}

}