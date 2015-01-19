package net.yukkuricraft.tenko.imgmap.graphproc;

import java.util.logging.Level;

public class AnimationRunnable extends MultiUserRunnable {

	private Object[][] packets;
	private int index = 0;
	private int delay;

	public AnimationRunnable(Object[][] packets, int delay){
		this.packets = packets;
		this.delay = delay;
	}

	@Override
	public Thread stop(){
		this.packets = null;
		return super.stop();
	}

	@Override
	public void running(){
		this.index++;
		if(this.index >= this.packets.length){
			this.stop();
		}

		Object[] frame = packets[index];
		if(frame != null){
			flushChannels();

			for(Object o : frame){
				writePacket(o);
			}

			flushChannels();
		}

		try{
			//Thread.sleep(delay);
			Thread.sleep(33); // Hah. Fun fact: 24 FPS is (rounded) 42ms per frame.
			// LET'S BE CRAZY! LET'S DO 30/29.97FPS!
		} catch (InterruptedException e){
			logger.log(Level.WARNING, "Something told us to wake up before the animation ended!");
		}
	}

}