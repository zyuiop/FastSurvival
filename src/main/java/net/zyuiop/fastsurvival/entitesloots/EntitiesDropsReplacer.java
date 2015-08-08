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
						int randomMin = Integer.parseInt((String) map.get("randomMin"));
						int randomMax = Integer.parseInt((String) map.get("randomMax"));
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
