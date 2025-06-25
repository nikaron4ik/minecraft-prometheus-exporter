package su.funtime.prometheusexporter.metrics;

import org.bukkit.plugin.Plugin;
import su.funtime.prometheusexporter.api.MetricCollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkTrafficProcess extends MetricCollector {
    private static final String interfaceName = "eth0"; // имя сетевого интерфейса

    public NetworkTrafficProcess(Plugin plugin) {
        super(plugin, "network_bytes", "Network traffic in bytes", true);
    }

    @Override
    public List<String> getLabelNames() {
        return List.of("type");
    }

    @Override
    public Map<List<String>, Double> collectWithLabels() {
        Map<List<String>, Double> map = new HashMap<>();
        long[] traffic = getNetworkTrafficFromProcess();

        map.put(List.of("received"), (double) traffic[0]);
        map.put(List.of("sent"), (double) traffic[1]);

        return map;
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
    public boolean isAsyncCapable() {
        return true;
    }
}
