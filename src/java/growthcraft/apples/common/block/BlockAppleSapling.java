package growthcraft.apples.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.common.world.WorldGenAppleTree;
import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class BlockAppleSapling extends BlockBush implements IGrowable
{
	private final int growth = GrowthCraftApples.getConfig().appleSaplingGrowthRate;

	public BlockAppleSapling()
	{
		super(Material.PLANTS);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setBlockName("grc.appleSapling");
		setTickRandomly(true);
		setCreativeTab(GrowthCraftCore.creativeTab);
		setBlockTextureName("grcapples:apple_sapling");
		final float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	/************
	 * MAIN
	 ************/
	public void updateTick(World world, BlockPos pos, Random random)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, x, y, z, random);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(this.growth) == 0)
			{
				this.markOrGrowMarked(world, x, y, z, random);
			}
		}
	}

	public void markOrGrowMarked(World world, BlockPos pos, Random random)
	{
		final int meta = world.getBlockState(x, y, z);

		if ((meta & 8) == 0)
		{
			world.setBlockState(x, y, z, meta | 8, BlockFlags.SUPRESS_RENDER);
		}
		else
		{
			this.growTree(world, x, y, z, random);
		}
	}

	public void growTree(World world, BlockPos pos, Random random)
	{
		if (!TerrainGen.saplingGrowTree(world, random, x, y, z)) return;

		final int meta = world.getBlockState(x, y, z) & 3;
		final WorldGenerator generator = new WorldGenAppleTree(true);

		world.setBlockToAir(x, y, z);

		if (!generator.generate(world, random, x, y, z))
		{
			world.setBlockState(x, y, z, this, meta, BlockFlags.ALL);
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		return (world.getBlockState(x, y, z) & 8) == 0;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos)
	{
		if (random.nextFloat() < 0.45D)
		{
			growTree(world, x, y, z, random);
		}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

	}
}
