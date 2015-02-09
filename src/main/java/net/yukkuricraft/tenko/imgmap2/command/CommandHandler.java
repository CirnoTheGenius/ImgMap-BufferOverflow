package net.yukkuricraft.tenko.imgmap2.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandHandler implements CommandExecutor {

	private int minimum;
	private boolean playerRequired;
	private String permission;
	private String usage;

	/**
	 * Defines basic information about a CommandHandler.
	 * @param min - The minimum amount of arguments required to execute this command.
	 * @param player - Whether or not we need a player for this command.
	 * @param permission - The permission we use to check whether or not the player has permission.
	 * @param usage - How to properly use this command.
	 */
	public CommandHandler(int min, boolean player, String permission, String usage){
		this.minimum = min;
		this.playerRequired = player;
		this.permission = permission;
		this.usage = usage;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments){
		if(!sender.hasPermission(permission)){
			commandFailure(sender, "The map is sad. The map wants you to do that, but it can't let you. The map will leak pixels :'(");
			return true;
		}

		if(playerRequired && !(sender instanceof Player)){
			commandFailure(sender, "You must be a player to use this command!");
			return true;
		}

		if(arguments.length < minimum){
			commandFailure(sender, "You're missing an argument or two.");
			commandFailure(sender, "Usage: " + usage);
			return true;
		}

		preExecution(command, sender);
		executeCommand(sender, arguments);
		return true;
	}

	public abstract void executeCommand(CommandSender sender, String[] arguments);

	public void preExecution(Command command, CommandSender sender){} // Used to notify users of deprecation.

	void commandFailure(CommandSender sender, String reason){
		sender.sendMessage(ChatColor.RED + "[ImgMap] " + reason);
	}

	void commandSuccess(CommandSender sender, String reason){
		sender.sendMessage(ChatColor.GREEN + "[ImgMap] " + reason);
	}

}