package su.funtime.prometheusexporter;

import lombok.NonNull;
import su.funtime.prometheusexporter.api.IProjectController;
import su.funtime.prometheusexporter.api.IProjectMetricRegistry;
import su.funtime.prometheusexporter.api.IProjectRegisterMetrics;

public class PrometheusApi {

    static IProjectController controller = null;

    private PrometheusApi() {
        throw new UnsupportedOperationException("This is a utility class and cannot initialize from your code.");
    }

    @NonNull
    private static IProjectController controller() {
        if (controller == null) {
            throw new IllegalStateException("API not initialized");
        }

        return controller;
    }

    @NonNull
    public static IProjectRegisterMetrics getRegisterMetrics() {
        return controller().getRegisterMetrics();
    }

    @NonNull
    public static IProjectMetricRegistry getMetricRegistry() {
        return controller().getMetricRegistry();
    }
}
