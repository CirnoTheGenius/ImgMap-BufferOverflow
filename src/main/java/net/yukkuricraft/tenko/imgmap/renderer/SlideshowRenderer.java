package net.yukkuricraft.tenko.imgmap.renderer;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.lang.ref.SoftReference;
import java.util.Stack;

public class SlideshowRenderer extends MapRenderer {

	//askjdnaskdnsa i can't spell.
	private Stack<Image> images; // Unfortunetly, we can't use UseOnceObject here since we need to loop through these.
	private BukkitRunnable runnable = new BukkitRunnable(){
		public void run(){
			if(plugin.get() != null){
				Image image = images.pop();
				canvas.drawImage(0, 0, image);
				images.push(image);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.get(), this, delayBetween);
			}
		}
	};
	private long delayBetween;
	private MapCanvas canvas;
	private boolean drawn;
	private SoftReference<ImgMap> plugin;
	// SoftReference is a reference type that is willing to let go of it's object if and only if it is
	// neccessary.

	public SlideshowRenderer(ImgMap plugin){
		this.plugin = new SoftReference<ImgMap>(plugin);
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(!drawn){
			this.canvas = canvas;
			runnable.run();
			drawn = true;
		}
	}


}