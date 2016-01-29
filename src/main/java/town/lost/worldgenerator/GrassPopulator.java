package town.lost.worldgenerator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by peter on 22/01/16.
 */
public class GrassPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        int mx16 = source.getX() * 16;
        int mz16 = source.getZ() * 16;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextInt(4) == 0) {
                    Block highestBlockAt = world.getHighestBlockAt(mx16 + x, mz16 + z);
                    Block below = highestBlockAt.getRelative(0, -1, 0);

                    if (below.getType() == Material.GRASS) {
                        int v = random.nextInt(15);
                        if ((v -= 5) < 8) {
                            highestBlockAt.setType(Material.LONG_GRASS);
                            highestBlockAt.setData((byte) 1);
                        } else if ((v -= 2) < 0) {
                            highestBlockAt.setType(Material.LONG_GRASS);
                            highestBlockAt.setData((byte) 2); //fern
                        } else if ((v -= 3) < 0) {
                            setPlant(highestBlockAt, 2); // tall grass
                        } else if ((v -= 1) < 0) {
                            setPlant(highestBlockAt, 3); // large fern
                        }
                    }
                }
            }
        }
        int type = random.nextInt(6);
        int x = random.nextInt(14) + 1;
        int z = random.nextInt(14) + 1;
        for (int xi = x - 1; xi <= x + 1; xi++) {
            for (int zi = z - 1; zi <= z + 1; zi++) {
                Block highestBlockAt = world.getHighestBlockAt(mx16 + xi, mz16 + zi);
                Block below = highestBlockAt.getRelative(0, -1, 0);
                if (below.getType() == Material.GRASS) {
                    setPlant(highestBlockAt, type);
                }
            }
        }
    }

    private void setPlant(Block highestBlockAt, int type) {
        highestBlockAt.setType(Material.DOUBLE_PLANT);
        highestBlockAt.setData((byte) type);
        Block up = highestBlockAt.getRelative(0, 1, 0);
        up.setType(Material.DOUBLE_PLANT);
        up.setData((byte) (type + 8)); // tall grass
    }
}
