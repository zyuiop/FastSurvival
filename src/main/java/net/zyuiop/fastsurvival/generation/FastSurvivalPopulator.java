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
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.*;

public class FastSurvivalPopulator extends BlockPopulator {

    private List<BlocksRule> blocks = new ArrayList<>();
    private Map<Material, Material> removeBlocks = new HashMap<>();

    public void registerRule(BlocksRule rule) {
        blocks.add(rule);
    }

    public void registerRule(BlocksRule rule, Material replaceOthers) {
        registerRule(rule);
        removeBlocks.put(rule.id, replaceOthers);
    }

	@Override
    public void populate(World world, Random random, Chunk chunk) {
		Bukkit.getLogger().fine("[Gen] Executing custom generation rules on chunk " + chunk.getX() + "-" + chunk.getZ());

        int x = 0;
        while (x < 16) {
            int z = 0;
            while (z < 16) {
                int y = 0;
                while (y < world.getMaxHeight()) {
                    Block block = chunk.getBlock(x, y, z);
                    if (removeBlocks.containsKey(block.getType()))
                        block.setType(removeBlocks.get(block.getType()));

                    y++;
                }
                z++;
            }
            x++;
        }

        for (BlocksRule bloc : blocks) {
            try {
                for (int i = 0; i < bloc.round; i++) {
                    x = chunk.getX() * 16 + random.nextInt(16);
                    int y = bloc.minY + random.nextInt(bloc.maxY - bloc.minY);
                    int z = chunk.getZ() * 16 + random.nextInt(16);

                    generate(world, random, x, y, z, bloc.size, bloc);

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void generate (World world, Random rand, int x, int y, int z, int size, BlocksRule material) {
        double rpi = rand.nextDouble() * Math.PI;
        double x1 = x + 8 + Math.sin(rpi) * size / 8.0F;
        double x2 = x + 8 - Math.sin(rpi) * size / 8.0F;
        double z1 = z + 8 + Math.cos(rpi) * size / 8.0F;
        double z2 = z + 8 - Math.cos(rpi) * size / 8.0F;
        double y1 = y + rand.nextInt(3) + 2;
        double y2 = y + rand.nextInt(3) + 2;
        for (int i = 0; i <= size; i++) {
            double xPos = x1 + (x2 - x1) * i / size;
            double yPos = y1 + (y2 - y1) * i / size;
            double zPos = z1 + (z2 - z1) * i / size;
            double fuzz = rand.nextDouble() * size / 16.0D;
            double fuzzXZ = (Math.sin((float) (i * Math.PI / size)) + 1.0F) * fuzz + 1.0D;
            double fuzzY = (Math.sin((float) (i * Math.PI / size)) + 1.0F) * fuzz + 1.0D;
            int xStart = (int)Math.floor(xPos - fuzzXZ / 2.0D);
            int yStart = (int)Math.floor(yPos - fuzzY / 2.0D);
            int zStart = (int)Math.floor(zPos - fuzzXZ / 2.0D);
            int xEnd = (int)Math.floor(xPos + fuzzXZ / 2.0D);
            int yEnd = (int)Math.floor(yPos + fuzzY / 2.0D);
            int zEnd = (int)Math.floor(zPos + fuzzXZ / 2.0D);
            for (int ix = xStart; ix <= xEnd; ix++) {
                double xThresh = (ix + 0.5D - xPos) / (fuzzXZ / 2.0D);
                if (xThresh * xThresh < 1.0D) {
                    for (int iy = yStart; iy <= yEnd; iy++) {
                        double yThresh = (iy + 0.5D - yPos) / (fuzzY / 2.0D);
                        if (xThresh * xThresh + yThresh * yThresh < 1.0D) {
                            for (int iz = zStart; iz <= zEnd; iz++) {
                                double zThresh = (iz + 0.5D - zPos) / (fuzzXZ / 2.0D);
                                if (xThresh * xThresh + yThresh * yThresh + zThresh * zThresh < 1.0D) {
                                    Block block = tryGetBlock(world, ix, iy, iz);
                                    if (block != null && block.getType() == Material.STONE) {
                                        block.setType(material.id);
                                        block.setData((byte) material.data);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Block tryGetBlock(World world, int x, int y, int z) {
        int cx = x >> 4;
        int cz = z >> 4;

        if ((!world.isChunkLoaded(cx, cz)) &&
                (!world.loadChunk(cx, cz, false))) {
            return null;
        }

        Chunk chunk = world.getChunkAt(cx, cz);
        if (chunk == null) {
            return null;
        }
        return chunk.getBlock(x & 0xF, y, z & 0xF);
    }
}
