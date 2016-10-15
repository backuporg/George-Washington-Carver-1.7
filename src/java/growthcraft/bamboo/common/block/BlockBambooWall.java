package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooWall;
import growthcraft.core.common.block.GrcBlockBase;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBambooWall extends GrcBlockBase
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockBambooWall()
	{
		super(Material.WOOD);
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F / 3.0F);
		setHardness(2.0F);
		setBlockName("grc.bambooWall");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean getBlocksMovement(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return false;
	}

	public boolean canConnectWallTo(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		if (world.isAirBlock(pos)) return false;

		final IBlockState block = world.getBlockState(pos);

		if (this == block ||
			GrowthCraftBamboo.blocks.bambooStalk.getBlockState() == block ||
			Blocks.GLASS_PANE == block ||
			block instanceof BlockFenceGate ||
			block instanceof BlockFence ||
			block.renderAsNormalBlock()) return true;

		return false;
	}

	/************
	 * RENDERS
	 ************/
	//@Override
	//public int getRenderType()
	//{
	//	return RenderBambooWall.id;
	//}

	//@Override
	//public boolean isOpaqueCube()
	//{
	//	return false;
	//}
//
	//@Override
	//public boolean renderAsNormalBlock()
	//{
		//return false;
	//}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return true;
	}

	/************
	 * BOXES
	 ************/
	//@Override
	//public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	//{
	//	int tm;
//
	//	final Block idXneg = world.getBlockState(pos.getX() - 1, pos.getY(), pos.getZ());
	//	final Block idXpos = world.getBlockState(x + 1, y, z);
	//	final Block idZneg = world.getBlockState(x, y, z - 1);
	//	final Block idZpos = world.getBlockState(x, y, z + 1);
//
	//	int metaXneg = world.getBlockState(x - 1, y, z);
	//	int metaXpos = world.getBlockState(x + 1, y, z);
	//	int metaZneg = world.getBlockState(x, y, z - 1);
	//	int metaZpos = world.getBlockState(x, y, z + 1);
//
	//	final boolean flagXneg = this.canConnectWallTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
	//	final boolean flagXpos = this.canConnectWallTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
	//	final boolean flagZneg = this.canConnectWallTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
	//	final boolean flagZpos = this.canConnectWallTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);

	//	float x1 = 0.375F;
	//	float x2 = 0.625F;
	//	float z1 = 0.375F;
	//	float z2 = 0.625F;

	//	//ZNEG
	//	if (flagZneg)
	//	{
	//		z1 = 0.0F;
	//	}
	//	else if (idZneg instanceof BlockDoor)
	//	{
	//		if ((metaZneg & 8) > 7)
	//		{
	//			metaZneg = world.getBlockState(pos.getX(), pos.getY() - 1, pos.getZ() - 1);
	//		}
//
	//		tm = metaZneg & 3;
//
	//		if (tm == 0 || tm == 2)
	//		{
	//			z1 = 0.0F;
//
	//			if (tm == 0)
	//			{
	//				x1 = 0.0F;
	//			}
//
	//			if (tm == 2)
	//			{
	//				x2 = 1.0F;
	//			}
	//		}
	//	}
//
	//	//ZPOS
	//	if (flagZpos)
	//	{
	//		z2 = 1.0F;
	///	}
	//	else if (idZpos instanceof BlockDoor)
	//	{
	//		if ((metaZpos & 8) > 7)
	//		{
	//			metaZpos = world.getBlockState(x, y - 1, z + 1);
	//		}
//
	//		tm = metaZpos & 3;
//
	//		if (tm == 0 || tm == 2)
	//		{
	//			z2 = 1.0F;
//
	//			if (tm == 0)
	//			{
	///				x1 = 0.0F;
	//			}
