package su.funtime.prometheusexporter.api;

import io.prometheus.client.Gauge;

import java.util.function.Supplier;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;

/**
 * Интерфейс для регистрации и сбора метрик через Prometheus
 */
public interface MetricRegistration {
    /**
     *
     * @param plugin Плагин, в котором собирается метрика
     * @param name Название метрики (уникальное) - строчными английскими буквами, без префикса "mc_"
     * @param help Описание метрики (при написании запросов в веб-панели)
     * @param supplier Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе
     * @param isAsyncCapable false для сбора в основном потоке сервера, true для сбора через другие потоки (ForkJoinPool)
     * @return {@link Gauge}, который сразу собирается и отображается в Prometheus
     */
    Gauge registerMetric(@NonNull Plugin plugin, @NonNull String name, @NonNull String help, @NonNull Supplier<Double> supplier, boolean isAsyncCapable);

    /**
     *
     * @param collector Объект, описывающих характеристики сбора метрик и сам процесс их сбора
     * @return {@link Gauge}, который сразу собирается и отображается в Prometheus
     */
    Gauge registerMetric(@NonNull MetricCollector collector);

    /**
     * Снимает регистрацию Gauge-метрики.
     * @param gauge-метрика, регистрацию которой требуется снять.
     */
    void unregisterMetric(@NonNull Gauge gauge);
}
