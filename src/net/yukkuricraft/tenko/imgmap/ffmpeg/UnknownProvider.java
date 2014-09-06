package net.yukkuricraft.tenko.imgmap.ffmpeg;

public class UnknownProvider extends Provider {

	public UnknownProvider(){
		super(null);
	}

	@Override
	public boolean execute(String id){
		return false;
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