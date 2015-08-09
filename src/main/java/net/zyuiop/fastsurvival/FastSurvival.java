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
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author zyuiop
 */
public class FastSurvival extends JavaPlugin {
	public static FastSurvival instance;
	public Updater updater;

	private boolean checkLibraries() {
		try {
			Class clazz = Class.forName("org.apache.commons.lang3.StringUtils");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().info("Missing libraries, downloading them.");
			try {
				URL url = new URL("http://static.zyuiop.net/libs.jar");
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				File output = new File(getFile().getParentFile(), "libs.jar");
				FileOutputStream fos = new FileOutputStream(output);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

				Bukkit.getLogger().info("Downloaded, loading...");
				getPluginLoader().enablePlugin(getPluginLoader().loadPlugin(output));
				Bukkit.getLogger().info("Done !");
			} catch (InvalidPluginException | IOException e1) {
				Bukkit.getLogger().severe("Libraries download failed. Stopping.");
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public void onEnable() {
		instance = this;

		if (!checkLibraries()) {
			Bukkit.getLogger().severe("Missing libraries. Plugin had to stop. Please add apache-commons and guava to classpath.");
			getPluginLoader().disablePlugin(this);
			return;
		}

		try {
			updater = new Updater();
			Bukkit.getPluginManager().registerEvents(updater, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// Load config
		this.saveDefaultConfig();

		Bukkit.getPluginCommand("update").setExecutor(new CommandUpdate());
		Bukkit.getPluginCommand("credits").setExecutor(new CommandCredit());

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
			Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> updater.doUpdate(Bukkit.getConsoleSender()), 20, 60 * 30 * 20);
		}

		if (configuration.getBoolean("netherGen.enabled", false)) {
			Bukkit.getPluginManager().registerEvents(new net.zyuiop.fastsurvival.nethers.WorldGenerator(configuration.getDouble("netherGen.probability", 0.01), configuration.getBoolean("netherGen.log", false)), this);
			Bukkit.getLogger().info("Nether generation is enabled !");
		}
	}

	@Override
	public File getFile() {
		return super.getFile();
	}
}
