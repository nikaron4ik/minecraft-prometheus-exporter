package su.funtime.prometheusexporter.metrics;


import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import su.funtime.prometheusexporter.MetricRegistry;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.api.MetricRegistration;

import java.util.function.Supplier;

import static su.funtime.prometheusexporter.metrics.Metric.prefix;

public class RegisterMetricsImpl implements MetricRegistration {
    @Override
    public Gauge registerMetric(Plugin plugin, String name, String help, @NotNull Supplier<Double> supplier, boolean isAsyncCapable) {
        Gauge gauge = Gauge.build()
                .name(prefix(name))
                .help(help)
                .create();

        RegisterGaugeMetric metric = new RegisterGaugeMetric(plugin, gauge, supplier, isAsyncCapable);
        metric.enable();
        MetricRegistry.getInstance().register(metric);

        return gauge;
    }

    @Override
    public Gauge registerMetric(MetricCollector collector) {
        return this.registerMetric(
                collector.getPlugin(),
                collector.getMetricName(),
                collector.getMetricDescription(),
                collector,
                collector.isAsyncCapable()
        );
    }

    @Override
    public void unregisterMetric(Gauge gauge) {
        Metric metric = MetricRegistry.getInstance().metricByGauge(gauge);
        if (metric != null) {
            metric.disable();
            MetricRegistry.getInstance().unregister(metric);
        } else {
            CollectorRegistry.defaultRegistry.unregister(gauge);
        }

    }

}
