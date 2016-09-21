package growthcraft.rice.common.item;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRice extends GrcItemBase
{
	public ItemRice()
	{
		super();
		this.setUnlocalizedName("grc.rice");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, int EnumFacing, float par8, float par9, float par10)
	{
		if (EnumFacing != 1)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z, EnumFacing, stack) && player.canPlayerEdit(x, y + 1, z, EnumFacing, stack))
		{
			final Block soil = world.getBlockState(x, y, z);

			if (soil != null && RiceBlockCheck.isPaddy(soil) && world.isAirBlock(x, y + 1, z) && world.getBlockState(x, y, z) > 0)
			{
				world.setBlockState(x, y + 1, z, GrowthCraftRice.blocks.riceBlock.getBlockState());
				--stack.stackSize;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcrice:rice");
	}

}
