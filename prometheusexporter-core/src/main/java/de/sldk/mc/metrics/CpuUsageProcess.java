package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsageProcess extends Metric {

    private static final Gauge CPU_USAGE_PROCESS = Gauge.build()
            .name(prefix("cpu_usage_process"))
            .help("CPU usage of process percentage (by number of cores)")
            .create();

    public CpuUsageProcess(Plugin plugin) {
        super(plugin, CPU_USAGE_PROCESS);
    }

    private double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;

            double CpuLoad = sunOsBean.getProcessCpuLoad();

            int availableProcessors = Runtime.getRuntime().availableProcessors();

            return CpuLoad * 100 * availableProcessors;
        } else {
            return 0.0;
        }
    }

    @Override
    protected void doCollect() {
        CPU_USAGE_PROCESS.set(getCpuUsage());
    }

    @Override
    public boolean isFoliaCapable() {
        return true;
    }

    @Override
    public boolean isAsyncCapable() {
        return true;
    }
}
