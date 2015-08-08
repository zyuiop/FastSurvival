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

package net.zyuiop.fastsurvival.generation;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.util.Map;

public class WorldGenerator implements Listener {
	private FastSurvivalPopulator populator;

	@EventHandler(priority = EventPriority.HIGH)
	public void onWorldInit(final WorldInitEvent event) {
		loadWorld(event.getWorld());
	}

	private void loadWorld(World world) {
		if (world.getEnvironment() == World.Environment.NORMAL) {
			world.getPopulators().add(populator);
			Bukkit.getLogger().info("Added custom generation to world " + world.getName());
		}
	}

	public WorldGenerator(Configuration configuration) {
		populator = new FastSurvivalPopulator();

		for (Map<?,?> map : configuration.getMapList("generator.rules")) {
			if (!map.containsKey("material")
					|| !map.containsKey("perChunk")
					|| !map.containsKey("minY")
					|| !map.containsKey("maxY")
					|| !map.containsKey("clusterSize")) {
				continue;
			}

			Material material;
			Object confMaterial = map.get("material");
			if (confMaterial instanceof Integer)
				material = Material.getMaterial((Integer) confMaterial);
			else {
				material = Material.valueOf((String) confMaterial);
				if (material == null) {
					try {
						material = Material.getMaterial(Integer.parseInt((String) confMaterial));
					} catch (Exception e) {
						Bukkit.getLogger().warning("Cannot parse rule : unknown material " + confMaterial);
						continue;
					}
				}
			}

			try {
				int perChunk = (Integer) map.get("perChunk");
				int minY = (Integer) map.get("minY");
				int maxY = (Integer) map.get("maxY");
				int clusterSize = (Integer) map.get("clusterSize");
				int data = 0;
				if (map.containsKey("data"))
					data = (Integer) map.get("data");

				if (!map.containsKey("deleteOthers") || !Boolean.parseBoolean((String) map.get("deleteOthers"))) {
					populator.registerRule(new BlocksRule(material, data, perChunk, minY, maxY, clusterSize));
				} else {
					populator.registerRule(new BlocksRule(material, data, perChunk, minY, maxY, clusterSize), Material.STONE);
				}
				Bukkit.getLogger().info("[Generator] Added new rule : " + perChunk + " clusters of " + clusterSize + " blocks of " + material + " will be added within Y " + minY + "-" + maxY);

			} catch (Exception e) {
				Bukkit.getLogger().warning("Cannot parse rule : malformed number for material " + confMaterial);
				e.printStackTrace();
			}
		}

		Bukkit.getWorlds().forEach(this::loadWorld);
	}
}