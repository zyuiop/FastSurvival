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

package net.zyuiop.fastsurvival.nethers;

import net.zyuiop.fastsurvival.FastSurvival;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WorldGenerator implements Listener {
	private NetherPopulator populator;

	@EventHandler(priority = EventPriority.HIGH)
	public void onWorldInit(final WorldInitEvent event) {
		loadWorld(event.getWorld());
	}

	private void loadWorld(World world) {
		if (world.getEnvironment() == World.Environment.NORMAL) {
			world.getPopulators().add(populator);
		}
	}

	public WorldGenerator(double proba, boolean log) {
		populator = new NetherPopulator(proba, log);

		// Fetch schematics
		try {
			CodeSource src = FastSurvival.class.getProtectionDomain().getCodeSource();
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());

				while(true) {
					ZipEntry e = zip.getNextEntry();
					if (e == null)
						break;

					String name = e.getName();
					if (name.startsWith("schematics/")) {
						FastSurvival.instance.saveResource(name, false);
						Bukkit.getLogger().info("[NetherGen] Extracted schematic " + name);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Find schematics in folder
		File directory = new File(FastSurvival.instance.getDataFolder() + "/schematics/");
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				populator.addSchematic(file);
				Bukkit.getLogger().info("[NetherGen] Added schematic " + file.getName());
			}
		}

		// Finish enabling
		Bukkit.getWorlds().forEach(this::loadWorld);
	}
}