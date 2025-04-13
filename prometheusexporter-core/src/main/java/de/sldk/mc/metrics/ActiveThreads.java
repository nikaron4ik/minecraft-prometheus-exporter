package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ActiveThreads extends Metric {

    private static final Gauge ACTIVE_THREADS = Gauge.build()
            .name(prefix("active_threads"))
            .help("Number of active threads")
            .create();

    public ActiveThreads(Plugin plugin) {
        super(plugin, ACTIVE_THREADS);
    }

    private int getActiveThreadsCount() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return threadMXBean.getThreadCount();
    }

    @Override
    public void doCollect() {
        ACTIVE_THREADS.set(getActiveThreadsCount());
    }

}
