package net.yukkuricraft.tenko.imgmap.command;

import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class ClearMapCommand extends CommandHandler {

	public ClearMapCommand(){
		super(0, true, "imgmap.command.clearmap", "/clearmap [id]");
	}

	@Override
	public void preExecution(Command command, CommandSender sender) {
		if(command.getName().equalsIgnoreCase("fixmap")){
			commandFailure(sender, "/fixmap is deprecated and is only retained for compatibility sake. The new command is \"/clearmap\".");
		}
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments){
		World world;
		short id;

		if(sender instanceof Player){
			world = ((Player)sender).getWorld();
		} else {
			world = Bukkit.getWorlds().get(0);
		}

		if(arguments.length > 0){
			id = Short.valueOf(arguments[0]);
		} else {
			if(sender instanceof Player){
				ItemStack stack = ((Player)sender).getItemInHand();
				if(stack == null || (stack.getType() != Material.MAP)){
					commandFailure(sender, "You must be holding a map or provide an ID to reset to use this command!");
					return;
				}

				id = stack.getDurability();
			} else {
				commandFailure(sender, "You need to provide an ID to reset");
				return;
			}
		}

		MapView view = Bukkit.getMap(id);
		MapHelper.removeRenderers(view);
		view.addRenderer(NMSHelper.getDefaultRenderer(id, world));
		((Player)sender).sendMap(view); // Update the map now.
		commandSuccess(sender, "Cleared map ID " + id + "!");
	}

}