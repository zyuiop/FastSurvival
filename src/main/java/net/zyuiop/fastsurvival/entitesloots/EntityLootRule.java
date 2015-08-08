package net.zyuiop.fastsurvival.entitesloots;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author zyuiop
 */
public interface EntityLootRule {
	void replaceLoots(List<ItemStack> loots);
}
