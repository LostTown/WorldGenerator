package town.lost.worldgenerator;

import org.bukkit.Chunk;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by peter on 22/01/16.
 */
public class TreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int t = 0; t < 4; t++) {
            int x = source.getX() * 16 + random.nextInt(14) + 1;
            int z = source.getZ() * 16 + random.nextInt(14) + 1;
            Block block = world.getHighestBlockAt(x, z);
            Block block1 = block.getRelative(0, -1, 0);
            switch (block1.getType()) {
                case GRASS:
                case DIRT:
                    LostBiome lostBiome = LostBiome.of(block1.getBiome());
                    TreeType tt = lostBiome.treeType.apply(random);
                    if (tt != null) {
/*
                        if (tt == BIG_TREE) {
                            Block block2 = block.getRelative(0, 1, 0);
                            block2.setType(Material.LOG);
                            block = block2;
                        }
*/
                        world.generateTree(block.getLocation(), tt);
                    }
                    break;
            }
        }
    }
}
