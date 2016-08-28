package growthcraft.api.core.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import net.minecraft.world.biome.Biome;

public class BiomeUtilsTest
{
	@Test
	public void test_testBiomeIdTags()
	{
		// It should report false, for an empty tag list
		{
			final TagParser.Tag[] tags = TagParser.csv.parse("");
			assertFalse(BiomeUtils.testBiomeIdTags("14", tags));
			assertFalse(BiomeUtils.testBiomeIdTags("0", tags));
		}

		{
			final TagParser.Tag[] tags = TagParser.csv.parse("12,32");
			assertTrue(BiomeUtils.testBiomeIdTags("12", tags));
			assertTrue(BiomeUtils.testBiomeIdTags("32", tags));
			assertFalse(BiomeUtils.testBiomeIdTags("14", tags));
			assertFalse(BiomeUtils.testBiomeIdTags("1", tags));
		}
	}

	@Test
	public void test_testBiomeTypeTags()
	{
		// it should report true if the biome IS a mountain
		{
			final TagParser.Tag[] tags = TagParser.csv.parse("+MOUNTAIN");
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.extremeHills, tags));
			assertFalse(BiomeUtils.testBiomeTypeTags(Biome.hell, tags));
		}

		// it should report true if the biome is some kind of hills or mountain
		{
			final TagParser.Tag[] tags = TagParser.csv.parse("MOUNTAIN,HILLS");
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.extremeHills, tags));
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.extremeHillsEdge, tags));
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.taigaHills, tags));
			assertFalse(BiomeUtils.testBiomeTypeTags(Biome.frozenOcean, tags));
		}

		// it should report true if the biome IS a mountain, optionally a mountain hills
		{
			final TagParser.Tag[] tags = TagParser.csv.parse("+MOUNTAIN,HILLS");
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.extremeHills, tags));
			assertTrue(BiomeUtils.testBiomeTypeTags(Biome.extremeHillsEdge, tags));
			assertFalse(BiomeUtils.testBiomeTypeTags(Biome.taigaHills, tags));
		}
	}
}
