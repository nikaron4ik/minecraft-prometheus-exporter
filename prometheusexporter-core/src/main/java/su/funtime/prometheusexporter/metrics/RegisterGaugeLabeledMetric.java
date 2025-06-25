package su.funtime.prometheusexporter.metrics;

import io.prometheus.client.Gauge;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.util.List;
import java.util.Map;

public class RegisterGaugeLabeledMetric extends Metric {
    @Getter
    private final MetricCollector metricCollector;
    private final boolean isAsyncCapable;

    public RegisterGaugeLabeledMetric(@NonNull Plugin plugin,
                               @NonNull Gauge gauge,
                               @NonNull MetricCollector collector,
                               boolean isAsyncCapable) {
        super(plugin, gauge);
        this.metricCollector = collector;
        this.isAsyncCapable = isAsyncCapable;
    }

    @Override
    protected void doCollect() {
       Gauge gauge = (Gauge) getCollector();
       Map<List<String>, Double> data = metricCollector.collectWithLabels();

       for (Map.Entry<List<String>, Double> entry : data.entrySet()) {
           List<String> labels = entry.getKey();
           Double value = entry.getValue();

           if (labels.isEmpty() || value == null) continue;

           gauge.labels(labels.toArray(new String[0])).set(value);
       }
    }

    @Override
    public boolean isAsyncCapable() {
        return isAsyncCapable;
    }

}
