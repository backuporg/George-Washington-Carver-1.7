package growthcraft.core.common.block;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.client.renderer.RenderRope;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockRope extends GrcBlockBase implements IBlockRope {
	@SideOnly(Side.CLIENT)


	public BlockRope() {
		super(Material.CLOTH);
		this.setHardness(0.5F);
		//this.setStepSound(soundWoodFootstep);
		this.setUnlocalizedName("grc.ropeBlock");
		this.setCreativeTab(null);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborChange(World world, BlockPos pos, Block block) {
		this.checkBlockCoordValid(world, pos);
	}

	protected final void checkBlockCoordValid(World world, BlockPos pos) {
		if (!this.canBlockStay(world, pos)) {
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockToAir(pos);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		if (this.canConnectRopeTo(world, x, y, z - 1)) return true;
		if (this.canConnectRopeTo(world, x, y, z + 1)) return true;
		if (this.canConnectRopeTo(world, x - 1, y, z)) return true;
		if (this.canConnectRopeTo(world, x + 1, y, z)) return true;
		if (this.canConnectRopeTo(world, x, y - 1, z)) return true;
		if (this.canConnectRopeTo(world, x, y + 1, z)) return true;
		return false;
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos) {
		return this.canPlaceBlockAt(world, pos);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos) {
		return GrowthCraftCore.items.rope.getItem();
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos) instanceof IBlockRope;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3) {
		return GrowthCraftCore.items.rope.getItem();
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)
//
	//{
	//	this.icons = new IIcon[2];
//
	//	icons[0] = reg.registerIcon("grccore:rope_1");
	//	icons[1] = reg.registerIcon("grccore:rope");
	//}

	//@SideOnly(Side.CLIENT)
	////public IIcon getIconByIndex(int index) {
	//	return icons[index];
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	// {
	//	return this.icons[1];
	//}

	///************
	// * RENDER
	// ************/
	//@Override
	//public int getRenderType() {
	//	return RenderRope.id;
	//}

	//@Override
	//public boolean renderAsNormalBlock() {
	//	return false;
	//}

	//@Override
	//public boolean isOpaqueCube() {
	//	return false;
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int par5) {
	//	return true;
	//}

	//************
	// * BOXES
	// ************/
	//@Override
	//@SuppressWarnings({"rawtypes", "unchecked"})
	//public void getCollisionBoundingBox(World world, BlockPos pos, AxisAlignedBB aabb, List list, Entity entity) {
	//	final boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
	//	final boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
	//	final boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
	//	final boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
	//	final boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
	//	final boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
	//	float f = 0.4375F;
	//	float f1 = 0.5625F;
	//	float f2 = 0.4375F;
	//	float f3 = 0.5625F;
	//	float f4 = 0.4375F;
	//	float f5 = 0.5625F;
//
	//	if (flag) {
	//		f2 = 0.0F;
		//}
//
	//	if (flag1) {
	//		f3 = 1.0F;
	//	}
//
	//	if (flag || flag1) {
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, pos, aabb, list, entity);
	//	}
//
	//	f2 = 0.4375F;
	//	f3 = 0.5625F;
//
	//	if (flag2) {
	//		f = 0.0F;
	//	}
//
	//	if (flag3) {
	//		f1 = 1.0F;
	//	}
//
	//	if (flag2 || flag3) {
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, pos, aabb, list, entity);
	//	}
//
	//	f = 0.4375F;
	//	f1 = 0.5625F;
//
	//	if (flag4) {
	//		f4 = 0.0F;
	//	}
//
	//	if (flag5) {
	//		f5 = 1.0F;
	//	}
//
	//	if (flag4 || flag5) {
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, x, y, z, aabb, list, entity);
	//	}
//
//		this.setBlockBoundsBasedOnState(world, x, y, z);
//	}
//
//	@Override
//	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
//		final boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
//		final boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
//		final boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
//		final boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
//		final boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
//		final boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
//		float f = 0.4375F;
//		float f1 = 0.5625F;
//		float f2 = 0.4375F;
//		float f3 = 0.5625F;
//		float f4 = 0.4375F;
//		float f5 = 0.5625F;
//
//		if (flag) {
//			f2 = 0.0F;
//		}
//
//		if (flag1) {
//			f3 = 1.0F;
//		}
//
//		if (flag2) {
//			f = 0.0F;
//		}
//
//		if (flag3) {
//			f1 = 1.0F;
//		}
//
//		if (flag4) {
//			f4 = 0.0F;
//		}
//
//		if (flag5) {
//			f5 = 1.0F;
//			//}
//
			//this.getBoundingBox(f, f4, f2, f1, f5, f3);
		}