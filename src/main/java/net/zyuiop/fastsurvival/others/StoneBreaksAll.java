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
