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
        int chance = 50;
        if (Math.abs(source.getX()) + Math.abs(source.getZ()) < 20)
            chance = 25;
        if (random.nextInt(chance) != 0)
            return;
        int x = source.getX() * 16 + random.nextInt(10) + 4;
        int z = source.getZ() * 16 + random.nextInt(10) + 5;
        Block block0 = world.getHighestBlockAt(x, z);
        Material type = block0.getRelative(BlockFace.DOWN).getType();
        if (type == Material.WATER || type == Material.LEAVES)
            return;
        if (block0.getRelative(0, -2, 0).getType() == Material.AIR)
            return;

        Block block = block0.getRelative(0, 0, -2);
        for (int y = 0; y < 3; y++) {
            for (int i = -1; i <= 1; i++) {
                block.getRelative(i, y, -2).setType(Material.WOOD);

                block.getRelative(i, y, +2).setType(i == 0 && y < 2 ? Material.AIR : Material.WOOD);
                block.getRelative(i, y, +3).setType(Material.AIR);
                block.getRelative(+2, y, i).setType(Material.WOOD);
                block.getRelative(-2, y, i).setType(Material.WOOD);
            }
            for (int xi = -2; xi <= 2; xi += 4) {
                for (int zi = -2; zi <= 2; zi += 4) {
                    block.getRelative(xi, y, zi).setType(Material.LOG);
                }
            }
        }
        for (int xi = -2; xi <= 2; xi++) {
            for (int zi = -2; zi <= 2; zi++) {
                block.getRelative(xi, 3, zi).setType(Material.LOG);
                block.getRelative(xi, -1, zi).setType(Material.WOOD);
            }
        }

        for (int y = 0; y < 3; y++) {
            for (int zi = -1; zi <= 1; zi++) {
                Block blockW = block.getRelative(-1, y, zi);
                blockW.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 5, true);
                Sign signW = (Sign) blockW.getState();
                signW.setLine(0, "§1(Barter)");
                signW.update();

                Block blockE = block.getRelative(+1, y, zi);
                blockE.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 4, true);
                Sign signE = (Sign) blockE.getState();
                signE.setLine(0, "§1(Barter)");
                signE.update();
            }
        }
        Block blockE = block.getRelative(0, +1, -1);
        blockE.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
        Sign signE = (Sign) blockE.getState();
        signE.setLine(0, "§1(Training)");
        signE.update();

        Block blockF = block.getRelative(0, +2, -1);
        blockF.setTypeIdAndData(Material.TORCH.getId(), (byte) 3, true);
    }
}

