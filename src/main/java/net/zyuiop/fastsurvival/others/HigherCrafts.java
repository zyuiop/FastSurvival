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
