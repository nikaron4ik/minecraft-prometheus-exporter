package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

public class Memory extends MetricCollector {

    public Memory(Plugin plugin) {
        super(plugin,
                "jvm_memory",
                "JVM used memory in percentage",
                true);
    }

    @Override
    public double collect() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        return ((double) usedMemory / maxMemory) * 100.0;
    }

}
