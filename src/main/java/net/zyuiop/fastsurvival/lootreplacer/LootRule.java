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
			multiplier = (Integer) config.get("targetMultiplier");
		} catch (Exception ignored) {}

		LootRule rule;
		if (config.containsKey("targetProbability")) {
			try {
				rule = new LootRule(source, new MaterialData(target, targetData), multiplier, (Double) config.get("targetProbability"));
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
