package su.funtime.prometheusexporter.metrics;


import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import su.funtime.prometheusexporter.MetricRegistry;
import su.funtime.prometheusexporter.api.IProjectMetrics;

import java.util.function.Supplier;

import static su.funtime.prometheusexporter.metrics.Metric.prefix;

public class RegisterMetrics implements IProjectMetrics {

    @Override
    public Gauge gaugeBuilder(String name, String help) {

        return Gauge.build()
                .name(prefix(name))
                .help(help)
                .create();
    }

    @Override
    public void collectMetric(Plugin plugin, Gauge gauge, Supplier<Double> supplier, boolean isAsync) {
        RegisterGaugeMetric metric = new RegisterGaugeMetric(plugin, gauge, supplier, isAsync);
        metric.enable();
        MetricRegistry.getInstance().register(metric);
    }

    @Override
    public void unregisterMetric(Gauge gauge) {
        CollectorRegistry.defaultRegistry.unregister(gauge);
    }

}
