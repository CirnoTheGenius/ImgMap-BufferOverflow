package net.yukkuricraft.tenko.imgmap2.command;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

public class DrawYTVideoCommand extends CommandHandler {

	public DrawYTVideoCommand(){
		super(1, true, "imgmap.command.drawytvideo", "/drawytvideo <YouTube Video ID> [-n if non-youtube source; must be a direct link to video]");
	}

	@Override
	public void executeCommand(CommandSender sender, String[] arguments){
		if(!ImgMap.isVideoStreamingEnabled()){
			commandFailure(sender, "Videos have been disabled. Is it enabled in the configuration and is the FFmpeg executable for your OS in the ImgMap folder?");
			return;
		}

		Player plyr = ((Player)sender);
		if(plyr.getItemInHand().getType() != Material.MAP){
			if(plyr.getItemInHand().getType() == Material.JUKEBOX){
				//I'm allowed to have fun... right?
				commandFailure(sender, "What masterpiece shall we play today? Oh wait... You can't stream music, unfortunately.");
				return;
			}

			commandFailure(sender, "You can't stream videos onto that!");
			return;
		}

		final MapView view = Bukkit.getMap(plyr.getItemInHand().getDurability());
		// TODO: this entire part again
		commandSuccess(sender, "Downloading video. The video will automatically start playing after it is finished.");
	}

}