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

import java.lang.ref.WeakReference;
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
      */
    @Override
    public Object getPacketData(int id, byte[] data){
        return new PacketPlayOutMap(id, MapView.Scale.FARTHEST.getValue(), empty, data, 0, 0, 0, 0);
    }

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

                private WeakReference<Channel> oneechan = new WeakReference<Channel>(channel);

                @Override
                public void sendPacket(Object o){
                    if(isOpen() && o instanceof Packet){
                        oneechan.get().write(o);
                    }
                }

                @Override
                public void flush(){
                    if(isOpen()){
                        oneechan.get().flush();
                    }
                }

                @Override
                public boolean isOpen(){
                    return oneechan.get() != null && oneechan.get().isOpen();
                }

            };
        } catch (ReflectiveOperationException e){
            e.printStackTrace();
            return null;
        }
    }

}