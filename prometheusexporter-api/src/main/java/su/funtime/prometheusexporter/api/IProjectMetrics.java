package su.funtime.prometheusexporter.api;

import io.prometheus.client.Gauge;
import java.util.function.Supplier;
import org.bukkit.plugin.Plugin;

/**
 * Интерфейс для регистрации и сбора метрик через Prometheus
 */
public interface IProjectMetrics {

    /**
     * Создаёт Gauge-метрику без регистрации её значений.
     *
     * @deprecated Рекомендуется использовать {@link #registerMetric}.
     * Оставлен для случаев, когда требуется как-либо изменять Gauge после создания.
     *
     * @param name Уникальное название метрики
     * @param help Описание назначения метрики
     * @return {@link Gauge} (для сбора метрики должен быть зарегистрирован через {@link #collectMetric})
     */
    Gauge gaugeBuilder(String name, String help);


    /**
     * Регистрирует сбор Gauge-метрики
     * @deprecated Рекомендуется использовать {@link #registerMetric}
     * Оставлен для случаев, когда требуется как-либо изменять Gauge после создания.
     *
     * @param plugin Плагин, в котором собирается метрика
     * @param gauge Gauge значение, создаётся при помощи gaugeBuilder
     * @param supplier Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе
     * @param isAsync true для асинхронного сбора метрики, false для синхронного
     */
    void collectMetric(Plugin plugin, Gauge gauge, Supplier<Double> supplier, boolean isAsync);

    /**
     *
     * @param plugin Плагин, в котором собирается метрика
     * @param name Название метрики (уникальное)
     * @param help Описание метрики
     * @param supplier Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе
     * @param isAsync true для асинхронного сбора метрики, false для синхронного\
     * @return {@link Gauge}, который сразу собирается и отображается в Prometheus
     */
    default Gauge registerMetric(Plugin plugin, String name, String help, Supplier<Double> supplier, boolean isAsync) {
        Gauge gauge = gaugeBuilder(name, help);
        collectMetric(plugin, gauge, supplier, isAsync);
        return gauge;
    }

    /**
     * Снимает регистрацию Gauge-метрики.
     * @param gauge-метрика, регистрацию которой требуется снять.
     */
    void unregisterMetric(Gauge gauge);
}
