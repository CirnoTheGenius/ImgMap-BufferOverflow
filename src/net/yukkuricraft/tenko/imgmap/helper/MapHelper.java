package net.yukkuricraft.tenko.imgmap.helper;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapHelper {

	private static Logger logger = ImgMap.getInstance().getLogger();

	private MapHelper(){
		throw new InstantiationError("Attempted to create an instance of a helper class.");
	}

	public static void removeRenderers(MapView view){
		Iterator<MapRenderer> iter_mr = view.getRenderers().iterator();
		while(iter_mr.hasNext()){
			iter_mr.remove();
		}
	}

	public static Image fetchImage(URL url){
		try {
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			int response = conn.getResponseCode();
			if(!(response >= 200 && response <= 207)){
				logger.log(Level.WARNING, "Received HTTP response code " + response + ". Attempting to read image anyways.");
			}

			Image image = ImageIO.read(conn.getInputStream());
			conn.disconnect();
			return image;
		} catch (IOException e){
			return null;
		}
	}

}