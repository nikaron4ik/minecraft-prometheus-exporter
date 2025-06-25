package su.funtime.prometheusexporter.metrics;

import org.bukkit.Bukkit;
import su.funtime.prometheusexporter.api.MetricCollector;
import su.funtime.prometheusexporter.metrics.player.PlayerStatisticLoaderFromBukkit;
import su.funtime.prometheusexporter.metrics.player.EmptyStatisticLoader;
import su.funtime.prometheusexporter.metrics.player.PlayerStatisticLoaderFromFile;
import su.funtime.prometheusexporter.metrics.player.PlayerStatisticLoader;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

/**
 * Offline player -> fetch data from files
 * <p>
 * Online player -> fetch data from Minecraft API
 */
public class PlayerStatistics extends MetricCollector {

	private static Logger logger;

	private final LinkedHashSet<PlayerStatisticLoader> statisticLoaderChain = new LinkedHashSet<>();

	public PlayerStatistics(Plugin plugin) {
		super(plugin,"player_statistic", "Player statistics", false);

		logger = plugin.getLogger();

		statisticLoaderChain.add(new PlayerStatisticLoaderFromBukkit(plugin));
		statisticLoaderChain.add(new PlayerStatisticLoaderFromFile(plugin));
		statisticLoaderChain.add(new EmptyStatisticLoader());
	}

	public List<String> getLabelNames() {
		return List.of("player_name", "player_uid", "statistic");
	}

	@Override
	public Map<List<String>, Double> collectWithLabels() {
		Map<List<String>, Double> map = new HashMap<>();

		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			String uid = player.getUniqueId().toString();
			String name = player.getName() != null ? player.getName() : player.getUniqueId().toString();

			for (PlayerStatisticLoader loader : statisticLoaderChain) {
				if (collectSuccessful(loader, player, name, uid, map)) {
					break;
				}
			}
		}
		return map;
	}

    private boolean collectSuccessful(PlayerStatisticLoader loader, OfflinePlayer player, String name, String uid, Map<List<String>, Double> map) {
		try {
			Map<Enum<?>, Integer> statistics = loader.getPlayerStatistics(player);

			if (statistics == null || statistics.isEmpty()) return false;

			for (Map.Entry<Enum<?>, Integer> entry : statistics.entrySet()) {
				map.put(List.of(name, uid, entry.getKey().name()), entry.getValue().doubleValue());
			}

			return true;
		} catch (Exception e) {
			String message =
					String.format("%s: Could not load statistics for player '%s'",
							loader.getClass().getSimpleName(), uid);
			logger.log(Level.FINE, message, e);
			return false;
		}
	}
}
