package net.yukkuricraft.tenko.imgmap.ffmpeg;

import java.io.File;

public class UnknownProvider extends Provider {

	public UnknownProvider(){
		super(null, null);
	}

	@Override
	public void execute(String str, File file){
		return;
	}

	@Override
	public boolean isAvailable(){
		return false;
	}

}