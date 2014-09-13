package net.yukkuricraft.tenko.imgmap.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetMapCommand extends CommandHandler {

	public GetMapCommand(){
		super(1, true, "imgmap.getmap", "/getmap <id>");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments){
		Player plyr = (Player)sender;
		ItemStack stack;

		if(StringUtils.isNumeric(arguments[0])){
			stack = new ItemStack(Material.MAP);
			stack.setDurability(Short.valueOf(arguments[0]));
		} else {
			commandFailure(sender, "You need to provide a map ID.");
			return;
		}

		ItemStack old = plyr.getInventory().getItemInHand();
		if(old != null || old.getType() != Material.AIR){
			plyr.getInventory().addItem(old);
			commandSuccess(sender, "Moved your current item in hand somewhere else.");
		}

		plyr.getInventory().setItemInHand(stack);
		commandSuccess(sender, "Gave map ID " + arguments[0]);
	}

}