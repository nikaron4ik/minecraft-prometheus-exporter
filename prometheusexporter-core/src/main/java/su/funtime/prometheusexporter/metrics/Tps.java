package su.funtime.prometheusexporter.metrics;

import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.collectors.TpsCollector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Tps extends MetricCollector {


    private int taskId;

    private TpsCollector tpsCollector = new TpsCollector();

    public Tps(Plugin plugin) {
        super(plugin, "tps", "Server TPS (ticks per second)", false);
    }

    @Override
    public void onRegister() {
        this.taskId = startTask(getPlugin());
    }

    @Override
    public void onUnregister() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private int startTask(Plugin plugin) {
        return Bukkit.getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(plugin, tpsCollector, 0, TpsCollector.POLL_INTERVAL);
    }

    @Override
    public double collect() {
        return tpsCollector.getAverageTPS();
    }
}
