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

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author zyuiop
 */
public class FasterObsidian implements Listener {

	@EventHandler
	public void onBeginBreak(BlockDamageEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
		if (event.getBlock().getType() == Material.OBSIDIAN)
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20000, 2, true, true));
		else
			event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
	}

}
