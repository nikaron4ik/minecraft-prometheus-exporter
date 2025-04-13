package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

public class Uptime extends Metric {

    private static final Gauge UPTIME = Gauge.build()
            .name(prefix("uptime"))
            .help("Server uptime in hours")
            .create();
    private final long serverStartTime;

    public Uptime(Plugin plugin) {
        super(plugin, UPTIME);
        this.serverStartTime = System.currentTimeMillis();
    }

    private double getUptimeInHours() {
        return (System.currentTimeMillis() - serverStartTime) / 1000.0 / 60.0 / 60.0;
    }


    public void doCollect() {
        UPTIME.set(getUptimeInHours());
    }
}
