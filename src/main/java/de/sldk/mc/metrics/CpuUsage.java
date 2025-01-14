package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsage extends Metric {

    private static final Gauge CPU_USAGE = Gauge.build()
            .name(prefix("cpu_usage"))
            .help("CPU usage percentage")
            .create();

    public CpuUsage(Plugin plugin) {
        super(plugin, CPU_USAGE);
    }

    private double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            return sunOsBean.getSystemCpuLoad() * 100;
        } else {
            return 0.0;
        }
    }

    @Override
    protected void doCollect() {
        CPU_USAGE.set(getCpuUsage());
    }
}
