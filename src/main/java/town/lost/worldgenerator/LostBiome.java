package town.lost.worldgenerator;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static org.bukkit.Material.*;
import static org.bukkit.TreeType.*;
import static town.lost.worldgenerator.Heights.*;

public enum LostBiome {
    SWAMPLAND(Plains, sel(SWAMP), SAND),
    FOREST(Plains, sel(REDWOOD, 6, BIG_TREE, 6, TREE), GRASS),
    TAIGA(Plains, sel(ACACIA, 8, BIRCH, 5), GRASS),
    DESERT(Plains, sel(TREE, 1), SAND),
    PLAINS(Plains, sel(TREE, 1), GRASS),
    HELL(Plains, none(), GRASS),
    SKY(Plains, none(), GRASS),
    OCEAN(-8, none(), SAND),
    RIVER(-2, none(), GRASS),
    EXTREME_HILLS(Mountain, sel(TREE, 8, BIG_TREE, 5), GRASS),
    FROZEN_OCEAN(-8, none(), ICE),
    FROZEN_RIVER(-2, none(), ICE),
    ICE_PLAINS(Plains, sel(TREE, 1), PACKED_ICE),
    ICE_MOUNTAINS(Plains, sel(TREE, 3), PACKED_ICE),
    MUSHROOM_ISLAND(Plains, sel(TreeType.BROWN_MUSHROOM, 5, TreeType.RED_MUSHROOM, 5), GRASS),
    MUSHROOM_SHORE(+0, none(), SAND),
    BEACH(+2, none(), SAND),
    DESERT_HILLS(Hills, sel(TREE, 1), SAND),
    FOREST_HILLS(Hills, sel(TREE, 12, BIG_TREE, 5), GRASS),
    TAIGA_HILLS(Hills, sel(ACACIA, 8, BIG_TREE, 5), GRASS),
    SMALL_MOUNTAINS(Mountain, sel(TREE, 8, BIG_TREE, 5), GRASS),
    JUNGLE(Plains, sel(JUNGLE_BUSH, 6, SMALL_JUNGLE, 6, TreeType.JUNGLE), GRASS),
    JUNGLE_HILLS(Hills, sel(JUNGLE_BUSH, 6, SMALL_JUNGLE, 5), GRASS),
    JUNGLE_EDGE(Plains, sel(JUNGLE_BUSH), GRASS),
    DEEP_OCEAN(-Hills, none(), SAND),
    STONE_BEACH(+0, none(), STONE),
    COLD_BEACH(+0, none(), SAND),
    BIRCH_FOREST(Plains, sel(BIRCH, 10, TALL_BIRCH, 10), GRASS),
    BIRCH_FOREST_HILLS(Hills, sel(BIRCH, 10), GRASS),
    ROOFED_FOREST(Hills, sel(REDWOOD, 10, BIG_TREE, 10, TREE), GRASS),
    COLD_TAIGA(Plains, sel(ACACIA), GRASS),
    COLD_TAIGA_HILLS(Plains, sel(ACACIA, 5), GRASS),
    MEGA_TAIGA(Plains, sel(ACACIA), GRASS),
    MEGA_TAIGA_HILLS(Plains, sel(ACACIA, 5), GRASS),
    EXTREME_HILLS_PLUS(MountainPlus, sel(TREE, 2), GRASS),
    SAVANNA(Plains, sel(TREE, 1), GRASS),
    SAVANNA_PLATEAU(Plains, sel(TREE, 1), GRASS),
    MESA(Plains, sel(TREE, 1), GRASS),
    MESA_PLATEAU_FOREST(Plains, sel(TREE, 7, BIG_TREE, 5), GRASS),
    MESA_PLATEAU(Plains, sel(TREE, 1), GRASS),
    SUNFLOWER_PLAINS(Plains, none(), GRASS),
    DESERT_MOUNTAINS(Mountain, sel(TREE, 1), GRASS),
    FLOWER_FOREST(Plains, sel(TREE, 7, BIG_TREE, 5), GRASS),
    TAIGA_MOUNTAINS(Mountain, sel(ACACIA, 1), GRASS),
    SWAMPLAND_MOUNTAINS(Mountain, sel(SWAMP), GRASS),
    ICE_PLAINS_SPIKES(Plains, none(), ICE),
    JUNGLE_MOUNTAINS(Mountain, sel(JUNGLE_BUSH), GRASS),
    JUNGLE_EDGE_MOUNTAINS(Mountain, sel(JUNGLE_BUSH), GRASS),
    COLD_TAIGA_MOUNTAINS(Mountain, sel(ACACIA), GRASS),
    SAVANNA_MOUNTAINS(Mountain, sel(TREE, 1), GRASS),
    SAVANNA_PLATEAU_MOUNTAINS(Mountain, sel(TREE, 1), GRASS),
    MESA_BRYCE(Plains, sel(BIRCH, 5), GRASS),
    MESA_PLATEAU_FOREST_MOUNTAINS(Mountain, sel(BIRCH), GRASS),
    MESA_PLATEAU_MOUNTAINS(Mountain, sel(BIRCH, 5), GRASS),
    BIRCH_FOREST_MOUNTAINS(Mountain, sel(BIRCH, 8, TALL_BIRCH, 5), GRASS),
    BIRCH_FOREST_HILLS_MOUNTAINS(Mountain, sel(BIRCH), GRASS),
    ROOFED_FOREST_MOUNTAINS(Mountain, sel(TREE, 7, BIG_TREE, 5), GRASS),
    MEGA_SPRUCE_TAIGA(Plains, sel(ACACIA), GRASS),
    EXTREME_HILLS_MOUNTAINS(MountainPlus, sel(TREE, 3), GRASS),
    EXTREME_HILLS_PLUS_MOUNTAINS(+64, sel(TREE, 3), GRASS),
    MEGA_SPRUCE_TAIGA_HILLS(Hills, sel(TREE, 3), GRASS);

    static final Map<Biome, LostBiome> biomeMap = new EnumMap<Biome, LostBiome>(Biome.class);

    static {
        for (Biome biome : Biome.values()) {
            biomeMap.put(biome, LostBiome.valueOf(biome.name()));
        }
    }

    final int height;
    final Function<Random, TreeType> treeType;
    final Material topMaterial;

    LostBiome(int height,
              Function<Random, TreeType> treeTypeFunction,
              Material topMaterial) {
        this.height = height;
        this.treeType = treeTypeFunction;
        this.topMaterial = topMaterial;
    }

    public static int heightFor(Biome b) {
        return biomeMap.get(b).height;
    }

    public static <T> Function<Random, T> none() {
        return r -> null;
    }

    public static <T> Function<Random, T> sel(T t) {
        return sel(t, 8);
    }

    public static <T> Function<Random, T> sel(T t, int chance) {
        return r -> r.nextInt(20) < chance ? t : null;
    }

    public static <T> Function<Random, T> sel(T t, int chance, T t2, int chance2) {
        return r -> r.nextInt(20) < chance ? t : r.nextInt(20) < chance2 ? t2 : null;
    }

    public static <T> Function<Random, T> sel(T t, int chance, T t2, int chance2, T t3) {
        return r -> r.nextInt(20) < chance ? t : r.nextInt(20) < chance2 ? t2 : t3;
    }

    public static LostBiome of(Biome biome) {
        return biomeMap.get(biome);
    }
}
