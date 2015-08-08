package net.zyuiop.fastsurvival.others;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

/**
 * @author zyuiop
 */
public class StoneBreaksAll implements Listener {
	private ImmutableList<Material> list;
	public StoneBreaksAll() {
		list = ImmutableList.of(DIAMOND_ORE,
		LAPIS_ORE,
		GOLD_ORE,
		OBSIDIAN,
		IRON_ORE,
		REDSTONE_ORE,
		QUARTZ_ORE);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Material material = event.getBlock().getType();
		if (list.contains(material)) {
			event.setCancelled(true);
			event.getBlock().breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE));
		}
	}
}
