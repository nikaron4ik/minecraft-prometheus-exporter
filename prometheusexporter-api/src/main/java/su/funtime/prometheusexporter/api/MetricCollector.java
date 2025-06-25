package su.funtime.prometheusexporter.api;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
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
     * Метод вызывается при регистрации метрики.
     */
    public void onRegister() {} // P.s. (Для ревью) Возможно следует сделать абстрактным?

    /**
     * Вызывается, при снятии регистрации с метрики
     */
    public void onUnregister() {} // P.s. (Для ревью) Возможно следует сделать абстрактным?

    /**
     * Метод, который реализуется для возвращения значения метрики (без лейблов).

     * @return числовое значение метрики
     * @throws UnsupportedOperationException если метод collect используется для получения значения метрики без лейблов
     */
    public double collect() {
        throw new UnsupportedOperationException("Метрика с лейблами поддерживает только collectWithLabels()");
    }

    /**
     * Метод, реализующий логику смотра значений метрик, имеющих лейблы
     * Например, для метрики mc_entities_total есть лейбл type, в котором помечается тип моба,
     * или world, где указывается мир моба
     *
     * @return мапу лейблов с соответствующим метрике с этими лейблами значением
     *  Пример:
     *  для метрики mc_entities_total с лейблами type="pig", world="world" будет значение 1251,
     *  для метрики mc_entites_total с лейблами type="cow", world="world" будет значение 845
     */
    public Map<List<String>, Double> collectWithLabels() {
        return Map.of(List.of(), collect());
    }

    /**
     * Список лейблов метрики.
     *
     * Если возвращается пустой список, считается, что метрика лейблов не имеет.
     */
    public List<String> getLabelNames() {
        return List.of();
    }

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
