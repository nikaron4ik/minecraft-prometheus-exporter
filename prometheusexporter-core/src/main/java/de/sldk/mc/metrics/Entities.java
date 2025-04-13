package de.sldk.mc.metrics;

import io.prometheus.client.Gauge;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * Get current count of all entities.
 * <p>
 * Entities are labelled by
 * <ol>
 *     <li> world,
 *     <li> type ({@link EntityType}),
 *     <li> alive ({@link EntityType#isAlive()}),
 *     <li> and spawnable ({@link EntityType#isSpawnable()})
 * </ol>
 */
public class Entities extends WorldMetric {

    private static final Gauge ENTITIES = Gauge.build()
            .name(prefix("entities_total"))
            .help("Entities loaded per world")
            .labelNames("world", "type", "alive", "spawnable")
            .create();

    /**
     * Override the value returned by {@link EntityType#isAlive()}.
     */
    private static final Map<EntityType, Boolean> ALIVE_OVERRIDE = singletonMap(EntityType.ARMOR_STAND, false);

    public Entities(Plugin plugin) {
        super(plugin, ENTITIES);
    }

    @Override
    protected void clear() {
        ENTITIES.clear();
    }

    @Override
    public void collect(World world) {
        Map<EntityType, Long> mapEntityTypesToCounts = new HashMap<>();

        // Цикл для подсчёта всех мобов на сервере
        Location location;
        for (Entity entity : world.getEntities() ) {
            location = entity.getLocation();
            // По неизвестной причине некоторые рамки и вагонетки из выгруженных чанков у нас возвращаются ядром,
            // при этом isValid() для них true
            if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) continue;
            EntityType type = entity.getType();
            mapEntityTypesToCounts.put(type, mapEntityTypesToCounts.getOrDefault(type, 0L) + 1);
        }

        // Цикл для сбора метрики по каждому подсчитанному мобу
        for (Map.Entry<EntityType, Long> entry : mapEntityTypesToCounts.entrySet()) {
            EntityType entityType = entry.getKey();
            long count = entry.getValue();

            ENTITIES
                    .labels(world.getName(),
                            getEntityName(entityType),
                            Boolean.toString(isEntityTypeAlive(entityType)),
                            Boolean.toString(entityType.isSpawnable()))
                    .set(count);
        }
    }

    private boolean isEntityTypeAlive(EntityType type) {
        return ALIVE_OVERRIDE.containsKey(type) ? ALIVE_OVERRIDE.get(type) : type.isAlive();
    }
}
