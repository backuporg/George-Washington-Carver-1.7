package growthcraft.hops.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.hops.GrowthCraftHops;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraft.util.EnumFacing;

public class ItemHopSeeds extends GrcItemBase implements IPlantable
{
	public ItemHopSeeds()
	{
		super();
		this.setUnlocalizedName("grc.hopSeeds");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	/************
	 * MAIN
	 ************/
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int EnumFacing, float par8, float par9, float par10)
	{
		if (EnumFacing != 1)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z, EnumFacing, stack) && player.canPlayerEdit(x, y + 1, z, EnumFacing, stack))
		{
			if (BlockCheck.canSustainPlant(world, x, y, z, EnumFacing.UP, GrowthCraftHops.blocks.hopVine.getBlock()) && BlockCheck.isRope(world, x, y + 1, z))
			{
				world.setBlock(x, y + 1, z, GrowthCraftHops.blocks.hopVine.getBlock());
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
		this.itemIcon = reg.registerIcon("grchops:hop_seeds");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z)
	{
		return GrowthCraftHops.blocks.hopVine.getBlock();
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
}
