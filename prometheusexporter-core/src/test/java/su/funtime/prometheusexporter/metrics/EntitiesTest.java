package su.funtime.prometheusexporter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class EntitiesTest {

	private Entities entitiesMetric;

	@BeforeEach
	void beforeEachTest() {
		entitiesMetric = new Entities(mock(Plugin.class));
	}

	@Test
	void givenTypedEntitiesExpectCorrectCount() {
		final String worldName = "world_name";
		final long numOfPigs = 1;
		final long numOfHorses = 6;
		final long numOfOrbs = 800;
		final long numOfChicken = 9000;
		final long numOfMinecarts = 10000;

		List<Entity> mockedEntities = new ArrayList<>();
		mockedEntities.addAll(mockEntities(numOfPigs, EntityType.PIG));
		mockedEntities.addAll(mockEntities(numOfHorses, EntityType.HORSE));
		mockedEntities.addAll(mockEntities(numOfOrbs, EntityType.EXPERIENCE_ORB));
		mockedEntities.addAll(mockEntities(numOfChicken, EntityType.CHICKEN));
		mockedEntities.addAll(mockEntities(numOfMinecarts, EntityType.MINECART));
		Collections.shuffle(mockedEntities);

		World world = mock(World.class);
		when(world.getName()).thenReturn(worldName);
		when(world.getEntities()).thenReturn(mockedEntities);
		when(world.isChunkLoaded(anyInt(), anyInt())).thenReturn(true);

		try (MockedStatic<Bukkit> mocked = mockStatic(Bukkit.class)) {
			mocked.when(Bukkit::getWorlds).thenReturn(List.of(world));

			Map<List<String>, Double> result = entitiesMetric.collectWithLabels();

			assertThat(result.get(List.of(worldName, "pig", "true", "true"))).isEqualTo(numOfPigs);
			assertThat(result.get(List.of(worldName, "horse", "true", "true"))).isEqualTo(numOfHorses);
			assertThat(result.get(List.of(worldName, "experience_orb", "false", "true"))).isEqualTo(numOfOrbs);
			assertThat(result.get(List.of(worldName, "chicken", "true", "true"))).isEqualTo(numOfChicken);
			assertThat(result.get(List.of(worldName, "minecart", "false", "true"))).isEqualTo(numOfMinecarts);
		}
	}

	// --- Helpers ---

	private List<Entity> mockEntities(long count, EntityType type) {
		return LongStream.range(0, count).mapToObj(i -> mockEntity(type)).collect(Collectors.toList());
	}

	private Entity mockEntity(EntityType type) {
		Entity e = mock(Entity.class);
		when(e.getType()).thenReturn(type);
		when(e.getLocation()).thenReturn(new Location(null, 0, 0, 0));
		return e;
	}
}
