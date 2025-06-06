package su.funtime.prometheusexporter.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public abstract class MetricCollector implements Supplier<Double> {
    private final @NonNull Plugin plugin;
    private final @NonNull String metricName;
    private final @NonNull String metricDescription;
    private final boolean isAsyncCapable;

    public abstract double collect();

    @Override
    public final Double get() {
        return this.collect();
    }
}
