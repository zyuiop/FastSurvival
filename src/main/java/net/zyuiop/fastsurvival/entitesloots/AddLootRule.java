/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 zyuiop
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
