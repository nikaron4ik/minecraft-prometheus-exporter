package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.tick_duration.TickDurationCollector;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class TickDurationMedianCollector extends MetricCollector {
    private static final String NAME = "tick_duration_median";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    public TickDurationMedianCollector(Plugin plugin) {
        super(plugin,
                NAME,
                "Median duration of server tick (milliseconds)",
                false);
    }

    @Override
    public double collect() {
        long[] tickTimes = collector.getTickDurations();
        Arrays.sort(tickTimes);
        return tickTimes[tickTimes.length / 2] * 1e-6;
    }
}
