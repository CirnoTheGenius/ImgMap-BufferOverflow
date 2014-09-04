package net.yukkuricraft.tenko.imgmap.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class DrawImageCommand extends CommandHandler {

	public DrawImageCommand(){
		super(1, true, "imgmap.drawimage", "/drawimage <url> [map id (if you're using from console)] [-l (if image is local)] [-s (to save)]");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments){
		ItemStack stack = ((Player)sender).getItemInHand(); // Safe to cast since we check if they're non-player earlier.
		if(stack == null || (stack.getType() != Material.MAP)){
			commandFailure(sender, "You must be holding a map to use this command!");
			return;
		}

		MapView view = Bukkit.getMap(stack.getDurability());

	}

}