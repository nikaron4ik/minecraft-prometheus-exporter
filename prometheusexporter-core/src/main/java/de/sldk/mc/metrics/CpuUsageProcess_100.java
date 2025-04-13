package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsageProcess_100 extends Metric {

    private static final Gauge CPU_USAGE_PROCESS_100 = Gauge.build()
            .name(prefix("cpu_usage_process_100"))
            .help("CPU usage of process percentage")
            .create();

    public CpuUsageProcess_100(Plugin plugin) {
        super(plugin, CPU_USAGE_PROCESS_100);
    }

    private double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            return sunOsBean.getProcessCpuLoad() * 100;
        } else {
            return 0.0;
        }
    }


    @Override
    protected void doCollect() {
        CPU_USAGE_PROCESS_100.set(getCpuUsage());
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
