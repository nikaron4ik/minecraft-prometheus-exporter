package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.tick_duration.TickDurationCollector;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class TickDurationMaxCollector extends MetricCollector {
    private static final String NAME = "tick_duration_max";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    public TickDurationMaxCollector(Plugin plugin) {
        super(plugin,
                NAME,
                "Max duration of server tick (milliseconds)",
                false);
    }

    @Override
    public double collect() {
        long max = Long.MIN_VALUE;
        for (Long val : collector.getTickDurations()) {
            if (val > max) {
                max = val;
            }
        }
        return max * 1e-6;
    }
}

