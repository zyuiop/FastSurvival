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

package net.zyuiop.fastsurvival;

import net.zyuiop.fastsurvival.entitesloots.EntitiesDropsReplacer;
import net.zyuiop.fastsurvival.generation.WorldGenerator;
import net.zyuiop.fastsurvival.lootreplacer.LootReplacer;
import net.zyuiop.fastsurvival.others.FasterObsidian;
import net.zyuiop.fastsurvival.others.HigherCrafts;
import net.zyuiop.fastsurvival.others.StoneBreaksAll;
import net.zyuiop.fastsurvival.others.TreeCapitator;
import net.zyuiop.fastsurvival.updater.CommandUpdate;
import net.zyuiop.fastsurvival.updater.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.MalformedURLException;

/**
 * @author zyuiop
 */
public class FastSurvival extends JavaPlugin {
	public static FastSurvival instance;
	public Updater updater;

	public void onEnable() {
		instance = this;
		try {
			updater = new Updater();
			Bukkit.getPluginManager().registerEvents(updater, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// Load config
		this.saveDefaultConfig();

		getCommand("update").setExecutor(new CommandUpdate());

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

		if (configuration.getBoolean("autoUpdate", false)) {
			Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> updater.doUpdate(Bukkit.getConsoleSender()), 20, 60*30*20);
		}
	}

	@Override
	public File getFile() {
		return super.getFile();
	}
}
