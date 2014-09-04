package net.yukkuricraft.tenko.imgmap.renderer;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.Stack;

public class SlideshowRenderer extends MapRenderer {

	//askjdnaskdnsa i can't spell.
	private Stack<Image> images; // Unfortunetly, we can't use UseOnceObject here since we need to loop through these.
	private BukkitRunnable runnable = new BukkitRunnable(){
		public void run(){
			Image image = images.pop();
			canvas.drawImage(0, 0, image);
			images.push(image);
			Bukkit.getScheduler().scheduleSyncDelayedTask(ImgMap.getInstance(), this, delayBetween);
		}
	};
	private long delayBetween;
	private MapCanvas canvas;
	private boolean drawn;

	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(!drawn){
			this.canvas = canvas;
			runnable.run();
			drawn = true;
		}
	}


}