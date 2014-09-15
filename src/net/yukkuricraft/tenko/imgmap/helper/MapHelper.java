package net.yukkuricraft.tenko.imgmap.helper;

import net.yukkuricraft.tenko.imgmap.renderer.ContinuousRenderer;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.logging.Logger;

public class MapHelper {

	private static Logger logger = Logger.getLogger("ImgMap");
	private static final Color[] colors = stealColors();

	private MapHelper(){
		throw new InstantiationError("Attempted to create an instance of a helper class.");
	}

	public static void removeRenderers(MapView view){
		Iterator<MapRenderer> iter_mr = view.getRenderers().iterator();
		while(iter_mr.hasNext()){
			MapRenderer renderer = iter_mr.next();
			if(renderer instanceof ContinuousRenderer){
				((ContinuousRenderer)renderer).stopRendering();
			}
			iter_mr.remove();
		}
	}

	// A bit confusing, but c1 is the int representation of the Color from frame.getRGB()
	// Why did I make such a function?
	// Because I hate how the old one created a Color object that was thrown away at the end.
	private static double getDistance(int c1, Color c2){
		c1 = 0xff000000 | c1;
		double rmean = ((c1 >> 16) + c2.getRed()) / 2.0;
		double r = ((c1 >> 16) & 0xFF) - c2.getRed();
		double g = ((c1 >> 8) & 0xFF) - c2.getGreen();
		int b = ((c1 >> 0) & 0xFF) - c2.getBlue();
		double weightR = 2 + rmean / 256.0;
		double weightG = 4.0;
		double weightB = 2 + (255 - rmean) / 256.0;
		return weightR * r * r + weightG * g * g + weightB * b * b;
	}

	public static byte matchColor(int c1){
		double best = -1;
		int index = 0;
		for(int i = 4; i < colors.length; i++){
			double calc = getDistance(c1, colors[i]);
			if(best == -1 || calc < best){
				best = calc;
				index = i;
			}
		}

		return (byte) (index < 128 ? index : -129 + (index - 127));
	}

	// Steal the colors from MapPalette instead of copy-pasting it.
	private static Color[] stealColors(){
		try{
			Field field = MapPalette.class.getDeclaredField("colors");
			field.setAccessible(true);
			return (Color[]) field.get(null);
		}catch (Throwable e){
			e.printStackTrace();
			return null;
		}
	}

}