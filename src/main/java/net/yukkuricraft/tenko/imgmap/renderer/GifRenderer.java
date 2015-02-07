package net.yukkuricraft.tenko.imgmap.renderer;

import net.yukkuricraft.tenko.imgmap.graphproc.AnimationRunnable;
import net.yukkuricraft.tenko.imgmap.graphproc.CachingRunnable;
import net.yukkuricraft.tenko.imgmap.graphproc.ThreadCallback;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class GifRenderer extends MapRenderer implements ContinuousRenderer {

	private final AtomicBoolean READY = new AtomicBoolean(false);
	private AnimationRunnable animation;
	private CachingRunnable cacher;
	private File file;

	public GifRenderer(File file){
		this.file = file;
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(cacher == null){
			cacher = new CachingRunnable(view.getId(), file);
			Thread thread = ThreadCallback.createThread(cacher, new Runnable() {

				@Override
				public void run() {
					READY.compareAndSet(false, true);
				}

			});
			thread.start();
		} else if(READY.get()){
			if(animation == null){
				animation = new AnimationRunnable(cacher.getPackets(), cacher.getDelay());
				animation.addPlayer(player);
				animation.start();
			} else {
				animation.addPlayer(player);
			}
		}
	}

	@Override
	public void stopRendering(){
		animation.stop();
	}

}