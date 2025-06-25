package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.tick_duration.TickDurationCollector;
import org.bukkit.plugin.Plugin;

public class TickDurationAverageCollector extends MetricCollector {
    private static final String NAME = "tick_duration_average";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    public TickDurationAverageCollector(Plugin plugin) {
        super(plugin,
                NAME,
                "Average duration of server tick (milliseconds)",
                false);
    }

    @Override
    public double collect() {
        long sum = 0;
        long[] durations = collector.getTickDurations();
        for (long val : durations) {
            sum += val;
        }
        double averageInNanoSeconds = (double) sum / durations.length;
        return averageInNanoSeconds * 1e-6; // Конвертация из миллисекунд в миллисекунды
    }
}
