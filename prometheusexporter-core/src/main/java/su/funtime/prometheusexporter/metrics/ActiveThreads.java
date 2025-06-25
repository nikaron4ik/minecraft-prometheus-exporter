package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ActiveThreads extends MetricCollector {


    public ActiveThreads(Plugin plugin) {
        super(plugin,
                "active_threads",
                "Number of active threads",
                false
        );
    }

    @Override
    public double collect() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return threadMXBean.getThreadCount();
    }

}
