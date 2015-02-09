package net.yukkuricraft.tenko.imgmap.processing;

import org.bukkit.Bukkit;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A runnable whose running() function is called multiple times.
 */
public abstract class AbstractRunnable implements Runnable {

	private final AtomicBoolean RUNNING = new AtomicBoolean(false);

	@Override
	public void run(){
		Bukkit.getScheduler().runTaskAsynchronously(ImgMap.)
	}



}