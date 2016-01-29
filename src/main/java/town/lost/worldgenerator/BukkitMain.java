package town.lost.worldgenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin implements Listener {

    private static final Logger LOGGER = LogManager.getLogger(BukkitMain.class);

    public static BukkitMain THIS;

    @Override
    public void onEnable() {
        THIS = this;
        log("Enabled");
    }

    @Override
    public void onDisable() {
        log("Disable");
        Bukkit.getScheduler().cancelTasks(this);
        THIS = null;
    }

    public void log(String msg) {
        if (THIS != null) {
            PluginDescriptionFile description = THIS.getDescription();
            msg = "[" + description.getName() + "] " + msg;
        }
        LOGGER.info(msg);
    }

    @Override
    final public ChunkGenerator getDefaultWorldGenerator(final String world, final String id) {
        return new LostChunkGenerator(world, this);
    }


}
