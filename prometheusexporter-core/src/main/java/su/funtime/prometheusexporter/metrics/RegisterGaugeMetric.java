package su.funtime.prometheusexporter.metrics;

import io.prometheus.client.Gauge;;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.util.function.Supplier;

public class RegisterGaugeMetric extends Metric {

    private final Supplier<Double> supplier;
    private final boolean isAsyncCapable;

    public RegisterGaugeMetric(@NonNull Plugin plugin,
                               @NonNull Gauge gauge,
                               @NonNull Supplier<Double> supplier,
                               boolean isAsyncCapable) {
        super(plugin, gauge);
        this.supplier = supplier;
        this.isAsyncCapable = isAsyncCapable;
    }

    @Override
    protected void doCollect() {
        Double val = this.supplier.get();
        if (val == null) {
            return;
        }
        ((Gauge) getCollector()).set(val);
    }

    @Override
    public boolean isAsyncCapable() {
        return isAsyncCapable;
    }

    public MetricCollector getMetricCollector() {
        return supplier instanceof MetricCollector ? (MetricCollector) supplier : null;
    }
}
