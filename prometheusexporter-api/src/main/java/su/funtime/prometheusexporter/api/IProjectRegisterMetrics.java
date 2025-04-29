package su.funtime.prometheusexporter.api;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public interface IProjectRegisterMetrics {

    Gauge registerGauge(String name, String help);

    Counter registerCounter(String name, String help);
}
