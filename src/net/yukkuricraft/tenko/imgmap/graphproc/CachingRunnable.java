package net.yukkuricraft.tenko.imgmap.graphproc;

import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import net.yukkuricraft.tenko.imgmap.objs.GifDecoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CachingRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger("ImgMap");
	private final File file;
	private final GifDecoder decoder = new GifDecoder();
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
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2); // Pretty sure this is JRE7...
		int numFrames;
		try{
			decoder.read(new FileInputStream(file));
		} catch (IOException e){
			e.printStackTrace();
			return;
		}
		numFrames = decoder.getFrameCount();
		this.delayMilliseconds = decoder.getDelay(1); //Grab some sample from the second frame.

		packets = new Object[numFrames][128];

		for(int i=0; i < numFrames; i++){
			final int index = i;

			service.execute(new Runnable(){

				@Override
				public void run(){
					Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

					BufferedImage image = decoder.getFrame(index);

					IOHelper.resizeImage(image); //Reisze it down to 128x128. (Let's test it with QUALITY resizes!)
					for(int x=0; x < 128; x++){
						byte[] row = new byte[131];
						row[1] = (byte)x;

						for(int y=0; y < 128; y++){
							row[y+3] = MapHelper.matchColor(image.getRGB(x, y));
						}

						//I think this works?
						packets[index][x] = NMSHelper.getMapPacket(id, row);
					}

					image.flush();
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
		decoder.dispose();
	}

}