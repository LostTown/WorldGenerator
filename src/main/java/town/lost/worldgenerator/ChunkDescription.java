package town.lost.worldgenerator;

/**
 * Created by peter on 23/01/16.
 */
public class ChunkDescription {

    public static final int MAX_ZIP = 3;
    final double averageHeight;
    private final XZ XZ;
    private final LostBiome[][] biomes;
    private final short[][] height;
    public boolean generated;
    int averageWater;

    public ChunkDescription(XZ XZ, LostBiome[][] biomes, double averageHeight, short[][] height) {
        this.XZ = XZ;
        this.biomes = biomes;
        this.averageHeight = averageHeight;
        this.height = height;
    }

    public LostBiome getBiome(int x, int z) {
        return biomes[x + 1][z + 1];
    }

    public int height(int x, int z) {
        return height[x][z];
    }

    public void height(int x, int z, int h) {
        if (x < 0 || x > 15 || z < 0 || z > 15)
            return;
        height[x][z] = (short) h;
    }

    private void heightAdj(int x, int z, int shift) {
        if (x < 0 || x > 15 || z < 0 || z > 15)
            return;
        height[x][z] += shift;
    }

    public void mergeWith(ChunkDescription north,
                          ChunkDescription south,
                          ChunkDescription west,
                          ChunkDescription east) {
        // calculate water averages
        calcWaterAverage(north, south, west, east);

//        zipNorth(north);
//        zipSouth(south);
//        zipWest(west);
//        zipEast(east);

    }

    private void zipNorth(ChunkDescription north) {
        if (!north.generated)
            return;
        int nTotal = 0, total = 0;
        for (int x = 0; x < 16; x++) {
            nTotal += north.height(x, 15);
            total += height(x, 0);
        }
        if (total + 16 < nTotal) {
            // raise
            int shift = Math.min(MAX_ZIP, (nTotal - total) / 16);
            for (int z = 0; z < MAX_ZIP; z++) {
                for (int x = 0; x < 16; x++) {
                    heightAdj(x, z, shift);
                }
                if (--shift <= 0)
                    break;
            }
        } else if (total > nTotal) {
            // lower
            int shift = Math.max(-MAX_ZIP, (nTotal - total) / 16);
            for (int z = 0; z < MAX_ZIP; z++) {
                for (int x = 0; x < 16; x++) {
                    heightAdj(x, z, shift);
                }
                if (++shift >= 0)
                    break;
            }
        }
    }

    private void zipSouth(ChunkDescription south) {
        if (!south.generated)
            return;
        int nTotal = 0, total = 0;
        for (int x = 0; x < 16; x++) {
            nTotal += south.height(x, 0);
            total += height(x, 15);
        }
        if (total + 16 < nTotal) {
            // raise
            int shift = Math.min(MAX_ZIP, (nTotal - total) / 16);
            for (int z = 15; z > 15 - MAX_ZIP; z--) {
                for (int x = 0; x < 16; x++) {
                    heightAdj(x, z, shift);
                }
                if (--shift <= 0)
                    break;
            }
        } else if (total > nTotal) {
            // lower
            int shift = Math.max(-MAX_ZIP, (nTotal - total) / 16);
            for (int z = 15; z > 15 - MAX_ZIP; z--) {
                for (int x = 0; x < 16; x++) {
                    heightAdj(x, z, shift);
                }
                if (++shift >= 0)
                    break;
            }
        }
    }

    private void zipWest(ChunkDescription west) {
        if (!west.generated)
            return;
        int nTotal = 0, total = 0;
        for (int z = 0; z < 16; z++) {
            nTotal += west.height(15, z);
            total += height(0, z);
        }
        if (total + 16 < nTotal) {
            // raise
            int shift = Math.min(MAX_ZIP, (nTotal - total) / 16);
            for (int x = 0; x < MAX_ZIP; x++) {
                for (int z = 0; z < 16; z++) {
                    heightAdj(x, z, shift);
                }
                if (--shift <= 0)
                    break;
            }
        } else if (total > nTotal) {
            // lower
            int shift = Math.max(-MAX_ZIP, (nTotal - total) / 16);
            for (int x = 0; x < MAX_ZIP; x++) {
                for (int z = 0; z < 16; z++) {
                    heightAdj(x, z, shift);
                }
                if (++shift >= 0)
                    break;
            }
        }
    }

