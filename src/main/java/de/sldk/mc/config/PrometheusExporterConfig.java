package de.sldk.mc.config;

import de.sldk.mc.MetricRegistry;
import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.metrics.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PrometheusExporterConfig {

    public static final PluginConfig<String> HOST = new PluginConfig<>("host", "localhost");
    public static final PluginConfig<Integer> PORT = new PluginConfig<>("port", 9940);
    public static final List<MetricConfig> METRICS = Arrays.asList(
            metricConfig("entities_total", true, Entities::new),
            metricConfig("villagers_total", true, Villagers::new),
            metricConfig("loaded_chunks_total", true, LoadedChunks::new),
            metricConfig("jvm_memory", true, Memory::new),
            metricConfig("players_online_total", true, PlayersOnlineTotal::new),
            metricConfig("players_total", true, PlayersTotal::new),
            metricConfig("whitelisted_players", false, WhitelistedPlayers::new),
            metricConfig("tps", true, Tps::new),
            metricConfig("world_size", true, WorldSize::new),

            // Дополнительно добавленные метрики
            metricConfig("uptime", true, Uptime::new),
            metricConfig("cpu_usage_os", true, CpuUsageOS::new),
            metricConfig("cpu_usage_os_100", true, CpuUsageOS_100::new),
            metricConfig("cpu_usage_process", true, CpuUsageProcess::new),
            metricConfig("cpu_usage_process_100", true, CpuUsageProcess_100::new),
            metricConfig("active_threads", true, ActiveThreads::new),
            metricConfig("network_bytes", true, NetworkTrafficProcess::new),
            metricConfig("console_errors", true, ConsoleErrors::new),
            //

            metricConfig("jvm_threads", true, ThreadsWrapper::new),
            metricConfig("jvm_gc", true, GarbageCollectorWrapper::new),

            metricConfig("tick_duration_median", true, TickDurationMedianCollector::new),
            metricConfig("tick_duration_average", true, TickDurationAverageCollector::new),
            metricConfig("tick_duration_min", false, TickDurationMinCollector::new),
            metricConfig("tick_duration_max", true, TickDurationMaxCollector::new),

            metricConfig("player_online", false, PlayerOnline::new),
            metricConfig("player_statistic", false, PlayerStatistics::new));

    private final PrometheusExporter prometheusExporter;

    public PrometheusExporterConfig(PrometheusExporter prometheusExporter) {
        this.prometheusExporter = prometheusExporter;
    }

    private static MetricConfig metricConfig(String key, boolean defaultValue, Function<Plugin, Metric> metricInitializer) {
        return new MetricConfig(key, defaultValue, metricInitializer);
    }

    public void loadDefaultsAndSave() {
        FileConfiguration configFile = prometheusExporter.getConfig();

        PrometheusExporterConfig.HOST.setDefault(configFile);
        PrometheusExporterConfig.PORT.setDefault(configFile);
        PrometheusExporterConfig.METRICS.forEach(metric -> metric.setDefault(configFile));

        configFile.options().copyDefaults(true);

        prometheusExporter.saveConfig();
    }

    public void enableConfiguredMetrics() {
        PrometheusExporterConfig.METRICS
                .forEach(metricConfig -> {
                    Metric metric = metricConfig.getMetric(prometheusExporter);
                    String metricName = metric.getClass().getSimpleName();
                    try {
                        Boolean enabled = get(metricConfig);

                        var foliaSupported = metric.isFoliaCapable();

                        if (Boolean.TRUE.equals(enabled)) {
                            if (isFolia() && !foliaSupported) {
                                prometheusExporter.getLogger().warning("Metric " + metricName + " is not supported in Folia and will not be enabled");
                                return;
                            }
                            metric.enable();
                        }

                        prometheusExporter.getLogger().fine("Metric " + metricName + " enabled: " + enabled);

                        MetricRegistry.getInstance().register(metric);
                    } catch (Exception e) {
                        prometheusExporter.getLogger().warning("Failed to enable metric " + metricName + ": " + e.getMessage());
                        prometheusExporter.getLogger().log(java.util.logging.Level.FINE, "Failed to enable metric " + metricName, e);
                    }
                });
    }

    public <T> T get(PluginConfig<T> config) {
        return config.get(prometheusExporter.getConfig());
    }

    /**
     * @return true if the server is running Folia
     * @see <a href="https://docs.papermc.io/paper/dev/folia-support">Folia Support</a>
     */
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
