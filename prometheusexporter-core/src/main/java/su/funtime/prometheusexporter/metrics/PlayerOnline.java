package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerOnline extends MetricCollector {


    public PlayerOnline(Plugin plugin) {
        super(plugin, "player_online", "Online state by player name", false);
    }

    @Override
    public List<String> getLabelNames() {
        return List.of("name", "uid");
    }

    @Override
    public Map<List<String>, Double> collectWithLabels() {
        Map<List<String>, Double> map = new HashMap<>();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            String uid = player.getUniqueId().toString();
            String name = player.getName() != null ? player.getName() : player.getUniqueId().toString();
            double online = player.isOnline() ? 1.0 : 0.0;

            map.put(List.of(name, uid), online);
        }

        return map;
    }
}
