package su.funtime.prometheusexporter.metrics;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import su.funtime.prometheusexporter.api.IProjectRegisterMetrics;

import static su.funtime.prometheusexporter.metrics.Metric.prefix;

public class RegisterMetrics implements IProjectRegisterMetrics {

    @Override
    public Gauge registerGauge(String name, String help) {

        return Gauge.build()
                .name(prefix(name))
                .help(help)
                .create();
    }

    @Override
    public Counter registerCounter(String name, String help) {

        return Counter.build()
                .name(prefix(name))
                .help(help)
                .create();
    }
}
