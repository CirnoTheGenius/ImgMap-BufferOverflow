package net.yukkuricraft.tenko.imgmap.helper;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.Iterator;
import java.util.logging.Logger;

public class MapHelper {

	private static Logger logger = ImgMap.getInstance().getLogger();

	private MapHelper(){
		throw new InstantiationError("Attempted to create an instance of a helper class.");
	}

	public static void removeRenderers(MapView view){
		Iterator<MapRenderer> iter_mr = view.getRenderers().iterator();
		while(iter_mr.hasNext()){
			iter_mr.next();
			iter_mr.remove();
		}
	}

}