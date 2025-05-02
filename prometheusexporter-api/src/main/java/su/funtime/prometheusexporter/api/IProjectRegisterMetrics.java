package su.funtime.prometheusexporter.api;

import io.prometheus.client.Gauge;
import java.util.function.Supplier;
import org.bukkit.plugin.Plugin;

public interface IProjectRegisterMetrics {
    /**
     *
     * @param name - Название метрики (автоматически создаётся префиксом mc_)
     * @param help - Строка справки для метрики
     * @return - Gauge значение, которое динамически обновляется при каждом сборе метрики
     */
    Gauge gaugeBuilder(String name, String help);


    /**
     *
     * @param plugin - плагин, в котором собирается метрика
     * @param gauge - Gauge значение (создаётся методом gaugeBuilder)
     * @param supplier - Поставщик значения (метод), возвращающий актуальное значение метрики при каждом сборе (только Double)
     * @param isAsync - определяет, асинхронно (true) или синхронно (false) будет собираться метрика
     */
    void collectMetric(Plugin plugin, Gauge gauge, Supplier<Double> supplier, boolean isAsync);

}
