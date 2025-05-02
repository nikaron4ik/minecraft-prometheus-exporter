package su.funtime.prometheusexporter.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.util.function.Supplier;

public class RegisterGaugeMetric extends Metric{

    private final Supplier<Double> supplier;
    private final boolean isAsync;

    public RegisterGaugeMetric(Plugin plugin, Gauge gauge, Supplier<Double> supplier, boolean isAsync) {
        super(plugin, gauge);
        this.supplier = supplier;
        this.isAsync = isAsync;
    }


    @Override
    protected void doCollect() {
        ((Gauge) getCollector()).set(supplier.get());
    }

    @Override
    public boolean isFoliaCapable() {
        return true;
    }

    @Override
    public boolean isAsyncCapable() {
        return isAsync;
    }
}
