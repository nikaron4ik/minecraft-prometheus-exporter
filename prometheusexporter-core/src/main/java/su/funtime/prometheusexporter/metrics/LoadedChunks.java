package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.collectors.LoadedChunksCollector;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadedChunks extends MetricCollector {

    private final LoadedChunksCollector loadedChunksCollector = new LoadedChunksCollector();

    public LoadedChunks(Plugin plugin) {
        super(plugin, "loaded_chunks_total", "Chunks loaded per world", false );
    }

    @Override
    public List<String> getLabelNames() {
        return List.of("world");
    }

    @Override
    public void onRegister() {
        getPlugin().getServer().getPluginManager().registerEvents(loadedChunksCollector, getPlugin());
    }

    @Override
    public void onUnregister() {
        HandlerList.unregisterAll(loadedChunksCollector);
    }

    @Override
    public Map<List<String>, Double> collectWithLabels() {
        Map<List<String>, Double> map = new HashMap<>();

        for (World world : Bukkit.getWorlds()) {
            map.put(List.of(world.getName()), (double) loadedChunksCollector.getLoadedChunkTotal(world.getName()));
        }

        return map;
    }
}
