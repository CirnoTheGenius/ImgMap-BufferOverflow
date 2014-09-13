package net.yukkuricraft.tenko.imgmap.helper;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import net.yukkuricraft.tenko.imgmap.graphproc.DownloadRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOHelper {

	private static final Logger logger = Logger.getLogger("ImgMap");

	public static Image fetchLocalImage(String fileName) throws IOException {
		File file = new File(ImgMap.getLImagesDirectory(), fileName);
		if(!file.exists()){
			throw new FileNotFoundException("Couldn't locate image file " + fileName);
		}

		return ImageIO.read(file);
	}

	public static Image fetchImage(String url) throws IOException {
		return fetchImage(new URL(url));
	}

	public static Image fetchImage(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		int response = conn.getResponseCode();
		if(!(response >= 200 && response <= 207)){
			logger.log(Level.WARNING, "Received HTTP response code " + response + ". Attempting to read image anyways.");
		}

		Image image = ImageIO.read(conn.getInputStream());
		conn.disconnect();
		return image;
	}

	public static void resizeImage(BufferedImage image){
		resizeImage(image, TargetType.QUALITY);
	}

	public static void resizeImage(BufferedImage image, TargetType type){
		Graphics2D graphics = image.createGraphics();

		switch(type){
			case SPEED:{
				break;
			}

			case QUALITY:{
				graphics.setComposite(AlphaComposite.Src);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				break;
			}
		}

		graphics.drawImage(image, 0, 0, 128, 128, null);
		graphics.dispose();
	}

	public static DownloadRunnable downloadVideo(String videoId){
		DownloadRunnable runnable = new DownloadRunnable(videoId);
		return runnable;
	}

	public static enum TargetType {
		/**
		 * If "SPEED", we don't perform any fancy edits to the picture.
		 */
		SPEED,
		/**
		 * If "QUALITY", we perform all the fancy edits.
		 */
		QUALITY
	}

}