/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.netherloid.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.netherloid.common.world.WorldGeneratorMaliceTree;
import growthcraft.netherloid.netherloid;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockNetherMaliceSapling extends BlockBush implements IGrowable
{
	@SideOnly(Side.CLIENT)
	private IIcon icon;

	private final int growth = 8;

	public BlockNetherMaliceSapling()
	{
		super(Material.PLANTS);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grcnetherloid.netherMaliceSapling");
		setTickRandomly(true);
		setCreativeTab(netherloid.tab);
		final float f = 0.4F;
		getBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	/************
	 * MAIN
	 ************/
	public void updateTick(World world, BlockPos pos, Random random, IBlockState state)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, pos, state, random);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(this.growth) == 0)
			{
				this.markOrGrowMarked(world, pos, random);
			}
		}
	}

	public void markOrGrowMarked(World world, BlockPos pos, Random random, IBlockState state)
	{
		final int meta = world.getBlockState(pos);

		if ((meta & 8) == 0)
		{
			world.setBlockState(pos, state, meta | 8, 4);
		}
		else
		{
			this.growTree(world, x, y, z, random);
		}
	}

	public void growTree(World world, BlockPos pos, Random random)
	{
		if (!TerrainGen.saplingGrowTree(world, random, pos)) return;

		final int meta = world.getBlockState(pos) & 3;
		final WorldGenerator generator = new WorldGeneratorMaliceTree(true);

		world.setBlockState(pos, Blocks.AIR, 0, BlockFlags.ALL);

		if (!generator.generate(world, random, pos))
		{
			world.setBlockState(pos, this, meta, BlockFlags.ALL);
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		return (world.getBlockState(pos) & 8) == 0;
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
			growTree(world, pos, random);
		}
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	icon = reg.registerIcon("grcnetherloid:malicesapling");
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	///
	//{
	//	return this.icon;
	//}

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
