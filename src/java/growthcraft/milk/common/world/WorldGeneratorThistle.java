package growthcraft.milk.common.world;

import growthcraft.api.core.util.BiomeUtils;
import growthcraft.milk.GrowthCraftMilk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Created by Firedingo on 25/02/2016.
 */
public class WorldGeneratorThistle implements IWorldGenerator
{
	private WorldGenerator thistle;

	private boolean canPlaceOnBlock(World world, BlockPos pos, Block block)
	{
		return Blocks.DIRT.equals(block) ||
			Blocks.GRASS.equals(block);
	}

	private boolean canReplaceBlock(World world, BlockPos pos, Block block, IBlockState state)
	{
		return block.isAir(state, world, pos) ||
			block.isLeaves(state, world, pos) ||
			Blocks.VINE == block;
	}

	private void genRandThistle(WorldGenerator generator, World world, Random rand, Block block, BlockPos pos, IBlockState state, int chunk_x, int chunk_z, int maxToSpawn, int minHeight, int maxHeight)
	{
		final int genChance = GrowthCraftMilk.getConfig().thistleGenChance;
		for (int i = 0; i < maxToSpawn; ++i)
		{
			if (genChance > 0)
			{
				if (rand.nextInt(genChance) != 0) continue;
			}

			final int x = chunk_x * 16 + rand.nextInt(16);
			final int z = chunk_z * 16 + rand.nextInt(16);
			int y = maxHeight;
			for (; y > minHeight; --y)
			{
				// If you can't replace the block now, it means you probably
				// hit the floor
				if (!canReplaceBlock(world, pos, block, state, world.getBlockState(pos)))
				{
					// move back up and break loop
					y += 1;
					break;
				}
			}
			// If we've exceeded the minHeight, bail this operation immediately
			if (y <= minHeight)
			{
				continue;
			}

			final Block block = world.getBlockState(x, y - 1, z);
			if (canPlaceOnBlock(world, x, y - 1, z, block))
			{
				world.setBlockState(pos, state, GrowthCraftMilk.blocks.thistle.getBlockState());
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.provider.dimensionId == 0)
		{
			final Biome biome = world.getBiome(chunkX, chunkZ);
			if (GrowthCraftMilk.getConfig().thistleUseBiomeDict)
			{
				if (!BiomeUtils.testBiomeTypeTagsTable(biome, GrowthCraftMilk.getConfig().thistleBiomesTypeList)) return;
			}
			else
			{
				final String biomeId = "" + biome.BiomeID;
				if (!BiomeUtils.testBiomeIdTags(biomeId, GrowthCraftMilk.getConfig().thistleBiomesIdList)) return;
			}
			genRandThistle(thistle, world, random, chunkX, chunkZ, GrowthCraftMilk.getConfig().thistleGenAmount, 64, 255);
		}
	}
}
