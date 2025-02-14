package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NetworkTrafficProcess extends Metric {

    private static final Gauge NETWORK_BYTES = Gauge.build()
            .name(prefix("network_bytes"))
            .help("Network traffic in bytes")
            .labelNames("type")
            .create();

    private static final String interfaceName = "eth0"; // имя сетевого интерфейса

    public NetworkTrafficProcess(Plugin plugin) {
        super(plugin, NETWORK_BYTES);
    }

    private long[] getNetworkTrafficFromProcess() {
        long[] traffic = new long[]{0, 0};
        try (BufferedReader reader = new BufferedReader(new FileReader("/proc/net/dev"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(interfaceName + ":")) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts.length > 9) {
                        try {
                            traffic[0] = Long.parseLong(parts[1]); // Received bytes
                            traffic[1] = Long.parseLong(parts[9]); // Sent bytes
                            break;
                        } catch (NumberFormatException e) {
                            getPlugin().getLogger().warning("Неверный формат числа в /proc/net/dev: " + e.getMessage());
                        }
                    } else {
                        getPlugin().getLogger().warning("Некорректный формат строки интерфейса: " + line);
                    }
                }
            }
        } catch (IOException e) {
            getPlugin().getLogger().warning("Ошибка при парсинге /proc/net/dev: " + e.getMessage());
        }
        return traffic;
    }

    @Override
    protected void doCollect() {
        long[] traffic = getNetworkTrafficFromProcess();
        long receivedBytes = traffic[0];
        long sentBytes = traffic[1];

        NETWORK_BYTES.labels("received").set(receivedBytes);
        NETWORK_BYTES.labels("sent").set(sentBytes);
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
