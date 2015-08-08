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

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

public class Schematic {

    public static void pasteNether(File f, final Location loc, boolean log) {
        try {
			long start = System.currentTimeMillis();
			if (log)
				Bukkit.getLogger().info("Pasting " + f.getAbsolutePath() + "...");

			if (!f.exists()) {
				Bukkit.getLogger().info("Schematic doesn't exist, skipping.");
				return;
			}

			NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(new FileInputStream(f)));
			CompoundTag nbt = (CompoundTag) nbtStream.readNamedTag().getTag();
			if (nbt == null)
				return;

            short width = nbt.getShort("Width");
            short height = nbt.getShort("Height");
            short length = nbt.getShort("Length");

			loadChunks(loc.getWorld(), loc.getBlockX(), loc.getBlockZ(), width, length);

            final byte[] blocks = nbt.getByteArray("Blocks");
            final byte[] data = nbt.getByteArray("Data");

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < length; z++) {
						final int index = y * width * length + z * width + x;
						final Location l = new Location(loc.getWorld(), x + loc.getX(), y + loc.getY(), z + loc.getZ());
						final int b = blocks[index] & 0xFF;
						final Block block = l.getBlock();
						final Material m = Material.getMaterial(b);

						block.setType(m, false);
						if (m == Material.MOB_SPAWNER) {
							CreatureSpawner spawner = (CreatureSpawner) block.getState();
							spawner.setSpawnedType(EntityType.BLAZE);
							spawner.setDelay(1);
							spawner.update();
						} else {
							block.setData(data[index]);
						}
					}
				}
			}

			if (log)
				Bukkit.getLogger().info("Pasted successfully in " + (System.currentTimeMillis() - start) + " milliseconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	// Generate the chunks around the schematic
	private static void loadChunks(World worldf, int x, int z, int sizeX, int sizeY) {
		sizeX = sizeX / 32;
		sizeY = sizeY / 32;
		sizeX ++;
		sizeY ++;

		Chunk chunk = worldf.getChunkAt(new org.bukkit.Location(worldf, x, 40, z));
		chunk.load(true);
		int cx = chunk.getX() - sizeX;
		int cz = chunk.getZ() - sizeY;
		while (cx < chunk.getX() + sizeX) {
			while (cz < chunk.getZ() + sizeY) {
				if (cx != chunk.getX() ||cz != chunk.getZ()) {
					worldf.getChunkAt(cx, cz).load(true);
				}
				cz++;
			}
			cx++;
		}
	}
}