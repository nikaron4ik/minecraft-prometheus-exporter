package de.sldk.mc.metrics;

import de.sldk.mc.metrics.tick_duration.TickDurationCollector;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class TickDurationAverageCollector extends Metric {
    private static final String NAME = "tick_duration_average";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    private static final Gauge TD = Gauge.build()
            .name(Metric.prefix(NAME))
            .help("Average duration of server tick (milliseconds)")
            .create();

    public TickDurationAverageCollector(Plugin plugin) {
        super(plugin, TD);
    }

    private double getTickDurationAverage() {
        long sum = 0;
        long[] durations = collector.getTickDurations();
        for (long val : durations) {
            sum += val;
        }
        double averageInNanoSeconds = (double) sum / durations.length;
        return averageInNanoSeconds * 1e-6; // Конвертация из наносекунд в миллисекунды
    }


    @Override
    public void doCollect() {
        TD.set(getTickDurationAverage());
    }
}
