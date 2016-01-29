package town.lost.worldgenerator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by peter on 22/01/16.
 */
public class SignPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        int x = source.getX() * 16 + random.nextInt(14) + 1;
        int z = source.getZ() * 16 + random.nextInt(14) + 1;
        Block block = world.getHighestBlockAt(x, z);
        Block block1 = block.getRelative(BlockFace.DOWN);
        switch (block1.getType()) {
            case GRASS:
            case DIRT:
                block.setType(Material.SIGN_POST);
                Sign sign = (Sign) block.getState();
                sign.setLine(0, "(Barter)");
                sign.update();
                break;
        }
    }
}

