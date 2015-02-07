package net.yukkuricraft.tenko.imgmap.graphproc;

public class ThreadCallback {

	public static Thread createThread(final Runnable runnable, final Runnable callback){
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run(){
				runnable.run();
				callback.run();
			}

		});

		return thread;
	}

}