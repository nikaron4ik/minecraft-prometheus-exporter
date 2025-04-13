package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class Memory extends Metric {

    private static final Gauge MEMORY_USED_PERCENT = Gauge.build()
            .name(prefix("jvm_memory"))
            .help("JVM used memory in percentage")
            .create();

    public Memory(Plugin plugin) {
        super(plugin, MEMORY_USED_PERCENT);
    }

    @Override
    public void doCollect() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        double usedMemoryPercent = ((double) usedMemory / maxMemory) * 100.0;
        MEMORY_USED_PERCENT.set(usedMemoryPercent);
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
