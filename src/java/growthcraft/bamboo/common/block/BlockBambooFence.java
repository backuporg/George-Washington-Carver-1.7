package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooFence;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooFence extends BlockFence
{
	@SideOnly(Side.CLIENT)


	public BlockBambooFence()
	{
		super(null, Material.WOOD);
		this.useNeighborBrightness = true;
		setSoundType(SoundType.WOOD);
		setResistance(5.0F);
		setHardness(2.0F);
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setBlockName("grc.bambooFence");
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean getBlocksMovement(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	public boolean canConnectFenceTo(IBlockAccess world, BlockPos pos)
	{
		final Block block = world.getBlockState(x, y, z);

		if (this == block ||
			(block instanceof BlockFence) ||
			(block instanceof BlockFenceGate) ||
			GrowthCraftBamboo.blocks.bambooWall.isSameAs(block) ||
			GrowthCraftBamboo.blocks.bambooStalk.isSameAs(block))
		{
			return true;
		}
		else
		{
			if (block != null && block.getMaterial().isOpaque() && block.renderAsNormalBlock())
			{
				return block.getMaterial() != Material.GOURD;
			}
		}
		return false;
	}


	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBambooFence.id;
	}
}
