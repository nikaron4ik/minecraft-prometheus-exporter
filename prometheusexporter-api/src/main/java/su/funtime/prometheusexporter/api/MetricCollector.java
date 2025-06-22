package su.funtime.prometheusexporter.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

/**
 * Абстрактный класс, использующийся для регистрации метрик в PrometheusExporter.
 *
 *
 */
@RequiredArgsConstructor
@Getter
public abstract class MetricCollector implements Supplier<Double> {
    /**
     * Плагин, в котором собирается метрика
     */
    private final @NonNull Plugin plugin;
    /**
     * Название метрики (уникальное) - строчными английскими буквами, без префикса "mc_"
     */
    private final @NonNull String metricName;
    /**
     * Описание метрики (при написании запросов в веб-панели)
     */
    private final @NonNull String metricDescription;
    /**
     * false для сбора в основном потоке сервера, true для сбора через другие потоки (ForkJoinPool)
     */
    private final boolean isAsyncCapable;

    /**
     * Абстрактный метод, который реализуется для возвращения значения метрики.

     * @return числовое значение метрики
     */
    public abstract double collect();

    /**
     * Используется для вызова метода {@link #collect()}
     *
     * @return текущее значение метрики
     */
    @Override
    public final Double get() {
        return this.collect();
    }
}
