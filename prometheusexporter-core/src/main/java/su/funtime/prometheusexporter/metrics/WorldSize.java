package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.utils.PathFileSize;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WorldSize extends MetricCollector {

    private final Logger log;

    public WorldSize(Plugin plugin) {
        super(plugin, "world_size", "World size in bytes", true);
        this.log = plugin.getLogger();
    }

    public List<String> getLabelNames() {
        return List.of("world");
    }

    @Override
    public Map<List<String>, Double> collectWithLabels() {
        Map<List<String>, Double> map = new HashMap<>();

        for (World world : Bukkit.getWorlds()) {
            try {
                PathFileSize pathUtils = new PathFileSize(world.getWorldFolder().toPath());
                long size = pathUtils.getSize();
                map.put(List.of(world.getName()), (double) size);
            } catch (Throwable t) {
                log.throwing(this.getClass().getSimpleName(), "collectWithLabels", t);
            }
        }

        return map;
    }

}