//
	//			if (tm == 2)
	///			{
		//			x2 = 1.0F;
		//		}
		//	}
		//}

		////XNEG
	//	if (flagXneg)
		//{
	//		x1 = 0.0F;
	//	}
	//	else if (idXneg instanceof BlockDoor)
	//	{
	//		if ((metaXneg & 8) > 7)
	//		{
	//			metaXneg = world.getBlockState(x - 1, y - 1, z);
	//		}
///
	//		tm = metaXneg & 3;
//
	//		if (tm == 1 || tm == 3)
	//		{
	//			x1 = 0.0F;
//
	//			if (tm == 1)
	//			{
	//				z1 = 0.0F;
	//			}
//
	//			if (tm == 3)
	//			{
	//				z2 = 1.0F;
	//			}
	//		}
	//	}
////
		//XPOS
	//	if (flagXpos)
	//	{
	//		x2 = 1.0F;
	//	}
	//	else if (idXpos instanceof BlockDoor)
	//	{
	//		if ((metaXpos & 8) > 7)
	//		{
	//			metaXpos = world.getBlockState(x + 1, y - 1, z);
	//		}
//
	//		tm = metaXpos & 3;

	//		if (tm == 1 || tm == 3)
	//		{
	//			x2 = 1.0F;
//
	//			if (tm == 1)
	//			{
	//				z1 = 0.0F;
	//			}
//
	//			if (tm == 3)
	//			{
	//				z2 = 1.0F;
	//			}
	//		}
	//	}
//
		//this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
	//}

	//@Override
	//@SuppressWarnings({"rawtypes", "unchecked"})
	//public void getCollisionBoundingBox(World world, BlockPos pos, AxisAlignedBB axis, List list, Entity entity)
	//{
	//	int tm;
//
	//	final Block idXneg = world.getBlockState(x - 1, y, z);
	//	final Block idXpos = world.getBlockState(x + 1, y, z);
	//	final Block idZneg = world.getBlockState(x, y, z - 1);
	//	final Block idZpos = world.getBlockState(x, y, z + 1);
//
	//	int metaXneg = world.getBlockState(x - 1, y, z);
	//	int metaXpos = world.getBlockState(x + 1, y, z);
	//	int metaZneg = world.getBlockState(x, y, z - 1);
	//	int metaZpos = world.getBlockState(x, y, z + 1);
//
	//	final boolean flagXneg = this.canConnectWallTo(world, x - 1, y, z) || (idXneg instanceof BlockStairs && (metaXneg & 3) == 0);
	//	final boolean flagXpos = this.canConnectWallTo(world, x + 1, y, z) || (idXpos instanceof BlockStairs && (metaXpos & 3) == 1);
	//	final boolean flagZneg = this.canConnectWallTo(world, x, y, z - 1) || (idZneg instanceof BlockStairs && (metaZneg & 3) == 2);
	//	final boolean flagZpos = this.canConnectWallTo(world, x, y, z + 1) || (idZpos instanceof BlockStairs && (metaZpos & 3) == 3);
