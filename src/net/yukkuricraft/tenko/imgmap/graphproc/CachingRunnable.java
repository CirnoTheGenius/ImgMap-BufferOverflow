package net.yukkuricraft.tenko.imgmap.graphproc;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import org.bukkit.map.MapPalette;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CachingRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger("ImgMap");
	private final URL url;
	private final ImageReader reader = new GIFImageReader(new GIFImageReaderSpi()); // A bit unsafe since it's com.sun...
	private final Color[] colors = stealColors();
	private final int id;

	public CachingRunnable(int id, URL url){
		this.url = url;
		this.id = id;
	}

	/**
	 * Plan: Go from each frame directly to the packet itself.
	 */
	@Override
	public void run(){
		ExecutorService service = Executors.newCachedThreadPool(); // Pretty sure this is JRE7...
		int numFrames;
		try{
			reader.setInput(ImageIO.createImageInputStream(url.openStream()));
			numFrames = reader.getNumImages(true);
		} catch (IOException e){
			e.printStackTrace();
			return;
		}

		final Object[][] packets = new Object[numFrames][128];

		for(int i=0; i < numFrames; i++){
			final int index = i;

			service.execute(new Runnable(){

				@Override
				public void run(){
					BufferedImage image;

					try{
						image = reader.read(index);
					} catch (IOException e){
						e.printStackTrace();;
						return;
					}

					IOHelper.resizeImage(image); //Reisze it down to 128x128.
					for(int x=0; x < 128; x++){
						byte[] row = new byte[131];

						for(int y=0; y < 128; y++){
							row[y+3] = matchColor(image.getRGB(x, y));
						}

						//I think this works?
						packets[index][x] = NMSHelper.getMapPacket(id, row);
					}
				}
			});
		}

		service.shutdown();
		try{
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);  // Have fun waiting 292.277 years!
		} catch (InterruptedException e){
			logger.log(Level.SEVERE, "We were woken up early!", e);
			return;
		}
		reader.dispose(); //I hope this doesn't leak.
	}

	// A bit confusing, but c1 is the int representation of the Color from frame.getRGB()
	// Why did I make such a function?
	// Because I hate how the old one created a Color object that was thrown away at the end.
	private double getDistance(int c1, Color c2){
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



	public byte matchColor(int c1){
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
	public Color[] stealColors(){
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