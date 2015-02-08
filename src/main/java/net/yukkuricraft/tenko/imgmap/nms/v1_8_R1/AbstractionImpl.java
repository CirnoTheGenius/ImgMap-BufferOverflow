package net.yukkuricraft.tenko.imgmap.nms.v1_8_R1;

import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R1.*;
import net.yukkuricraft.tenko.imgmap.nms.Abstraction;
import net.yukkuricraft.tenko.imgmap.nms.ProxyChannel;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R1.map.CraftMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class AbstractionImpl implements Abstraction {

    private final List<MapIcon> empty = Collections.unmodifiableList(new ArrayList<MapIcon>());

    @Override
    public MapRenderer getDefaultRenderer(short id, World world){
        org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.MAP);
        bukkitStack.setDurability(id);
        ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
        // https://github.com/Bukkit/CraftBukkit/blob/61e57e1196fc917d0e8f9c5f1ee48818a85fd5b9/src/main/java/org/bukkit/craftbukkit/map/CraftMapRenderer.java
        // It _was_ stored in the past; it was just never used, passed, or interacted with.
        // Don't ask why.
        return new CraftMapRenderer(null, ((ItemWorldMap)nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld)world).getHandle()));
    }

     /*
      *      Directly from the 1.8.1 source
      *
      *      public void sendMap(MapView map) {
      *          if(this.getHandle().playerConnection != null) {
      *              RenderData data = ((CraftMapView)map).render(this);
      *              ArrayList icons = new ArrayList();
      *              Iterator packet = data.cursors.iterator();
      *
      *              while(packet.hasNext()) {
      *                  MapCursor cursor = (MapCursor)packet.next();
      *                  if(cursor.isVisible()) {
      *                     icons.add(new MapIcon(cursor.getRawType(), cursor.getX(), cursor.getY(), cursor.getDirection()));
      *                  }
      *              }
      *
      *              PacketPlayOutMap packet1 = new PacketPlayOutMap(map.getId(), map.getScale().getValue(), icons, data.buffer, 0, 0, 0, 0);
      *              this.getHandle().playerConnection.sendPacket(packet1);
      *              }
      *          }
      *
      *
      *      Seriously, what the hell is data.buffer?
      *      Okay, so to the client, buffer (or colors) is supposed to be 16384 bytes (which is 128 x 128)
      */
    @Override
    public Object getPacketData(int id, byte[] data){
        return new PacketPlayOutMap(id, MapView.Scale.FARTHEST.getValue(), empty, data, 0, 0, 0, 0);
    }

    /*
     * Suggestion to self:
     * Instead of using the Channel object to send the packet, use the PlayerConnection.sendPacket(Packet) method.
     * Reason for not doing this in the past: PlayerConnection.sendPacket(Packet) used to create large amounts of lag.
     */
    @Override
    public ProxyChannel newChannel(Player player){
        if(!(player instanceof CraftPlayer)){
            Abstraction.LOGGER.log(Level.WARNING, "Detected Non-CraftBukkit player! Kinda odd that this plugin still functions.");
            return null;
        }

        Object netty = ((CraftPlayer)player).getHandle().playerConnection.networkManager;
        try{
            Field f = netty.getClass().getDeclaredField("i"); // m -> i
            f.setAccessible(true);
            final Channel channel = (Channel)f.get(netty);
            return new ProxyChannel(){

                private Channel oneechan = channel; // Why do I do this? I don't want to hard reference the channel.

                @Override
                public void sendPacket(Object o){
                    if(isOpen() && o instanceof Packet){
                        oneechan.write(o);
                    }
                }

                @Override
                public void flush(){
                    if(isOpen()){
                        oneechan.flush();
                    }
                }

                @Override
                public boolean isOpen(){
                    return oneechan != null && oneechan.isOpen();
                }

                @Override
                public void close() {
                    this.oneechan = null;
                }

            };
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            return null;
        }
    }

}