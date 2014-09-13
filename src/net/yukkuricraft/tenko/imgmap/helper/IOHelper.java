package net.yukkuricraft.tenko.imgmap.helper;

import net.yukkuricraft.tenko.imgmap.ImgMap;

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
		Graphics2D graphics = image.createGraphics();
		graphics.drawImage(image, 0, 0, 128, 128, null);
		graphics.dispose();
	}

}