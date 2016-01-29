package town.lost.worldgenerator;

import org.bukkit.Material;

/**
 * Created by peter on 24/01/16.
 */
public enum OreGenerators {
    ICE(2, 64, 10, Material.PACKED_ICE),
    LAVA(2, 128, 2, Material.LAVA),
    LAVA2(80, 128, 2, Material.LAVA), // extreme hills
    SAND(2, 255, 10, Material.SAND),
    SANDSTONE(2, 255, 10, Material.SANDSTONE),
    CLAY(64, 80, 5, Material.CLAY),
    DIRT(2, 255, 20, Material.DIRT),
    COAL(2, 64, 10, Material.COAL_ORE),
    COAL2(60, 255, 10, Material.COAL_ORE),
    COAL3(60, 255, 10, Material.COAL_ORE),
    IRON(2, 64, 5, Material.IRON_ORE),
    IRON2(60, 255, 5, Material.IRON_ORE),
    IRON3(80, 255, 10, Material.IRON_ORE), // extreme hills
    EMERALD(5, 31, 0.5, Material.EMERALD_ORE),
    GOLD(5, 80, 0.5, Material.GOLD_ORE),
    LAPIS(5, 40, 0.3, Material.LAPIS_ORE),
    REDSTONE(5, 16, 2, Material.REDSTONE_ORE),
    DIAMOND(5, 16, 0.5, Material.DIAMOND_ORE),
    DIAMOND2(80, 255, 0.5, Material.DIAMOND_ORE), // extreme hills

    GREY_GLASS(60, 255, 50, Material.STAINED_GLASS, 7),
    BLACK_GLASS(60, 255, 50, Material.STAINED_GLASS, 15),
    GRANTIE(60, 255, 50, Material.STONE, 1),
    DIORITE(60, 255, 50, Material.STONE, 3),
    ANDESITE(60, 255, 50, Material.STONE, 5),
    WATER(2, 64, 20, Material.WATER),
    WATER2(64, 128, 10, Material.WATER),
    WATER3(64, 128, 10, Material.WATER),
    GRAVEL(2, 255, 50, Material.GRAVEL),
    COBBLE(2, 255, 50, Material.COBBLESTONE),
    AIR(2, 64, 50, Material.AIR),
    AIR2(64, 255, 50, Material.AIR);

    final int min;
    final int max;
    final double ratio;
    final Material material;
    final int data;

    OreGenerators(int min, int max, double ratio, Material material) {
        this(min, max, ratio, material, 0);
    }

    OreGenerators(int min, int max, double ratio, Material material, int data) {
        this.min = min;
        this.max = max;
        this.ratio = ratio;
        this.material = material;
        this.data = data;
    }
}
