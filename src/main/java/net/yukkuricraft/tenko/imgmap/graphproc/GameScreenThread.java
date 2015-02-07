package net.yukkuricraft.tenko.imgmap.graphproc;

import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GameScreenThread extends MultiUserRunnable {

	private BufferedImage frame = new BufferedImage(/*GPU.WIDTH, GPU.HEIGHT*/128, 128, BufferedImage.TYPE_INT_RGB);
	private int[] data = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
	private int id;
	/*private GPU gpu;*/

	public GameScreenThread(int id/*, GPU gpu*/){
		this.id = id;
		/*this.gpu = gpu;*/
	}

	public void running(){
		checkState();
		/*System.arraycopy(gpu.getCurrentFrame(), 0, data, 0, data.length);*/
		IOHelper.resizeImage(frame);

		for(int x=0; x < 128; x++){
			byte[] packetData = new byte[131];
			packetData[1] = (byte)x;

			for(int y=3; y < 131; y++){
				packetData[y] = MapHelper.matchColor(frame.getRGB(x, y));
			}

			writePacket(NMSHelper.getMapPacket(id, packetData));
		}
		flushChannels();

		try{
			Thread.sleep(20);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

}