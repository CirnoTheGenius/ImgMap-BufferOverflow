package net.yukkuricraft.tenko.imgmap2.utils;

import net.yukkuricraft.tenko.imgmap2.ImgMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class ImageUtils {

	public Image getLocalImage(String fileName) throws IOException {
		File localImages = new File(ImgMap.getSingleton().getDataFolder(), "localImages");
		File imageFile = new File(localImages, fileName);

		return ImageIO.read(imageFile);
	}

	public static BufferedImage getImage(String url) throws IOException {
		return getImage(new URL(url));
	}

	public static BufferedImage getImage(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		int response = conn.getResponseCode();
		if(!(response >= 200 && response <= 207)){
			ImgMap.logThrowable(Level.WARNING, "Received HTTP response code " + response + ". Attempting to read image anyways.");
		}

		BufferedImage image = ImageIO.read(conn.getInputStream());
		conn.disconnect();
		return image;
	}

	public Image resizeImage(Image image){

	}

}