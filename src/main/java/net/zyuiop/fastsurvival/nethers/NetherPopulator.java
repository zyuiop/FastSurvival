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

import net.zyuiop.fastsurvival.generation.BlocksRule;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.io.File;
import java.util.*;

public class NetherPopulator extends BlockPopulator {

	private List<File> schematics = new ArrayList<>();
	private double probability = 0.01;
	private final boolean log;


	public NetherPopulator(double probability, boolean log) {
		this.probability = probability;
		this.log = log;
	}

	public NetherPopulator(List<File> schematics, double probability, boolean log) {
		this.probability = probability;
		this.log = log;
		this.schematics.addAll(schematics);
	}

	public void generateBlazeFortress(World world, int x, int z, Random random) {
		File file = getRandomSchematic(random);
		if (file == null) {
			Bukkit.getLogger().severe("Impossible to paste nether : no schematic available.");
		} else {
			Location location = new Location(world, x, random.nextInt(10) + 5, z);
			Schematic.pasteNether(file, location, log);
		}
	}

	public void addSchematic(File file) {
		schematics.add(file);
	}

	public File getRandomSchematic(Random random) {
		if (schematics.size() == 0)
			return null;

		int i = random.nextInt(schematics.size());
		File file = schematics.get(i);
		if (!file.exists()) {
			schematics.remove(i);
			return getRandomSchematic(random);
		}
		return file;
	}

	@Override
    public void populate(World world, Random random, Chunk chunk) {
		if (random.nextDouble() <= probability) {
			int xFortress = chunk.getX()*16 + random.nextInt(15);
			int zFortress = chunk.getZ()*16 + random.nextInt(15);
			generateBlazeFortress(world, xFortress, zFortress, random);
		}
    }
}
