package su.funtime.prometheusexporter.metrics;


import io.prometheus.client.Gauge;
import lombok.NonNull;

import su.funtime.prometheusexporter.MetricRegistry;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.api.MetricRegistration;

import java.util.List;

import static su.funtime.prometheusexporter.metrics.Metric.prefix;

public class RegisterMetricsImpl implements MetricRegistration {

    @Override
    public Gauge registerMetric(@NonNull MetricCollector collector) {
        List<String> labelNames = collector.getLabelNames();

        Gauge.Builder builder = Gauge.build()
                .name(prefix(collector.getMetricName()))
                .help(collector.getMetricDescription());

        if (!labelNames.isEmpty()) {
            builder.labelNames(labelNames.toArray(new String[0]));
        }
        Gauge gauge = builder.create();

        Metric metric = labelNames.isEmpty()
                ? new RegisterGaugeMetric(collector.getPlugin(), gauge, collector, collector.isAsyncCapable())
                : new RegisterGaugeLabeledMetric(collector.getPlugin(), gauge, collector, collector.isAsyncCapable());

        // вызов хука при регистрации
        collector.onRegister();

        metric.enable();
        MetricRegistry.getInstance().register(metric);

        return gauge;
    }

    @Override
    public void unregisterMetric(@NonNull MetricCollector collector) {
        Metric metric = MetricRegistry.getInstance().metricByCollector(collector);
        if (metric != null) {
            // вызов хука при снятии регистрации
            collector.onUnregister();
            // Разрегистрация и отключения Gauge-сборщика
            metric.disable();
            // Удаление экземпляра метрики непосредственно из HashMap плагина
            MetricRegistry.getInstance().unregister(metric);
        }

    }


    /* Изначальная реализация (вдруг пригодится)
    public Gauge registerMetric(@NonNull Plugin plugin, @NonNull String name, @NonNull String help, @NonNull Supplier<Double> supplier, boolean isAsyncCapable) {
        Gauge.Builder builder = Gauge.build()
                .name(prefix(name))
                .help(help);

        if (!labelNames.isEmpty()) {
            builder.labelNames(labelNames.toArray(new String[0]));
        }
        Gauge gauge = builder.create();

        RegisterGaugeMetric metric = new RegisterGaugeMetric(plugin, gauge, supplier, isAsyncCapable);
        metric.enable();
        MetricRegistry.getInstance().register(metric);

        return gauge;
    }
*/

}
