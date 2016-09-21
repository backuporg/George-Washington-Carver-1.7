package growthcraft.grapes.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.common.block.BlockGrapeVine0;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemGrapeSeeds extends GrcItemBase implements IPlantable
{
	public ItemGrapeSeeds()
	{
		super();
		this.setUnlocalizedName("grc.grapeSeeds");
		this.setCreativeTab(GrowthCraftGrapes.creativeTab);
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
			final BlockGrapeVine0 block = GrowthCraftGrapes.blocks.grapeVine0.getBlockState();
			if (BlockCheck.canSustainPlant(world, x, y, z, EnumFacing.UP, block) && world.isAirBlock(x, y + 1, z))
			{
				world.setBlockState(x, y + 1, z, block);
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
		this.itemIcon = reg.registerIcon("grcgrapes:grape_seeds");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, BlockPos pos)
	{
		return GrowthCraftGrapes.blocks.grapeVine0.getBlockState();
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
}
