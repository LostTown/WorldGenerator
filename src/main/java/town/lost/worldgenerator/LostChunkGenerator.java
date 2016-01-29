package town.lost.worldgenerator;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by peter on 22/01/16.
 */
public class LostChunkGenerator extends ChunkGenerator {

    final Map<World, LostWorldGenerator> genMap = new ConcurrentHashMap<>();
    private final BukkitMain bukkitMain;

    public LostChunkGenerator(String world, BukkitMain bukkitMain) {
        this.bukkitMain = bukkitMain;
    }

    public LostWorldGenerator getGenerator(World world) {
        return genMap.computeIfAbsent(world, LostWorldGenerator::new);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.<BlockPopulator>asList(
                new TreePopulator(),
                new GrassPopulator(),
                new SignPopulator());
    }


    @Override
    public synchronized ChunkData generateChunkData(World world, Random random, int mx, int mz, BiomeGrid biomeGrid) {
        ChunkData chunkData = createChunkData(world);
        getGenerator(world).generateChunkData(random, mx, mz, chunkData);
        return chunkData;
    }
}
