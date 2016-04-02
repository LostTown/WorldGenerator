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
    BEACHES(+3, none(), SAND),
    BIRCH_FOREST_HILLS(Hills, sel(BIRCH, 10), GRASS),
    BIRCH_FOREST(Plains, sel(BIRCH, 10, TALL_BIRCH, 10), GRASS),
    COLD_BEACH(+3, none(), SAND),
    DEEP_OCEAN(-Hills, none(), SAND),
    DESERT(Plains, sel(TREE, 1), SAND),
    DESERT_HILLS(Hills, sel(TREE, 1), SAND),
    EXTREME_HILLS(Mountain, sel(TREE, 8, BIG_TREE, 5), GRASS),
    EXTREME_HILLS_WITH_TREES(+64, sel(TREE, 3), GRASS),
    FOREST(Plains, sel(REDWOOD, 6, BIG_TREE, 6, TREE), GRASS),
    FOREST_HILLS(Hills, sel(TREE, 12, BIG_TREE, 5), GRASS),
    FROZEN_OCEAN(-8, none(), ICE),
    FROZEN_RIVER(-2, none(), ICE),
    HELL(Plains, none(), GRASS),
    ICE_MOUNTAINS(Mountain, sel(TREE, 3), PACKED_ICE),
    ICE_FLATS(Plains, sel(TREE, 1), PACKED_ICE),
    JUNGLE_EDGE(Plains, sel(JUNGLE_BUSH), GRASS),
    JUNGLE_HILLS(Hills, sel(JUNGLE_BUSH, 6, SMALL_JUNGLE, 5), GRASS),
    JUNGLE(Plains, sel(JUNGLE_BUSH, 6, SMALL_JUNGLE, 6, TreeType.JUNGLE), GRASS),
    MESA(Plains, sel(TREE, 1), GRASS),
    MESA_CLEAR_ROCK(Plains, sel(TREE, 1), STONE),
    MESA_ROCK(Plains, sel(TREE, 1), GRASS),
    MUSHROOM_ISLAND(Plains, sel(TreeType.BROWN_MUSHROOM, 5, TreeType.RED_MUSHROOM, 5), GRASS),
    MUSHROOM_ISLAND_SHORE(+3, none(), SAND),
    OCEAN(-8, none(), SAND),
    PLAINS(Plains, sel(TREE, 1), GRASS),
    REDWOOD_TAIGA(Plains, sel(REDWOOD, 8, TALL_REDWOOD, 5, MEGA_REDWOOD), GRASS),
    REDWOOD_TAIGA_HILLS(Hills, sel(REDWOOD, 8, TALL_REDWOOD, 5, MEGA_REDWOOD), GRASS),
    RIVER(-2, none(), GRASS),
    ROOFED_FOREST(Hills, sel(REDWOOD, 10, BIG_TREE, 10, TREE), GRASS),
    SAVANNA(Plains, sel(TREE, 1), GRASS),
    SAVANNA_ROCK(Plains, sel(TREE, 1), STONE),
    SKY(Plains, none(), GRASS),
    SMALLER_EXTREME_HILLS(Mountain, sel(TREE, 8, BIG_TREE, 5), GRASS),
    STONE_BEACH(+3, none(), STONE),
    SWAMPLAND(Plains, sel(SWAMP), SAND),
    TAIGA_COLD(Plains, sel(ACACIA, 8, BIG_TREE, 5), GRASS),
    TAIGA_COLD_HILLS(Hills, sel(ACACIA, 8, BIG_TREE, 5), GRASS),
    TAIGA_HILLS(Hills, sel(ACACIA, 8, BIG_TREE, 5), GRASS),
    TAIGA(Plains, sel(ACACIA, 8, BIRCH, 5), GRASS),
    VOID(Plains, sel(DARK_OAK, 1), BEDROCK);

    static final Map<Biome, LostBiome> biomeMap = new EnumMap<Biome, LostBiome>(Biome.class);

    static {
        for (Biome biome : Biome.values()) {
            String name = biome.name().replace("MUTATED_", "");
            biomeMap.put(biome, LostBiome.valueOf(name));
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
