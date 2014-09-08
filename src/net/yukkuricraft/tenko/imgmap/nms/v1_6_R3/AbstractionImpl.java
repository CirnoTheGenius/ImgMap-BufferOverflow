package net.yukkuricraft.tenko.imgmap.nms.v1_6_R3;

import net.minecraft.server.v1_6_R3.ItemStack;
import net.minecraft.server.v1_6_R3.ItemWorldMap;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_6_R3.map.CraftMapRenderer;
import org.bukkit.map.MapRenderer;

public class AbstractionImpl implements NMSHelper.Abstraction {

	public MapRenderer getDefaultRenderer(short id, World world){
		org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.MAP);
		bukkitStack.setDurability(id);
		ItemStack nmsStack = CraftItemStack.asNMSCopy(bukkitStack);
		return new CraftMapRenderer(null, ((ItemWorldMap)nmsStack.getItem()).getSavedMap(nmsStack, ((CraftWorld)world).getHandle()));
	}

}