package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

public class PlayersTotal extends MetricCollector {

    public PlayersTotal(Plugin plugin) {
        super(plugin, "players_total", "Unique players (online + offline)", true);
    }

    @Override
    public double collect() {
        return Bukkit.getOfflinePlayers().length;
    }

}
