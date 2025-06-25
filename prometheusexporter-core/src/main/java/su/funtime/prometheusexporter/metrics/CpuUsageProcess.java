package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CpuUsageProcess extends MetricCollector {


    public CpuUsageProcess(Plugin plugin) {
        super(plugin,
                "cpu_usage_process",
                "CPU usage of process percentage (by number of cores)",
                true
        );
    }

    @Override
    public double collect() {
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
}
