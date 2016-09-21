package growthcraft.apples.common.item;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public abstract class ItemAppleSeeds extends GrcItemBase implements IPlantable
{
	private Block cropBlock;

	public ItemAppleSeeds()
	{
		super();
		this.cropBlock = GrowthCraftApples.blocks.appleSapling.getBlockState();
		this.setUnlocalizedName("grc.appleSeeds");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, int EnumFacing, float par8, float par9, float par10)
	{
		final Block block = world.getBlockState(x, y, z);
		if (block == Blocks.SNOW_LAYER && (world.getBlockState(x, y, z) & 7) < 1)
		{
			EnumFacing = 1;
		}
		else if (block != Blocks.VINE && block != Blocks.TALLGRASS && block != Blocks.DEADBUSH)
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
			if (world.canPlaceEntityOnSide(cropBlock, x, y, z, false, EnumFacing, (Entity)null, stack))
			{
				final int meta = cropBlock.onBlockPlaced(world, x, y, z, EnumFacing, par8, par9, par10, 0);

				if (world.setBlockState(x, y, z, cropBlock, meta, 3))
				{
					if (world.getBlockState(x, y, z) == cropBlock)
					{
						cropBlock.onBlockPlacedBy(world, x, y, z, player, stack);
						cropBlock.onPostBlockPlaced(world, x, y, z, meta);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), cropBlock.stepSound.func_150496_b(), (cropBlock.stepSound.getVolume() + 1.0F) / 2.0F, cropBlock.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public Block getPlant(IBlockAccess world, BlockPos pos)
	{
		return cropBlock;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, BlockPos pos)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcapples:apple_seed");
	}
}