    private void zipEast(ChunkDescription east) {
        if (!east.generated)
            return;
        int nTotal = 0, total = 0;
        for (int z = 0; z < 16; z++) {
            nTotal += east.height(0, z);
            total += height(15, z);
        }
        if (total + 16 < nTotal) {
            // raise
            int shift = Math.min(MAX_ZIP, (nTotal - total) / 16);
            for (int x = 15; x > 15 - MAX_ZIP; x--) {
                for (int z = 0; z < 16; z++) {
                    heightAdj(x, z, shift);
                }
                if (--shift <= 0)
                    break;
            }
        } else if (total > nTotal) {
            // lower
            int shift = Math.max(-MAX_ZIP, (nTotal - total) / 16);
            for (int x = 15; x > 15 - MAX_ZIP; x--) {
                for (int z = 0; z < 16; z++) {
                    heightAdj(x, z, shift);
                }
                if (++shift >= 0)
                    break;
            }
        }
    }

    private void calcWaterAverage(ChunkDescription... nsew) {
        if (isAnyOcean()) {
            averageWater = 0;
            return;
        }
        int minWater = 128;
        for (ChunkDescription cd : nsew) {
            if (cd.generated)
                minWater = Math.min(minWater, cd.averageWater);
        }
        averageWater = minWater == 128 ? 0 : minWater;
        if ((int) averageHeight > averageWater)
            averageWater++;
//        System.out.println("min: " + minWater + ", w: " + averageWater + " h:" + averageHeight);
    }

    private boolean isAnyOcean() {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                LostBiome lostBiome = biomes[x][z];
                switch (lostBiome) {
                    case OCEAN:
                    case FROZEN_OCEAN:
                    case DEEP_OCEAN:
                        return true;
                }
            }
        }
        return false;
    }

/*
    public void mergeWith(Random random, ChunkGenerator.BiomeGrid biomeGrid, ChunkDescription... descs) {
        double totalGround = 0;
        int groundCount = 0;
        List<String> neighbours = new ArrayList<>();
        double minWater = 255, maxWater = 1;
        for (ChunkDescription desc : descs) {
            if (desc == null)
                continue;
            double aw = desc.averageWater;
            if (minWater > aw)
                minWater = aw;
            if (maxWater < aw)
                maxWater = aw;
            totalGround += desc.averageGround;
            groundCount++;
            neighbours.add("w: " + desc.averageWater + ", g: " + desc.averageGround);
        }

        List<Biome> biomes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            Biome biome = biomeGrid.getBiome(x, z);
            biomes.add(biome);
            LostBiome lostBiome = LostBiome.of(biome);
            long height = lostBiome.height;
            if (height <= 0) {
                switch (biome) {
                    case OCEAN:
                    case DEEP_OCEAN:
                        maxWater = minWater = 0;
                        break;
                    case RIVER:
                        totalGround--;
                        break;
                }
            }
            totalGround += height;
            groundCount++;
        }

        averageGround = groundCount == 0 ? 0 : Maths.round2(totalGround / groundCount);
        averageWater = Math.min(minWater + 1, (minWater + maxWater) / 2);
        if (averageGround > averageWater + 0.5)
            averageWater += 0.5;
        else if (averageGround < averageWater) {
            if (averageGround < 0)
                averageWater = 0;
            else
                averageWater = averageGround;
        }
        averageWater = Maths.round2(averageWater);
        System.out.println(XZ + " avgGround: " + averageGround + ", avgWater: " + averageWater + " - " + neighbours + ", biomes: " + biomes);
    }
*/

}
