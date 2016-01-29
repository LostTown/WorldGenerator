package town.lost.worldgenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by peter on 23/01/16.
 */
public class LostWorldGenerator {
    static final int WATER_HEIGHT = 64;

    private final World world;
    private final Map<XZ, ChunkDescription> descriptionMap = new LinkedHashMap<>();
    private final SimplexOctaveGenerator sog8, sog8B, dirtSog;
//    private final SimplexOctaveGenerator skyTop, skyUnder, skyHeight;


    public LostWorldGenerator(World world) {
        this.world = world;
        sog8 = new SimplexOctaveGenerator(world, 8);
        sog8.setScale(1.0 / 64);

        dirtSog = new SimplexOctaveGenerator(world, 8);
        dirtSog.setScale(1.0 / 16);

        sog8B = new SimplexOctaveGenerator(world, 8);
        sog8B.setScale(1.0 / 512);

/*
        skyTop = new SimplexOctaveGenerator(world, 8);
        skyTop.setScale(1.0 / 128);

        skyUnder = new SimplexOctaveGenerator(world, 9);
        skyUnder.setScale(1.0 / 200);

        skyHeight = new SimplexOctaveGenerator(world, 10);
        skyHeight.setScale(1.0 / 256);
*/
    }

    static int getHeight(LostBiome[][] biomes, int x, int z) {
        return biomes[x][z].height;
    }

    static void farm(ChunkGenerator.ChunkData chunkData, int x, int z, int y, int y2, Material crops) {
        chunkData.setBlock(x, y, z, Material.SOIL);
        chunkData.setBlock(x, y2, z, crops);
    }

