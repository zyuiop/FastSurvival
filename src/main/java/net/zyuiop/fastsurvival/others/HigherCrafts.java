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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * @author zyuiop
 */
public class HigherCrafts implements Listener {
	public HigherCrafts() {
		final ShapedRecipe cobblePickaxe = new ShapedRecipe(new ItemStack(Material.STONE_PICKAXE));
		cobblePickaxe.shape("WWW", "ASA", "ASA");
		cobblePickaxe.setIngredient('W', Material.WOOD);
		cobblePickaxe.setIngredient('S', Material.STICK);
		cobblePickaxe.setIngredient('A', Material.AIR);

		final ShapedRecipe cobbleAxe = new ShapedRecipe(new ItemStack(Material.STONE_AXE));
		cobbleAxe.shape("WWA", "WSA", "ASA");
		cobbleAxe.setIngredient('W', Material.WOOD);
		cobbleAxe.setIngredient('S', Material.STICK);
		cobbleAxe.setIngredient('A', Material.AIR);

		final ShapedRecipe cobbleAxeB = new ShapedRecipe(new ItemStack(Material.STONE_AXE));
		cobbleAxeB.shape("AWW", "ASW", "ASA");
		cobbleAxeB.setIngredient('W', Material.WOOD);
		cobbleAxeB.setIngredient('S', Material.STICK);
		cobbleAxeB.setIngredient('A', Material.AIR);

		final ShapedRecipe cobbleSword = new ShapedRecipe(new ItemStack(Material.STONE_SWORD));
		cobbleSword.shape("AWA", "AWA", "ASA");
		cobbleSword.setIngredient('W', Material.WOOD);
		cobbleSword.setIngredient('S', Material.STICK);
		cobbleSword.setIngredient('A', Material.AIR);

		final ShapedRecipe cobbleShoveel = new ShapedRecipe(new ItemStack(Material.STONE_SPADE));
		cobbleShoveel.shape("AWA", "ASA", "ASA");
		cobbleShoveel.setIngredient('W', Material.WOOD);
		cobbleShoveel.setIngredient('S', Material.STICK);
		cobbleShoveel.setIngredient('A', Material.AIR);

		Bukkit.getServer().addRecipe(cobbleAxe);
		Bukkit.getServer().addRecipe(cobblePickaxe);
		Bukkit.getServer().addRecipe(cobbleSword);
		Bukkit.getServer().addRecipe(cobbleAxeB);
		Bukkit.getServer().addRecipe(cobbleSword);
	}

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		if (event.getRecipe().getResult().getType() == Material.WOOD_PICKAXE)
			event.getInventory().setResult(new ItemStack(Material.STONE_PICKAXE));
		else if (event.getRecipe().getResult().getType() == Material.WOOD_AXE)
			event.getInventory().setResult(new ItemStack(Material.STONE_AXE));
		else if (event.getRecipe().getResult().getType() == Material.WOOD_SWORD)
			event.getInventory().setResult(new ItemStack(Material.STONE_SWORD));
		else if (event.getRecipe().getResult().getType() == Material.WOOD_SPADE)
			event.getInventory().setResult(new ItemStack(Material.STONE_SPADE));
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		if (event.getRecipe().getResult().getType() == Material.WOOD_PICKAXE)
			event.getInventory().setResult(new ItemStack(Material.STONE_PICKAXE));
		else if (event.getRecipe().getResult().getType() == Material.WOOD_AXE)
			event.getInventory().setResult(new ItemStack(Material.STONE_AXE));
		else if (event.getRecipe().getResult().getType() == Material.WOOD_SPADE)
			event.getInventory().setResult(new ItemStack(Material.STONE_SPADE));
	}
}
