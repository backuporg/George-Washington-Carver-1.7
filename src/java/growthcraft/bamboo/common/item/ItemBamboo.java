package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBamboo extends GrcItemBase
{
	public ItemBamboo()
	{
		super();
		setUnlocalizedName("grc.bamboo");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, int EnumFacing, float par8, float par9, float par10)
	{
		final Block block1 = world.getBlockState(x, y, z);

		if (block1 == Blocks.SNOW && (world.getBlockState(x, y, z) & 7) < 1)
		{
			EnumFacing = 1;
		}
		else if (block1 != Blocks.VINE && block1 != Blocks.TALLGRASS && block1 != Blocks.DEADBUSH)
		{
			if (EnumFacing == 0)
			{
				--y;
			}

			if (EnumFacing == 1)
			{
				++y;
			}

			if (EnumFacing == 2)
			{
				--z;
			}

			if (EnumFacing == 3)
			{
				++z;
			}

			if (EnumFacing == 4)
			{
				--x;
			}

			if (EnumFacing == 5)
			{
				++x;
			}
		}

		if (!player.canPlayerEdit(x, y, z, EnumFacing, stack))
		{
			return false;
		}
		else if (stack.stackSize == 0)
		{
			return false;
		}
		else
		{
			final Block block = GrowthCraftBamboo.blocks.bambooStalk.getBlockState();
			if (world.canPlaceEntityOnSide(block, x, y, z, false, EnumFacing, (Entity)null, stack))
			{
				if (world.setBlockState(x, y, z, block, 1, 3))
				{
					if (world.getBlockState(x, y, z) == block)
					{
						block.onBlockPlacedBy(world, x, y, z, player, stack);
						block.onPostBlockPlaced(world, x, y, z, 1);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcbamboo:bamboo");
	}
}