    static int fillIn(ChunkGenerator.ChunkData chunkData, Random random, int x, int y, int z, int size, Material material, int data) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            Material type = chunkData.getType(x & 15, y, z & 15);
            if (type == Material.STONE) {
                chunkData.setBlock(x, y, z, material.getId(), (byte) data);
                count++;
            }
            x += random.nextInt(3) - 1;
            y += random.nextInt(2) - random.nextInt(2);
            z += random.nextInt(3) - 1;
            if (y < 2 || y > 255)
                break;
        }
        return count;
    }

    static void fill(ChunkGenerator.ChunkData chunkData, int x, int z, int minY, int maxY, Material material) {
        for (int y = minY; y < maxY; y++) {
            chunkData.setBlock(x, y, z, material);
        }
    }

    public ChunkDescription acquireChunkDescription(XZ key) {
        return descriptionMap.computeIfAbsent(key, this::createChunkDescription);
    }

    ChunkDescription createChunkDescription(XZ XZ) {
        int mx16 = XZ.x * 16, mz16 = XZ.z * 16;

        LostBiome[][] biomes = new LostBiome[18][18];
        short[][] height = new short[16][16];
        int totalHeight = 0;

        LostBiome lb2 = LostBiome.BEACH;
        for (int x = 0; x < 18; x++) {
            for (int z = 0; z < 18; z++) {
                Biome b = world.getBiome(mx16 + x - 1, mz16 + z - 1);
                LostBiome lb = LostBiome.of(b);
                biomes[x][z] = lb;
                if (lb != LostBiome.RIVER && lb2.height < lb.height)
                    lb2 = lb;
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double p = sog8B.noise(mx16 + x, mz16 + z, 0.7, 0.7) * 0.3;
                double base = (getHeight(biomes, x, z + 1) + getHeight(biomes, x + 1, z)
                        + getHeight(biomes, x + 1, z + 2) + getHeight(biomes, x + 2, z + 1)
                        + getHeight(biomes, x + 1, z + 1) * 2) / 10.0;
                double v = sog8.noise(mx16 + x, mz16 + z, p, p) * Math.abs(base) + base;
                height[x][z] = (short) v;
                totalHeight += v;
            }
        }
        double averageHeight = totalHeight / 256.0;
        System.out.println("Obtained biomes for " + XZ + " mid " + biomes[8][8] + ",  avg " + averageHeight);
        return new ChunkDescription(XZ, biomes, averageHeight, height);
    }

    public void generateChunkData(Random random, int mx, int mz, ChunkGenerator.ChunkData chunkData) {
        chunkData.setRegion(0, 0, 0, 16, 1, 16, Material.BEDROCK);

        XZ key = new XZ(mx, mz);
        ChunkDescription centre = acquireChunkDescription(key);
        centre.generated = true;
        ChunkDescription north = acquireChunkDescription(key.adjXZ(0, -1));
        ChunkDescription south = acquireChunkDescription(key.adjXZ(0, +1));
        ChunkDescription west = acquireChunkDescription(key.adjXZ(-1, 0));
        ChunkDescription east = acquireChunkDescription(key.adjXZ(+1, 0));
        centre.mergeWith(north, south, west, east);
        System.out.println("... w: " + centre.averageWater + " h:" + centre.averageHeight);

        // generate to height
        generateToHeight(mx * 16, mz * 16, chunkData, centre);

        generateOres(random, chunkData, centre);

        generateSoil(random, chunkData, centre);

//        generateSkyIslands(mx * 16, mz * 16, chunkData, centre);
    }

    private void generateSoil(Random random, ChunkGenerator.ChunkData chunkData, ChunkDescription centre) {
        int type = random.nextInt(100);
        if (type > 4)
            return;
        int x = random.nextInt(16);
        int z = random.nextInt(16);
        for (int y = 60; y < 80; y++) {
            if (chunkData.getType(x, y, z) == Material.GRASS) {
                switch (type) {
                    case 0:
                    case 1:
                    case 2:
                        farm(chunkData, x, z, y, y + 1, Material.CROPS);
                        break;
                    case 3:
                        farm(chunkData, x, z, y, y + 1, Material.POTATO);
                        break;
                    case 4:
                        farm(chunkData, x, z, y, y + 1, Material.CARROT);
                        break;
                }
                return;
            }
        }
    }

    /*
    private void generateSkyIslands(int mx16, int mz16, ChunkGenerator.ChunkData chunkData, ChunkDescription centre) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int top = (int) (skyTop.noise(mx16 + x, mz16 + z, 0.5, 0.5) * 8);
                int under = (int) (skyUnder.noise(mx16 + x, mz16 + z, 0.5, 0.5) * 128) + 128 - 8;
                if (top <= under + 1)
                    continue;
                int height = (int) (skyHeight.noise(mx16 + x, mz16 + z, 0.5, 0.5) * 32 + 128);
                if (top - under > 3) {
                    if (chunkData.getType(x, height + under, z) == Material.AIR)
                        chunkData.setBlock(x, height + under, z, Material.GLOWSTONE);
                }
                for (int y = height + under; y < height + top; y++) {
                    if (chunkData.getType(x, y, z) == Material.AIR)
                        chunkData.setBlock(x, y, z, Material.WOOL);
                }
                if (chunkData.getType(x, height + top, z) == Material.AIR)
                    chunkData.setBlock(x, height + top, z, Material.GRASS);
            }
        }
    }
    */

    private void generateOres(Random random, ChunkGenerator.ChunkData chunkData, ChunkDescription centre) {
        OreGenerators[] values = OreGenerators.values();
        int max = (int) centre.averageHeight + WATER_HEIGHT;
        for (OreGenerators gen : values) {
            int max2 = Math.min(max, gen.max);
            int range = max2 - gen.min + 1;
            if (range < 2) {
                System.out.println(gen + " range " + range);
                continue;
            }
            int x = random.nextInt(16);
            int y = random.nextInt(range) + gen.min;
            int z = random.nextInt(16);
            int bound = random.nextInt((int) (max * gen.ratio) + 1);
            int size = random.nextInt(bound + 1);
            if (size > 0) {
                int count = fillIn(chunkData, random, x, y, z, size, gen.material, gen.data);
                System.out.println(gen + " size " + size + " count: " + count);
            } else {
                System.out.println(gen + " size " + size);
            }
        }

    }

    private void generateToHeight(int mx16, int mz16, ChunkGenerator.ChunkData chunkData, ChunkDescription centre) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                LostBiome lostBiome = centre.getBiome(x, z);
                int h = centre.height(x, z) + WATER_HEIGHT;
                h = Math.min(254, Math.max(2, h));
//                System.out.println("x: " + x + ", z: " + z + " "+lostBiome+" h:" + h);

                Material topMaterial = lostBiome.topMaterial;
                if (topMaterial == Material.GRASS) {
                    int dirt = (int) (dirtSog.noise(mx16 + x, mz16 + z, 0.5, 0.5) * 3 + 3);
                    fill(chunkData, x, z, 1, h - dirt, Material.STONE);
                    fill(chunkData, x, z, h - dirt, h, Material.DIRT);

                } else if (topMaterial == Material.SAND) {
                    int dirt = (int) (dirtSog.noise(mx16 + x, mz16 + z, 0.5, 0.5) * 3 + 3);
                    fill(chunkData, x, z, 1, h - dirt, Material.STONE);
                    fill(chunkData, x, z, h - dirt, h, Material.SANDSTONE);

                } else {
                    fill(chunkData, x, z, 1, h, Material.STONE);
                }
                chunkData.setBlock(x, h, z, topMaterial);
                if (h < WATER_HEIGHT)
                    fill(chunkData, x, z, h, WATER_HEIGHT + 1, Material.WATER);
                if (lostBiome == LostBiome.RIVER) {
                    int averageWater = centre.averageWater;
                    int wh = averageWater + WATER_HEIGHT;
                    if (wh > h)
                        wh = h;
                    chunkData.setBlock(x, wh, z, Material.WATER);
                    fill(chunkData, x, z, wh + 1, wh + 4, Material.AIR);
                }
            }
        }
    }
}
