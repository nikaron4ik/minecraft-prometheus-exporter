package su.funtime.prometheusexporter;

import lombok.NonNull;
import su.funtime.prometheusexporter.api.ProjectController;
import su.funtime.prometheusexporter.api.MetricRegistration;

public class PrometheusApi {

    static ProjectController controller = null;

    private PrometheusApi() {
        throw new UnsupportedOperationException("This is a utility class and cannot initialize from your code.");
    }

    @NonNull
    private static ProjectController controller() {
        if (controller == null) {
            throw new IllegalStateException("API not initialized");
        }

        return controller;
    }

    @NonNull
    public static MetricRegistration getRegisterMetrics() {
        return controller().getRegisterMetrics();
    }

}
