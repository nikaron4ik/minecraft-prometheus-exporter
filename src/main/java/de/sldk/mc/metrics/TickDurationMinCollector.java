package de.sldk.mc.metrics;

import de.sldk.mc.metrics.tick_duration.TickDurationCollector;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class TickDurationMinCollector extends Metric {
    private static final String NAME = "tick_duration_min";
    private final TickDurationCollector collector = TickDurationCollector.forServerImplementation(this.getPlugin());

    private static final Gauge TD = Gauge.build()
            .name(prefix(NAME))
            .help("Min duration of server tick (nanoseconds)")
            .create();

    public TickDurationMinCollector(Plugin plugin) {
        super(plugin, TD);
    }

    private double getTickDurationMin() {
        long min = Long.MAX_VALUE;
        for (Long val : collector.getTickDurations()) {
            if (val < min) {
                min = val;
            }
        }
        return min * 1e-6;
    }

    @Override
    public void doCollect() {
        TD.set(getTickDurationMin());
    }
}

