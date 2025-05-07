package su.funtime.prometheusexporter.api;

import io.prometheus.client.Gauge;
import java.util.function.Supplier;
import org.bukkit.plugin.Plugin;

/**
 * Интерфейс для регистрации и сбора метрик через Prometheus
 */
public interface IProjectRegisterMetrics {

    /**
     * Создаёт Gauge-метрику без регистрации её значений.
     * @deprecated Рекомендуется использовать {@link #registerMetric}
     *
     * @param name Уникальное название метрики
     * @param help Описание назначения метрики
     * @return {@link Gauge} (для сбора метрики должен быть зарегистрирован через {@link #collectMetric})
     */
    Gauge gaugeBuilder(String name, String help);


    /**
     * Регистрирует сбор Gauge-метрики
     * @deprecated Рекомендуется использовать {@link #registerMetric}
     *
     * @param plugin Плагин, в котором собирается метрика
     * @param gauge Gauge значение, создаётся при помощи gaugeBuilder
     * @param supplier Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе
     * @param isAsync true для асинхронного сбора метрики, false для синхронного
     */
    void collectMetric(Plugin plugin, Gauge gauge, Supplier<Double> supplier, boolean isAsync);


    /**
     * Создаёт и сразу регистрирует сбор Gauge-метрики <b>(рекомендуемый метод) </b>
     *
     * @param plugin Плагин, в котором собирается метрика
     * @param name Название метрики (уникальное)
     * @param help Описание метрики
     * @param supplier Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе
     * @param isAsync true для асинхронного сбора метрики, false для синхронного
     */
    default void registerMetric(Plugin plugin, String name, String help, Supplier<Double> supplier, boolean isAsync) {
        collectMetric(plugin, gaugeBuilder(name, help), supplier, isAsync);
    }
}
