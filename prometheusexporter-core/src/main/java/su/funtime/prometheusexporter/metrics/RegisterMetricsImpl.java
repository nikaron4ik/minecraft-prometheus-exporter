package su.funtime.prometheusexporter.metrics;


import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

import su.funtime.prometheusexporter.MetricRegistry;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.api.MetricRegistration;

import java.util.function.Supplier;

import static su.funtime.prometheusexporter.metrics.Metric.prefix;

public class RegisterMetricsImpl implements MetricRegistration {
    @Override
    public Gauge registerMetric(@NonNull Plugin plugin, @NonNull String name, @NonNull String help, @NonNull Supplier<Double> supplier, boolean isAsyncCapable) {
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
    public Gauge registerMetric(@NonNull MetricCollector collector) {
        return this.registerMetric(
                collector.getPlugin(),
                collector.getMetricName(),
                collector.getMetricDescription(),
                collector,
                collector.isAsyncCapable()
        );
    }

    @Override
    public void unregisterMetric(@NonNull Gauge gauge) {
        Metric metric = MetricRegistry.getInstance().metricByGauge(gauge);
        if (metric != null) {
            // Разрегистрация и отключения Gauge-сборщика
            metric.disable();
            // Удаление экземпляра метрики непосредственно из HashMap плагина
            MetricRegistry.getInstance().unregister(metric);
        } else {
            // В случае отсутствия экземпляра метрики попросту снимается регистрация Gauge-сборщика
            CollectorRegistry.defaultRegistry.unregister(gauge);
        }

    }

}
