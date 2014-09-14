package net.yukkuricraft.tenko.imgmap.graphproc;

import net.minecraft.util.org.apache.commons.io.IOUtils;
import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadRunnable implements Runnable {

	private String video_id;
	private File result;

	public DownloadRunnable(String video_id){
		this.video_id = video_id;
		this.result = new File(ImgMap.getLVideosDirectory(), video_id + ".gif");
	}

	@Override
	public void run(){
		if(result.exists()){
			return; // The video is already downloaded. Invalid ID or not, it's a gif file that exists.
		}

		//Test if the video exists.
		InputStream stream = null;
		InputStreamReader reader = null;
		try{
			URL url = new URL("http://gdata.youtube.com/feeds/api/videos/" + video_id);
			stream = url.openStream();
			reader = new InputStreamReader(stream);

			String str = IOUtils.readLines(reader).get(0);
			if(str.trim().startsWith("Invalid id")){
				Logger.getLogger("ImgMap").log(Level.WARNING, "Invalid ID (" + video_id + ")!"); //TODO: Notify sender, not console.
			}

			reader.close();
			stream.close();
		} catch (IOException e){
			return;
		} finally {
			if(stream != null){
				try{
					stream.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}

			if(reader != null){
				try {
					reader.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}

		ImgMap.getFFmpegProvider().execute(video_id, result); // This blocks.
	}

	public File getFile(){
		return result;
	}

}