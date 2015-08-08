package net.zyuiop.fastsurvival;

import net.zyuiop.fastsurvival.entitesloots.EntitiesDropsReplacer;
import net.zyuiop.fastsurvival.generation.WorldGenerator;
import net.zyuiop.fastsurvival.lootreplacer.LootReplacer;
import net.zyuiop.fastsurvival.others.FasterObsidian;
import net.zyuiop.fastsurvival.others.HigherCrafts;
import net.zyuiop.fastsurvival.others.StoneBreaksAll;
import net.zyuiop.fastsurvival.others.TreeCapitator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author zyuiop
 */
public class FastSurvival extends JavaPlugin {
	public static FastSurvival instance;

	public void onEnable() {
		// Load config
		this.saveDefaultConfig();

		Configuration configuration = getConfig();

		// World Generator
		if (configuration.getBoolean("generator.enabled", false)) {
			Bukkit.getPluginManager().registerEvents(new WorldGenerator(configuration), this);
			Bukkit.getLogger().info("Improved generation is enabled !");
		}

		if (configuration.getBoolean("lootReplacer.enabled", false)) {
			Bukkit.getPluginManager().registerEvents(new LootReplacer(configuration.getConfigurationSection("lootReplacer")), this);
			Bukkit.getLogger().info("Improved loots are enabled !");
		}

		if (configuration.getBoolean("entitiesDrops.enabled", false)) {
			Bukkit.getPluginManager().registerEvents(new EntitiesDropsReplacer(configuration.getConfigurationSection("entitiesDrops")), this);
			Bukkit.getLogger().info("Improved entities drops are enabled !");
		}

		if (configuration.getBoolean("higherCrafts", false)) {
			Bukkit.getPluginManager().registerEvents(new HigherCrafts(), this);
			Bukkit.getLogger().info("Improved crafts are enabled !");
		}

		if (configuration.getBoolean("fasterTrees", false)) {
			Bukkit.getPluginManager().registerEvents(new TreeCapitator(), this);
			Bukkit.getLogger().info("Faster tree breaking is enabled !");
		}

		if (configuration.getBoolean("fasterObisidian", false)) {
			Bukkit.getPluginManager().registerEvents(new FasterObsidian(), this);
			Bukkit.getLogger().info("Faster obsidian breaking is enabled !");
		}

		if (configuration.getBoolean("stoneBreaksAll", false)) {
			Bukkit.getPluginManager().registerEvents(new StoneBreaksAll(), this);
			Bukkit.getLogger().info("Better tools breaking is enabled !");
		}
	}
}
