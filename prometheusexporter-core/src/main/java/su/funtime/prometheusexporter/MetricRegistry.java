package su.funtime.prometheusexporter;

import org.jetbrains.annotations.NotNull;
import su.funtime.prometheusexporter.api.IMetric;
import su.funtime.prometheusexporter.api.IProjectMetricRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MetricRegistry implements IProjectMetricRegistry {

    private static final MetricRegistry INSTANCE = new MetricRegistry();
    
    private final List<IMetric> metrics = new ArrayList<>();

    private MetricRegistry() {
        
    }
    
    public static MetricRegistry getInstance() {
        return INSTANCE;
    }
    
    public void register(IMetric metric) {
        this.metrics.add(metric);
    }

    CompletableFuture<Void> collectMetrics() {
        /* Combine all Completable futures into a single one */
        return CompletableFuture.allOf(this.metrics.stream()
                .map(IMetric::collect)
                .filter(Objects::nonNull)
                .toArray(CompletableFuture[]::new));
    }


    @Override
    public void registerMetric(@NotNull IMetric metric) {
        this.metrics.add(metric);
    }
}
