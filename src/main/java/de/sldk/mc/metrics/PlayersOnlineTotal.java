package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayersOnlineTotal extends WorldMetric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online per world")
            .labelNames("world")
            .create();

    public PlayersOnlineTotal(Plugin plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void clear() {
    }

    @Override
    protected void collect(World world) {
        List<Player> players = world.getPlayers();
        players.removeIf(player -> player.hasMetadata("NPC"));
        PLAYERS_ONLINE.labels(world.getName()).set(players.size());
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
