package su.funtime.prometheusexporter.api;

public interface IProjectController {

    IProjectRegisterMetrics getRegisterMetrics();

    IProjectMetricRegistry getMetricRegistry();
}
