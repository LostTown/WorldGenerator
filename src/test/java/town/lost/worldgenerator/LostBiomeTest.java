package town.lost.worldgenerator;

import org.bukkit.block.Biome;
import org.junit.Test;

/**
 * Created by peter on 01/04/16.
 */
public class LostBiomeTest {

    @Test
    public void testOf() throws Exception {
        for (LostBiome biome : LostBiome.values())
            Biome.valueOf(biome.name());
    }
}