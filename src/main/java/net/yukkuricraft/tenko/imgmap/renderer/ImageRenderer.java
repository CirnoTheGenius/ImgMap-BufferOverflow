package net.yukkuricraft.tenko.imgmap.renderer;

import net.yukkuricraft.tenko.imgmap.objs.UseOnceObject;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;

public class ImageRenderer extends MapRenderer {

	//Allow the image to be garbage collected as opposed to
	//occupying space in memory like versions of ImgMap do.
	private final UseOnceObject<Image> image;
	private boolean drawn = false; // hasRendered... that was too long to type. I could have just used 'drawn'...

	public ImageRenderer(Image img){
		image = new UseOnceObject<Image>(img);
	}

	@Override
	public void render(MapView view, MapCanvas canvas, Player player){
		if(!drawn){
			canvas.drawImage(0, 0, image.get());
			drawn = true;
		}
	}

}