//
	//	float x1 = 0.375F;
	//	float x2 = 0.625F;
	//	float z1 = 0.375F;
	//	float z2 = 0.625F;

	//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
	//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);

	//	//XNEG
		//if (flagXneg)
		//{
		//	x1 = 0.0F;
		//	x2 = 0.375F;
		//	z1 = 0.375F;
		//	z2 = 0.625F;

		//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
		//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
		//}
		//else if (idXneg instanceof BlockDoor)
		//{
			//if ((metaXneg & 8) > 7)
		//	{
			//	metaXneg = world.getBlockState(x - 1, y - 1, z);
			//}

			//tm = metaXneg & 3;

			//if (tm == 1 || tm == 3)
			//{
			//	x1 = 0.0F;
			//	x2 = 0.375F;
			//	z1 = 0.375F;
			//	z2 = 0.625F;

			//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
			//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);

			//	if (tm == 1)
			//	{

				//	x1 = 0.0F;
				//	x2 = 0.25F;
				//	z1 = 0.0F;
				//	z2 = 0.375F;

				//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
				//}

				//if (tm == 3)
				//{

					//x1 = 0.0F;
					//x2 = 0.25F;
					//z1 = 0.625F;
					//z2 = 1.0F;

					//this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
					//super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
				//}
			//}
		//}

		//XPOS
		//if (flagXpos)
		//{
			//x1 = 0.625F;
			//x2 = 1.0F;
			//z1 = 0.375F;
			//z2 = 0.625F;

			//this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
			//super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
		//}
		//else if (idXpos instanceof BlockDoor)
		//{
			//if ((metaXpos & 8) > 7)
			//{
			//	metaXpos = world.getBlockState(x + 1, y - 1, z);
			//}

			//tm = metaXpos & 3;

			//if (tm == 1 || tm == 3)
			//{
				//x1 = 0.625F;
				//x2 = 1.0F;
				//z1 = 0.375F;
				//z2 = 0.625F;

				//this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);

				//if (tm == 1)
				//{
				//	x1 = 0.75F;
				//	x2 = 1.0F;
				//	z1 = 0.0F;
				//	z2 = 0.375F;

				//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
				//}

				//if (tm == 3)
				//{
				//	x1 = 0.75F;
				//	x2 = 1.0F;
				//	z1 = 0.625F;
				//	z2 = 1.0F;

				//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
				//}
			//}
		//}

		//ZNEG
		//if (flagZneg)
		//{
//
		//	x1 = 0.375F;
		//	x2 = 0.625F;
		//	z1 = 0.0F;
		//	z2 = 0.375F;

		//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
		//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
		//}
		//else if (idZneg instanceof BlockDoor)
		//{
		//	if ((metaZneg & 8) > 7)
		//	{
		//		metaZneg = world.getBlockState(x, y - 1, z - 1);
		//	}

		//	tm = metaZneg & 3;

		//	if (tm == 0 || tm == 2)
		//	{
			//	x1 = 0.375F;
			//	x2 = 0.625F;
			//	z1 = 0.0F;
			//	z2 = 0.375F;

			//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
			//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);

				//if (tm == 0)
				//{
				//	x1 = 0.0F;
				//	x2 = 0.375F;
				//	z1 = 0.0F;
				//	z2 = 0.25F;
//
				//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
				//}

				//if (tm == 2)
				//{
				//	x1 = 0.625F;
				//	x2 = 1.0F;
				//	z1 = 0.0F;
				//	z2 = 0.25F;
//
				//	this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
				//	super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
			//	}
			//}
		//}

		//ZPOS
//		if (flagZpos)
//		{
//			x1 = 0.375F;
//			x2 = 0.625F;
//			z1 = 0.625F;
//			z2 = 1.0F;
//
//			this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
//			super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
//		}
//		else if (idZpos instanceof BlockDoor)
//
//	{
//		if ((metaZpos & 8) > 7) {
//			metaZpos = world.getBlockState(x, y - 1, z + 1);
//		}
//
//		tm = metaZpos & 3;
//
//		if (tm == 0 || tm == 2) {
//			x1 = 0.375F;
//			x2 = 0.625F;
//			z1 = 0.625F;
//			z2 = 1.0F;
//
//			this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
//			super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
//
//			if (tm == 0) {
//				x1 = 0.0F;
//				x2 = 0.375F;
//				z1 = 0.75F;
//				z2 = 1.0F;
//
//				this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
//				super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
//		}
//
//			if (tm == 2) {
//				x1 = 0.625F;
//				x2 = 1.0F;
//				z1 = 0.75F;
//				z2 = 1.0F;
//
//				this.getBoundingBox(x1, 0.0F, z1, x2, 1.0F, z2);
//				super.getCollisionBoundingBox(world, x, y, z, axis, list, entity);
//			}
//		}
//		}
//
//		this.setBlockBoundsBasedOnState(world, x, y, z);
//	}
//}
}