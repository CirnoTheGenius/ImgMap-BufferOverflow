package net.yukkuricraft.tenko.imgmap.command;

import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.renderer.ImageRenderer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class DrawImageCommand extends CommandHandler {

	public DrawImageCommand(){
		super(1, true, "imgmap.command.drawimage", "/drawimage <url> [-l (if image is local)] [-s (to save)]");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments){
		ItemStack stack = ((Player)sender).getItemInHand(); // Safe to cast since we check if they're non-player earlier.
		if(stack == null || (stack.getType() != Material.MAP)){
			commandFailure(sender, "You must be holding a map to use this command!");
			return;
		}

		MapView view = Bukkit.getMap(stack.getDurability());
		BufferedImage image;
		try {
			if(ArrayUtils.contains(arguments, "-l")){
				image = IOHelper.fetchLocalImage(arguments[0]);
			} else {
				image = IOHelper.fetchImage(new URL(arguments[0]));
			}
		} catch (IOException e){
			commandFailure(sender, "Failed to retrieve image!");
			e.printStackTrace();
			return;
		}

		IOHelper.resizeImage(image);
		MapHelper.removeRenderers(view);
		view.addRenderer(new ImageRenderer(image));
		((Player)sender).sendMap(view); // Update the map now.
		commandSuccess(sender, "Drawing " + arguments[0] + "...");
	}

}