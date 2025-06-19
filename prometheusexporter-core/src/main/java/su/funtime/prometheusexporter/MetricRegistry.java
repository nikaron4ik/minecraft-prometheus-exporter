package su.funtime.prometheusexporter;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.metrics.Metric;

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

    public void register(Metric metric) {
        pluginMetrics.computeIfAbsent(metric.getPlugin(), p -> new ArrayList<>()).add(metric);
    }

    public void unregister(Metric metric) {
        Plugin plugin = metric.getPlugin();
        List <Metric> metrics = pluginMetrics.get(plugin);
        if (metrics != null) {
            metrics.remove(metric);
            if (metrics.isEmpty()) {
                pluginMetrics.remove(plugin);
            }
        }
    }

    public void unregisterAndDisableAllOfPlugin(Plugin plugin) {
        List <Metric> metrics = pluginMetrics.get(plugin);
        for (Metric metric : metrics) {
            metric.disable();
        }
        pluginMetrics.remove(plugin);
    }

    public Metric metricByGauge(Gauge gauge) {
        return pluginMetrics.values().stream()
                .flatMap(Collection::stream)
                .filter(metric -> metric.getCollector() == gauge)
                .findFirst().orElse(null);
    }

    CompletableFuture<Void> collectMetrics() {
        /* Combine all Completable futures into a single one */
        return CompletableFuture.allOf(pluginMetrics.values().stream()
                .flatMap(Collection::stream)
                .map(Metric::collect)
                .filter(Objects::nonNull)
                .toArray(CompletableFuture[]::new));
    }

}
