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
import growthcraft.netherloid.netherloid;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNetherSquash extends BlockDirectional
{
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public BlockNetherSquash()
	{
		super(Blocks.PUMPKIN.getMaterial());
		setTickRandomly(true);
		setBlockTextureName("grcnetherloid:soulsquash");
		setBlockName("grcnetherloid.netherSquash");
		setCreativeTab(netherloid.tab);
	}

	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack itemstack)
	{
		final int meta = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
		world.setBlockState(x, y, z, meta, BlockFlags.SYNC);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		icons = new IIcon[3];

		icons[0] = reg.registerIcon(getTextureName() + "_side");
		icons[1] = reg.registerIcon(getTextureName() + "_top");
		icons[2] = reg.registerIcon(getTextureName() + "_face");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		if (side == 1)
		{
			return icons[1];
		}
		else if (side == 0)
		{
			return icons[0];
		}
		else
		{
			switch (meta)
			{
				case 0:
					if (side == 3) return icons[2];
					break;
				case 1:
					if (side == 4) return icons[2];
					break;
				case 2:
					if (side == 2) return icons[2];
					break;
				case 3:
					if (side == 5) return icons[2];
					break;
				default:
					break;
			}
		}
		return icons[0];
	}
}
