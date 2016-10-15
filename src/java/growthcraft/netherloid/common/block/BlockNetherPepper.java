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
import growthcraft.api.core.util.RenderType;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.netherloid.netherloid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockNetherPepper extends BlockBush implements ICropDataProvider, IGrowable
{
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

	public static class PepperStages
	{
		public static final int SEEDLING = 0;
		public static final int YOUNG = 1;
		public static final int FULL = 2;
		public static final int FRUIT = 3;
		public static final int COUNT = 4;

		private PepperStages() {}
	}

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private int minPepperPicked = netherloid.getConfig().minPepperPicked;
	private int maxPepperPicked = netherloid.getConfig().maxPepperPicked;

	public BlockNetherPepper()
	{
		super(Material.PLANTS);
		setTickRandomly(true);
		setBlockTextureName("grcnetherloid:pepper");
		setBlockName("grcnetherloid.netherPepper");
	}

	private void incrementGrowth(World world, BlockPos pos, int meta, IBlockState state)
	{
		world.setBlockState(pos, state, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	public boolean isFullyGrown(World world, BlockPos pos)
	{
		return world.getBlockState(pos) >= PepperStages.FRUIT;
	}

	public boolean canGrow(World world, BlockPos pos)
	{
		return world.getBlockState(pos) < PepperStages.FRUIT;
	}

	/* IGrowable: can this grow anymore */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean b)
	{
		return !isFullyGrown(world, pos);
	}

	/* IGrowable: does this accept bonemeal */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return canGrow(world, pos);
	}

	public boolean onUseBonemeal(World world, BlockPos pos)
	{
		if (canGrow(world, pos))
		{
			if (!world.isRemote)
			{
				incrementGrowth(world, pos, world.getBlockState(pos));
			}
			return true;
		}
		return false;
	}

	/* IGrowable: Apply bonemeal effect */
	public void func_149853_b(World world, Random random, BlockPos pos)
	{
		onUseBonemeal(world, pos);
	}

	@Override
	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)PepperStages.FRUIT;
	}

	protected boolean func_149854_a(Block block)
	{
		return Blocks.SOUL_SAND == block;
	}

	@Override
	public void updateTick(World world, BlockPos pos, Random random, int meta)
	{
		final Event.Result result = AppleCore.validateGrowthTick(this, world, pos, random);
		if (Event.Result.DENY == result) return;

		if (canGrow(world, pos))
		{
			if (Event.Result.ALLOW == result || random.nextInt(10) == 0)
			{
				incrementGrowth(world, pos, meta, world.getBlockState(pos));
			}
		}
		super.updateTick(world, pos, random);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return netherloid.items.netherPepper.getItem();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int side, float par7, float par8, float par9, IBlockState state)
	{
		if (isFullyGrown(world, pos))
		{
			if (!world.isRemote)
			{
				world.setBlockState(pos, state, PepperStages.FULL, BlockFlags.SYNC);
				final int count = minPepperPicked + world.rand.nextInt(maxPepperPicked - minPepperPicked);
				dropBlockAsItem(world, pos, state, netherloid.items.netherPepper.asStack(count));
			}
			return true;
		}
		return false;
	}

	@Override
	public int getRenderType()
	{
		return RenderType.CROPS;
	}

	@Override
	@SideOnly(Side.CLIENT)

	{
		this.icons = new IIcon[PepperStages.COUNT];

		for (int stage = 0; stage < icons.length; ++stage)
		{
			icons[stage] = reg.registerIcon(getTextureName() + "_stage_" + stage);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (meta >= PepperStages.SEEDLING && meta <= PepperStages.FRUIT)
		{
			return icons[meta];
		}
		return icons[PepperStages.FRUIT];
	}
}
