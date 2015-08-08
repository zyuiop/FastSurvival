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

import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * @author zyuiop
 */
public class EntitiesDropsReplacer implements Listener {
	private HashMultimap<EntityType, EntityLootRule> rules = HashMultimap.create();

	public EntitiesDropsReplacer(ConfigurationSection section) {
		for (Map<?,?> map : section.getMapList("rules")) {
			if (!map.containsKey("type") || !map.containsKey("entityType") || !map.containsKey("material"))
				continue;

			EntityType type = EntityType.valueOf((String) map.get("entityType"));
			if (type == null) {
				Bukkit.getLogger().warning("Unknown entity type : " + map.get("entityType"));
				continue;
			}

			Material material = Material.getMaterial((String) map.get("material"));
			if (material == null) {
				Bukkit.getLogger().warning("Unknown material type : " + map.get("material"));
				continue;
			}

			switch ((String) map.get("type")) {
				case "repair":
					rules.put(type, new RepairLootRule(material));
					break;
				case "add":
					try {
						int randomMin = (Integer) map.get("randomMin");
						int randomMax = (Integer) map.get("randomMax");
						rules.put(type, new AddLootRule(material, randomMin, randomMax));
					} catch (Exception e) {
						Bukkit.getLogger().warning("Could not add rule.");
						e.printStackTrace();
					}
					break;
				default:
					Bukkit.getLogger().info("Unknown rule type : " + map.get("type"));
			}
		}
	}

	private static Material getMaterial(String input) {
		Material material = Material.getMaterial(input);
		if (material == null) {
			try {
				material = Material.getMaterial(Integer.parseInt(input));
			} catch (Exception e) {
				Bukkit.getLogger().info("Unrecognized material " + input);
			}
		}
		return material;
	}

	@EventHandler
	public void onEntityDie(EntityDeathEvent entityDeathEvent) {
		EntityType entity = entityDeathEvent.getEntityType();
		List<ItemStack> loots = entityDeathEvent.getDrops();
		for (EntityLootRule rule : rules.get(entity))
			rule.replaceLoots(loots);
	}
}
