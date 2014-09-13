package net.yukkuricraft.tenko.imgmap.graphproc;

import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import net.yukkuricraft.tenko.imgmap.nms.ProxyChannel;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Represents an animation that will be played for multiple people.
 */
public abstract class MultiUserRunnable extends AbstractSafeRunnable {

	private Map<UUID, ProxyChannel> channels = new HashMap<UUID, ProxyChannel>();

	public void addPlayer(Player player){
		ProxyChannel channel = NMSHelper.getChannel(player);
		if(channel == null){
			logger.log(Level.WARNING, "Failed to generate a proxy channel class.");
			return;
		}

		channels.put(player.getUniqueId(), channel);
	}

	public void writePacket(Object packet){
		for(ProxyChannel channel : channels.values()){
			channel.sendPacket(packet);
		}
	}

	public void flushChannels(){
		for(ProxyChannel channel : channels.values()){
			channel.flush();
		}
	}

	public void removeDeadChannels(){
		Iterator<Map.Entry<UUID, ProxyChannel>> iter = channels.entrySet().iterator();
		while(iter.hasNext()){
			if(!iter.next().getValue().isOpen()){
				iter.remove();
			}
		}
	}

}