package net.yukkuricraft.tenko.imgmap.graphproc;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public abstract class AbstractSafeRunnable implements Runnable {

	protected static Logger logger = Logger.getLogger("ImgMap");
	protected SoftReference<Thread> thread; // I love using Reference classes.
	private AtomicBoolean isRunning = new AtomicBoolean(false);

	@Override
	public void run(){
		if(isRunning == null){
			return; // ... Why would it be null here?
		}

		this.isRunning.set(true);

		while(this.isRunning.get()){
			running();
		}
	}

	/**
	 * Starts the thread, or returns the current thread.
	 * @return The newly started thread or the currently running thread if already started.
	 */
	public Thread start(){
		if(thread == null || thread.get() == null){
			Thread thread = new Thread(this);
			thread.start();
			this.thread = new SoftReference<>(thread);
			return thread;
		}

		return thread.get();
	}

	/**
	 * Get a thread
	 * @return The thread at hand.
	 */
	public Thread getThread(){
		return thread.get();
	}

	/**
	 * Attempts to stop a thread and returns it, or if it hasn't been started, returns null.
	 * There is no guarantee the thread has stopped.
	 * @return The current thread running or null, if the thread is null or is dead.
	 */
	public Thread stop(){
		if(thread == null || thread.get() == null || !thread.get().isAlive()){
			return null;
		}

		this.isRunning.compareAndSet(true, false);
		return thread.get();
	}

	public abstract void running();

}