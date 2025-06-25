package su.funtime.prometheusexporter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class VillagersTest {

	private Villagers villagersMetric;
	private World world;

	@BeforeEach
	void setup() {
		Plugin plugin = mock(Plugin.class);
		villagersMetric = new Villagers(plugin);
		world = mock(World.class);
	}

	@Test
	void givenVillagersExpectCorrectCount() {
		// Setup
		final String worldName = "world_name";
		final long numDesertFarmersLevel1 = 2;
		final long numPlainsNoneLevel2 = 3;

		List<Villager> villagers = Stream.concat(
				mockVillagers(numDesertFarmersLevel1, Villager.Type.DESERT, Villager.Profession.FARMER, 1),
				mockVillagers(numPlainsNoneLevel2, Villager.Type.PLAINS, Villager.Profession.NONE, 2)
		).collect(Collectors.toList());

		when(world.getName()).thenReturn(worldName);
		when(world.getEntitiesByClass(Villager.class)).thenReturn(villagers);


		mockStatic(org.bukkit.Bukkit.class).when(org.bukkit.Bukkit::getWorlds).thenReturn(List.of(world));


		Map<List<String>, Double> result = villagersMetric.collectWithLabels();


		assertThat(result.get(List.of(worldName, "desert", "farmer", "1")))
				.isEqualTo(numDesertFarmersLevel1);

		assertThat(result.get(List.of(worldName, "plains", "none", "2")))
				.isEqualTo(numPlainsNoneLevel2);
	}

	private Stream<Villager> mockVillagers(long count, Villager.Type type, Villager.Profession profession, int level) {
		return LongStream.range(0, count)
				.mapToObj(i -> mockVillager(type, profession, level));
	}

	private Villager mockVillager(Villager.Type type, Villager.Profession profession, int level) {
		Villager villager = mock(Villager.class);
		when(villager.getVillagerType()).thenReturn(type);
		when(villager.getProfession()).thenReturn(profession);
		when(villager.getVillagerLevel()).thenReturn(level);
		return villager;
	}
}
