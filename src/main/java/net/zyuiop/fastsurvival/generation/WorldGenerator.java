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
		if (event.getWorld().getEnvironment() == World.Environment.NORMAL) {
			event.getWorld().getPopulators().add(populator);
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


	}
}