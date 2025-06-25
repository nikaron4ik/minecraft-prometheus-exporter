package su.funtime.prometheusexporter;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.Metric;
import su.funtime.prometheusexporter.metrics.RegisterGaugeLabeledMetric;
import su.funtime.prometheusexporter.metrics.RegisterGaugeMetric;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MetricRegistry {

    private static final MetricRegistry INSTANCE = new MetricRegistry();

    private final Map<Plugin, List<Metric>> pluginMetrics = new HashMap<>();

    private MetricRegistry() {

    }

    public static MetricRegistry getInstance() {
        return INSTANCE;
    }

    public void register(@NonNull Metric metric) {
        pluginMetrics.computeIfAbsent(metric.getPlugin(), p -> new ArrayList<>()).add(metric);
    }

    public void unregister(@NonNull Metric metric) {
        Plugin plugin = metric.getPlugin();
        List<Metric> metrics = pluginMetrics.get(plugin);
        if (metrics != null) {
            metrics.remove(metric);
            if (metrics.isEmpty()) {
                pluginMetrics.remove(plugin);
            }
        }
    }

    public void unregisterAndDisableAllOfPlugin(@NonNull Plugin plugin) {
        List<Metric> metrics = pluginMetrics.get(plugin);
        if (metrics != null) {
            for (Metric metric : metrics) {
                metric.disable();
            }
            pluginMetrics.remove(plugin);
        }
    }

    public @Nullable Metric metricByCollector(@NonNull MetricCollector collector) {
        List<Metric> metrics = pluginMetrics.get(collector.getPlugin());
        if (metrics == null) return null;

        return metrics.stream()
                .filter(metric -> {
                    if (metric instanceof RegisterGaugeMetric gm) {
                        return gm.getMetricCollector() == collector;
                    }
                    else if (metric instanceof RegisterGaugeLabeledMetric glm) {
                        return glm.getMetricCollector() == collector;
                    }
                    return false;
                })
                .findFirst()
                .orElse(null);
    }

    CompletableFuture<Void> collectMetrics() {
        /* Combine all Completable futures into a single one */
        return CompletableFuture.allOf(pluginMetrics.values().stream()
                .flatMap(Collection::stream)
                .map(Metric::collect)
                .filter(Objects::nonNull)
                .toArray(CompletableFuture[]::new));
    }

    /* Возможно, когда-то пригодится
    public @Nullable Metric metricByGauge(Gauge gauge) {
        return pluginMetrics.values().stream()
                .flatMap(Collection::stream)
                .filter(metric -> metric.getCollector() == gauge)
                .findFirst().orElse(null);
    }
     */

}
