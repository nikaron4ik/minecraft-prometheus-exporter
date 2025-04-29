package su.funtime.prometheusexporter.api;

import org.jetbrains.annotations.NotNull;

public interface IProjectMetricRegistry {

    void registerMetric(@NotNull IMetric metric);
}
