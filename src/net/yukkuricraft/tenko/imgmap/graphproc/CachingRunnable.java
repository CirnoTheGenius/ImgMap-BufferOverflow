package net.yukkuricraft.tenko.imgmap.graphproc;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CachingRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger("ImgMap");
	private final File file;
	private final ImageReader reader = new GIFImageReader(new GIFImageReaderSpi()); // A bit unsafe since it's com.sun...
	private final int id;
	private int delayMilliseconds;
	private Object[][] packets;

	public CachingRunnable(int id, File file){
		this.file = file;
		this.id = id;
	}

	public int getDelay(){
		return delayMilliseconds;
	}

	public Object[][] getPackets(){
		return packets;
	}

	/**
	 * Plan: Go from each frame directly to the packet itself.
	 */
	@Override
	public void run(){
		ExecutorService service = Executors.newCachedThreadPool(); // Pretty sure this is JRE7...
		int numFrames;
		try{
			reader.setInput(ImageIO.createImageInputStream(file));
			numFrames = reader.getNumImages(true);
		} catch (IOException e){
			e.printStackTrace();
			return;
		}

		//A block to obtain the delay in milliseconds between frames.
		{
			try{
				IIOMetadata metadata = reader.getImageMetadata(0);
				IIOMetadataNode node = (IIOMetadataNode)metadata.getAsTree(metadata.getNativeMetadataFormatName());
				for(int i = 0; i < node.getLength(); i++){
					if(node.item(i).getNodeName().equalsIgnoreCase("GraphicControlExtension")){
						delayMilliseconds = Integer.valueOf(((IIOMetadataNode)node.item(i)).getAttribute("delayTime"));
					}
				}
			} catch (IOException e){
				delayMilliseconds = 10;
			}
		}

		packets = new Object[numFrames][128];

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

					IOHelper.resizeImage(image); //Reisze it down to 128x128. (Let's test it with QUALITY resizes!)
					for(int x=0; x < 128; x++){
						byte[] row = new byte[131];

						for(int y=0; y < 128; y++){
							row[y+3] = MapHelper.matchColor(image.getRGB(x, y));
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

}