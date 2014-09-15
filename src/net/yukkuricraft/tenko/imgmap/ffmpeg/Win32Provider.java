package net.yukkuricraft.tenko.imgmap.ffmpeg;

import net.yukkuricraft.tenko.imgmap.ImgMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Win32Provider extends Provider {

	public Win32Provider(ImgMap plugin) {
		super(plugin, new File(plugin.getDataFolder(), "ffmpeg.exe"));
	}

	@Override
	public File execute(String video_id, File target){
		try {
			if(plugin.get() != null){
				ProcessBuilder procbuild = new ProcessBuilder();
				procbuild.directory(plugin.get().getDataFolder());
				procbuild.command(win32Args(video_id, target));
				procbuild.redirectErrorStream(true);

				Process process = procbuild.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String l;
				while((l = reader.readLine()) != null){
					logger.info("FFmpeg (" + video_id + ")-> " + l);
				}
				reader.close();
				return target;
			} else {
				throw new IllegalStateException("The primary plugin object was garbage collected! Is ImgMap loaded?");
			}
		} catch (Throwable t){
			t.printStackTrace();
			return null;
		}
	}

	@Override
	public File executeNonYouTube(String url, File target){
		try {
			if(plugin.get() != null){
				ProcessBuilder procbuild = new ProcessBuilder();
				procbuild.directory(plugin.get().getDataFolder());
				String[] arguments = win32Args("empty", target);
				arguments[2] = url;
				procbuild.command(arguments);
				procbuild.redirectErrorStream(true);

				Process process = procbuild.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String l;
				while((l = reader.readLine()) != null){
					logger.info("FFmpeg (" + url + ")-> " + l);
				}
				reader.close();
				return target;
			} else {
				throw new IllegalStateException("The primary plugin object was garbage collected! Is ImgMap loaded?");
			}
		} catch (Throwable t){
			t.printStackTrace();
			return null;
		}
	}

}