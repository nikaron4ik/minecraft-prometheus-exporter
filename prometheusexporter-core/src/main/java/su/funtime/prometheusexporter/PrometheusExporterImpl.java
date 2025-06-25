package su.funtime.prometheusexporter;

import lombok.Getter;
import su.funtime.prometheusexporter.api.ProjectController;
import su.funtime.prometheusexporter.config.PrometheusExporterConfig;
import su.funtime.prometheusexporter.health.ConcurrentHealthChecks;
import su.funtime.prometheusexporter.health.HealthChecks;


import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.funtime.prometheusexporter.metrics.RegisterMetricsImpl;

public final class PrometheusExporterImpl extends JavaPlugin implements ProjectController {
    @NotNull
    private final PrometheusExporterConfig config = new PrometheusExporterConfig(this);
    @Nullable
    private MetricsServer metricsServer;

    @Getter
    private RegisterMetricsImpl registerMetrics;


    @Override
    public void onEnable() {
        registerMetrics = new RegisterMetricsImpl();

        PrometheusApi.controller = this;

        this.config.loadDefaultsAndSave();
        this.config.enableConfiguredMetrics();
        HealthChecks healthChecks = ConcurrentHealthChecks.create();
        this.getServer().getServicesManager().register(HealthChecks.class, healthChecks, this, ServicePriority.Normal);
        Objects.requireNonNull(healthChecks);
        this.startMetricsServer(healthChecks);
    }

    private void startMetricsServer(HealthChecks healthChecks) {
        String host = this.config.get(PrometheusExporterConfig.HOST);
        Integer port = this.config.get(PrometheusExporterConfig.PORT);
        Objects.requireNonNull(port);
        this.metricsServer = new MetricsServer(host, port, this, healthChecks);

        try {
            this.metricsServer.start();
            this.getLogger().info("Started Prometheus metrics endpoint at: " + host + ':' + port);
        } catch (Exception var5) {
            this.getLogger().severe("Could not start embedded Jetty server: " + var5.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        try {
            MetricsServer server = this.metricsServer;
            if (server != null) server.stop();
        } catch (Exception var2) {
            this.getLogger().log(Level.WARNING, "Failed to stop metrics server gracefully: " + var2.getMessage());
            this.getLogger().log(Level.FINE, "Failed to stop metrics server gracefully", var2);
        }
    }
}
