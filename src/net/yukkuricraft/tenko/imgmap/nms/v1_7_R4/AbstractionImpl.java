package net.yukkuricraft.tenko.imgmap.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.ItemWorldMap;
import net.yukkuricraft.tenko.imgmap.nms.NMSHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R4.map.CraftMapRenderer;
import org.bukkit.map.MapRenderer;

public class AbstractionImpl implements NMSHelper.Abstraction {

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

}