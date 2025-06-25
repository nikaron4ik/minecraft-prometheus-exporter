package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.tick_duration.TickDurationCollector;
import org.bukkit.plugin.Plugin;

public class TickDurationMinCollector extends MetricCollector {
    private static final String NAME = "tick_duration_min";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    public TickDurationMinCollector(Plugin plugin) {
        super(plugin,
                NAME,
                "Min duration of server tick (milliseconds)",
                false);
    }


    @Override
    public double collect() {
        long min = Long.MAX_VALUE;
        for (Long val : collector.getTickDurations()) {
            if (val < min) {
                min = val;
            }
        }
        return min * 1e-6;
    }
}

