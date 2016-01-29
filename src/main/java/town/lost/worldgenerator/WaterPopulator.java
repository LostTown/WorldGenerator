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
public class WaterPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        int count = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                Block block = world.getHighestBlockAt(x, z);
                for (int y = block.getY(); y <= 64; y++) {
                    source.getBlock(x, y, z).setType(Material.WATER);
                    count++;
                }
            }
        }
        System.out.println("Filled " + count + " with water");
    }
}
