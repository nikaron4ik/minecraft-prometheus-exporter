package su.funtime.prometheusexporter.config;

import su.funtime.prometheusexporter.api.MetricCollector;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class MetricConfig extends PluginConfig<Boolean> {

    private static final String CONFIG_PATH_PREFIX = "enable_metrics";

    private Function<Plugin, MetricCollector> metricInitializer;

    protected MetricConfig(String key, Boolean defaultValue, Function<Plugin, MetricCollector> metricInitializer) {
        super(CONFIG_PATH_PREFIX + "." + key, defaultValue);
        this.metricInitializer = metricInitializer;
    }

    public MetricCollector getMetric(Plugin plugin) {
        return metricInitializer.apply(plugin);
    }
}
