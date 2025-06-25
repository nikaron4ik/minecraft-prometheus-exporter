package su.funtime.prometheusexporter.api;

import io.prometheus.client.Gauge;


import lombok.NonNull;

/**
 * Интерфейс для регистрации и сбора метрик через Prometheus
 */
public interface MetricRegistration {
    /**
     *
     * @param collector Объект, описывающих характеристики сбора метрик и сам процесс их сбора
     * @return {@link Gauge}, который сразу собирается и отображается в Prometheus
     */
    Gauge registerMetric(@NonNull MetricCollector collector);

    /**
     * Снимает регистрацию Gauge-метрики.
     * @param collector объект метрики, регистрацию которой требуется снять.
     */
    void unregisterMetric(@NonNull MetricCollector collector);
}
