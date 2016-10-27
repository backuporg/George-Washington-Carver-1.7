package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public abstract class BlockBambooSlab extends BlockSlab
{
	public BlockBambooSlab(boolean par2)
	{
		super(par2, Material.WOOD);
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setUnlocalizedName("grc.bambooSlab");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (GrowthCraftBamboo.blocks.bambooDoubleSlab.getItem() != item)
		{
			list.add(new ItemStack(item, 1, 0));
		}
	}

	@Override
	public String func_150002_b(int par1)
	{
		return super.getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	private static boolean isBlockSingleSlab(Block block)
	{
		return block == GrowthCraftBamboo.blocks.bambooSingleSlab.getBlockState();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return GrowthCraftBamboo.blocks.bambooSingleSlab.getItem();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return GrowthCraftBamboo.blocks.bambooSingleSlab.getItem();
	}

	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(GrowthCraftBamboo.blocks.bambooSingleSlab.getItem(), 2, 0);
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)
//
	//{
		return GrowthCraftBamboo.blocks.bambooBlock.getBlockState().getIcon(side, meta);
	//}
}
