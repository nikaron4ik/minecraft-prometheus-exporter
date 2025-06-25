package su.funtime.prometheusexporter.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsageProcess_100 extends MetricCollector {


    public CpuUsageProcess_100(Plugin plugin) {
        super(plugin,
                "cpu_usage_process_100",
                "CPU usage of process percentage",
                true);
    }

    @Override
    public double collect() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            return sunOsBean.getProcessCpuLoad() * 100;
        } else {
            return 0.0;
        }
    }
}
