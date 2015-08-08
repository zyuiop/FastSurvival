package net.zyuiop.fastsurvival.entitesloots;

import net.zyuiop.fastsurvival.lootreplacer.LootReplacer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

/**
 * @author zyuiop
 */
public class AddLootRule implements EntityLootRule {
	private final Material addStuff;
	private final int randomMin;
	private final int randomMax;

	public AddLootRule(Material addStuff, int randomMin, int randomMax) {
		this.addStuff = addStuff;
		this.randomMin = randomMin;
		this.randomMax = randomMax;
	}

	@Override
	public void replaceLoots(List<ItemStack> loots) {
		int qty = new Random().nextInt(randomMax - 1) + randomMin;
		if (qty > 0)
			loots.add(LootReplacer.mark(new ItemStack(addStuff, qty)));
	}
}
