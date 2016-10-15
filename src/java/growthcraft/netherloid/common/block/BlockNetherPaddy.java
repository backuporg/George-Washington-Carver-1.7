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
import growthcraft.core.common.block.BlockPaddyBase;
import growthcraft.netherloid.netherloid;
import growthcraft.netherloid.util.NetherBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockNetherPaddy extends BlockPaddyBase
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	private final int paddyFieldMax = netherloid.getConfig().paddyFieldMax;
	private final boolean filledPaddy;

	public BlockNetherPaddy(boolean filled)
	{
		super(Material.SAND);
		setHardness(0.5F);
		setBlockName("grcnetherloid.netherPaddyField");
		setCreativeTab(netherloid.tab);
		this.filledPaddy = filled;
		if (filledPaddy)
		{
			setLightLevel(1.0F);
		}
		else
		{
			setStepSound(soundTypeSand);
		}
	}

	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		if (world.isRemote) return;

		entity.motionX *= 0.4D;
		entity.motionZ *= 0.4D;

		// set fire to the entity if they step into a filled lava paddy
		if (filledPaddy) entity.setFire(15);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random, IBlockState state)
	{
		super.randomDisplayTick(state, world, pos, random);
		if (filledPaddy)
		{
			if (world.getBlockState(x, y + 1, z).getMaterial() == Material.AIR && !world.getBlockState(x, y + 1, z).isOpaqueCube())
			{
				if (random.nextInt(100) == 0)
				{
					final double px = (double)((float)x + random.nextFloat());
					final double py = (double)y + getBlockBoundsMaxY();
					final double pz = (double)((float)z + random.nextFloat());
					world.spawnParticle("lava", px, py, pz, 0.0D, 0.0D, 0.0D);
					world.playSound(px, py, pz, "liquid.lavapop", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}

				if (random.nextInt(200) == 0)
				{
					world.playSound((double)x, (double)y, (double)z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
				}
			}
		}
	}

	/**
	 * Returns the fluid block used to fill this paddy
	 *
	 * @return fluid block
	 */
	@Override
	@Nonnull public Block getFluidBlock()
	{
		return Blocks.LAVA;
	}

	@Override
	@Nonnull public Fluid getFillingFluid()
	{
		return FluidRegistry.LAVA;
	}

	@Override
	public int getMaxPaddyMeta(IBlockAccess world, BlockPos pos)
	{
		return paddyFieldMax;
	}

	@Override
	public boolean isBelowFillingFluid(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(x, y + 1, z).getMaterial() == Material.LAVA;
	}

	@Override
	public void drainPaddy(World world, BlockPos pos, IBlockState state)
	{
		final int meta = world.getBlockState(pos);
		if (meta > 1)
		{
			world.setBlockState(pos, state, meta - 1, BlockFlags.SYNC);
		}
		else
		{
			final Block targetBlock = netherloid.blocks.netherPaddyField.getBlockState();
			if (this != targetBlock)
			{
				world.setBlockState(pos, targetBlock, 0, BlockFlags.SYNC);
			}
		}
	}

	@Override
	public void fillPaddy(World world, BlockPos pos, IBlockState state)
	{
		final Block targetBlock = netherloid.blocks.netherPaddyFieldFilled.getBlockState();
		if (this != targetBlock)
		{
			world.setBlockState(pos, targetBlock, getMaxPaddyMeta(world, pos), BlockFlags.SYNC);
		}
		else
		{
			world.setBlockState(pos, getMaxPaddyMeta(world, pos), BlockFlags.SYNC);
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(Blocks.SOUL_SAND);
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return Item.getItemFromBlock(Blocks.SOUL_SAND);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	icons = new IIcon[3];
//
	//	icons[0] = reg.registerIcon("soul_sand");
	//	icons[1] = reg.registerIcon("grcnetherloid:soul_sand_paddy_dry");
	//	icons[2] = reg.registerIcon("grcnetherloid:soul_sand_paddy_wet");
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public IIcon getIcon(int side, int meta)
	//{
	//	if (side == 1)
	//	{
	//		if (meta == 0)
	//		{
	//			return icons[1];
	//		}
	//		else
	//		{
	///			return icons[2];
	//		}
	//	}
	//	return icons[0];
	//}

	public boolean canConnectPaddyTo(IBlockAccess world, int i, int j, int k, int m)
	{
		if (m > 0)
		{
			m = 1;
		}

		int meta = world.getBlockState(i, j, k);

		if (meta > 0)
		{
			meta = 1;
		}

		return NetherBlockCheck.isPaddy(world.getBlockState(i, j, k)) && meta == m;
	}
}
