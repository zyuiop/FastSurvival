package net.zyuiop.fastsurvival.entitesloots;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author zyuiop
 */
public class RepairLootRule implements EntityLootRule {
	private final Material repairStuff;

	public RepairLootRule(Material repairStuff) {
		this.repairStuff = repairStuff;
	}

	@Override
	public void replaceLoots(List<ItemStack> loots) {
		loots.stream().filter(stack -> stack.getType() == repairStuff).forEach(stack -> stack.setDurability((short) 0));
	}
}
