package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersOnlineTotal extends MetricCollector {

    public PlayersOnlineTotal(Plugin plugin) {
        super(plugin, "players_online_total", "Players currently online per world", true);
    }

    @Override
    public List<String> getLabelNames() {
        return List.of("world");
    }

    @Override
    public Map<List<String>, Double> collectWithLabels() {
        Map<List<String>, Double> map = new HashMap<>();

        for (World world : Bukkit.getWorlds()) {
            map.put(List.of(world.getName()), (double) world.getPlayers().size());
        }

        return map;
    }
}
