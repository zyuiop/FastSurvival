package net.zyuiop.fastsurvival.lootreplacer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyuiop
 */
public class LootRule {
	private static HashMap<Material, LootRule> map = new HashMap<>();

	private Material source;
	private MaterialData target;
	private int multiplier;
	private double probability = 1;

	protected LootRule(Material source, MaterialData target, int multiplier) {
		this.source = source;
		this.target = target;
		this.multiplier = multiplier;
	}

	protected LootRule(Material source, MaterialData target, int multiplier, double probability) {
		this.source = source;
		this.target = target;
		this.multiplier = multiplier;
		this.probability = probability;
	}

	public static LootRule addRule(Map<?,?> config) {
		if (!config.containsKey("sourceMaterial") || !config.containsKey("targetMaterial"))
			return null;

		Material source = getMaterial((String) config.get("sourceMaterial"));
		Material target = getMaterial((String) config.get("targetMaterial"));
		if (source == null || target == null)
			return null;

		byte targetData = 0;
		try {
			targetData = Byte.parseByte((String) config.get("targetData"));
		} catch (Exception ignored) {}

		int multiplier = 1;
		try {
			multiplier = Integer.parseInt((String) config.get("targetMultiplier"));
		} catch (Exception ignored) {}

		LootRule rule;
		if (config.containsKey("targetProbability")) {
			try {
				rule = new LootRule(source, new MaterialData(target, targetData), multiplier, Double.parseDouble((String) config.get("targetProbability")));
			} catch (Exception ignored) {
				rule = new LootRule(source, new MaterialData(target, targetData), multiplier);
			}
		} else {
			rule = new LootRule(source, new MaterialData(target, targetData), multiplier);
		}
		map.put(source, rule);

		return rule;
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

	public double getProbability() {
		return probability;
	}

	public static LootRule getRule(Material material) {
		return map.get(material);
	}

	public Material getSource() {
		return source;
	}

	public MaterialData getTarget() {
		return target;
	}

	public int getMultiplier() {
		return multiplier;
	}
}
