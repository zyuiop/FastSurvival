package net.zyuiop.fastsurvival.lootreplacer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ContainerBlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * @author zyuiop
 */
public class LootReplacer implements Listener {

	public LootReplacer(ConfigurationSection configuration) {
		for (Map<?,?> map : configuration.getMapList("rules")) {
			LootRule rule = LootRule.addRule(map);
			if (rule != null) {
				Bukkit.getLogger().info("[LootReplacer] Replaced " + rule.getSource() + " loots with " + rule.getTarget().getItemType() + ", multiplying stack size by " + rule.getMultiplier());
			}
		}
	}

	public static boolean isMarked(ItemStack stack) {
		return stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null && stack.getItemMeta().getDisplayName().startsWith("§r");
	}

	public static ItemStack mark(ItemStack stack) {
		if (isMarked(stack))
			return stack;

		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§r" + meta.getDisplayName());
		ItemStack ret = stack.clone();
		ret.setItemMeta(meta);
		return ret;
	}

	@EventHandler
	public void playerDrop(PlayerDropItemEvent event) {
		event.getItemDrop().setItemStack(mark(event.getItemDrop().getItemStack())); // Avoid duplication bug.
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event) {
		BlockState state = event.getBlock().getState();
		if (state instanceof InventoryHolder) {
			Inventory inventory = ((InventoryHolder) state).getInventory();
			for (int slot = 0; slot < inventory.getSize(); slot++) {
				ItemStack stack = inventory.getItem(slot);
				if (stack != null) {
					inventory.setItem(slot, mark(stack));
				}
			}
		}
	}

	@EventHandler
	public void blockExplode(EntityExplodeEvent entityExplodeEvent) {
		for (Block block : entityExplodeEvent.blockList()) {
			BlockState state = block.getState();
			if (state instanceof InventoryHolder) {
				Inventory inventory = ((InventoryHolder) state).getInventory();
				for (int slot = 0; slot < inventory.getSize(); slot++) {
					ItemStack stack = inventory.getItem(slot);
					if (stack != null) {
						inventory.setItem(slot, mark(stack));
					}
				}
			}
		}
	}

	@EventHandler
	public void itemSpawn(ItemSpawnEvent event) {
		ItemStack stack = event.getEntity().getItemStack();
		if (stack == null || isMarked(stack))
			return;

		Material material = stack.getType();
		LootRule rule = LootRule.getRule(material);
		if (rule == null)
			return;

		if (rule.getProbability() < 1 && new Random().nextDouble() > rule.getProbability())
			return;

		MaterialData target = rule.getTarget();
		ItemStack replace = new ItemStack(target.getItemType(), stack.getAmount() * rule.getMultiplier());
		replace.setData(target);

		event.getEntity().setItemStack(mark(replace));
	}
}
