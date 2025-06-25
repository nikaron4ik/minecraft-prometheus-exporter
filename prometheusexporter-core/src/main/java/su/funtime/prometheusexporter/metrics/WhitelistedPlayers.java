package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

public class WhitelistedPlayers extends MetricCollector {

    public WhitelistedPlayers(Plugin plugin) {
        super(plugin, "whitelisted_players", "Players count on the white list", false);
    }

    @Override
    public double collect() {
        return Bukkit.getWhitelistedPlayers().size();
    }

}
