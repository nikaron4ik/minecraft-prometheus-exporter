package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsageOS_100 extends MetricCollector {

    public CpuUsageOS_100(Plugin plugin) {
        super(plugin,
                "cpu_usage_os_100",
                "CPU usage of OS percentage",
                true
        );
    }

    @Override
    public double collect() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            return sunOsBean.getSystemCpuLoad() * 100;
        } else {
            return 0.0;
        }
    }
}
