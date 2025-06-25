package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

public class Uptime extends MetricCollector {

    private final long serverStartTime;

    public Uptime(Plugin plugin) {
        super(plugin, "uptime", "Server uptime in hours", true);
        this.serverStartTime = System.currentTimeMillis();
    }

    @Override
    public double collect() {
        return (System.currentTimeMillis() - serverStartTime) / 1000.0 / 60.0 / 60.0;
    }
}
