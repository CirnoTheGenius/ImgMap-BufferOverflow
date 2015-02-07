package net.yukkuricraft.tenko.imgmap.graphproc;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.File;

public class DownloadRunnable implements Runnable {

	private String url;
	private File result;

	public DownloadRunnable(String url){
		this.url = url;

		if(url.indexOf('.') == -1){
			result = new File(ImgMap.getLVideosDirectory(), url + ".gif");
		} else {
			result = new File(ImgMap.getLImagesDirectory(), url.substring(url.lastIndexOf('/'), url.length()));
		}
	}

	@Override
	public void run(){
		if(result.exists()){
			return;
		}

		ImgMap.getFFmpegProvider().execute(url, result); // This blocks.
	}

	public File getFile(){
		return result;
	}

}