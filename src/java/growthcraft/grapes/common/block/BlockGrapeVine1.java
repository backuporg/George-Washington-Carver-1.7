package growthcraft.grapes.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.client.renderer.RenderGrapeVine1;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockGrapeVine1 extends BlockGrapeVineBase {
	public boolean graphicFlag;

	@SideOnly(Side.CLIENT)


	public BlockGrapeVine1() {
		super();
		setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineTrunkGrowthRate);
		setTickRandomly(true);
		setHardness(2.0F);
		setResistance(5.0F);
		//setStepSound(soundTypeWood);
		setUnlocalizedName("grc.grapeVine1");
		setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	protected boolean canUpdateGrowth(World world, BlockPos pos) {
		return world.getBlockState(pos) == 0 || world.isAirBlock(x, y + 1, z);
	}

	@Override
	protected void doGrowth(World world, BlockPos pos, int meta) {
		final Block above = world.getBlockState(x, y + 1, z);
		/* Is there a rope block above this? */
		if (BlockCheck.isRope(above)) {
			incrementGrowth(world, pos, meta);
			world.setBlockState(x, y + 1, z, GrowthCraftGrapes.blocks.grapeLeaves.getBlockState(), 0, BlockFlags.UPDATE_AND_SYNC);
		} else if (world.isAirBlock(x, y + 1, z)) {
			incrementGrowth(world, pos, meta);
			world.setBlockState(x, y + 1, z, this, 0, BlockFlags.UPDATE_AND_SYNC);
		} else if (GrowthCraftGrapes.blocks.grapeLeaves.getBlockState() == above) {
			incrementGrowth(world, pos, meta);
		}
	}

	@Override
	protected float getGrowthRate(World world, BlockPos pos) {
		int j = y;
		if (world.getBlockState(x, j - 1, z) == this && world.getBlockState(x, j - 2, z) == Blocks.FARMLAND) {
			j = y - 1;
		}
		return super.getGrowthRate(world, x, j, z);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos) {
		return BlockCheck.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, this) ||
				this == world.getBlockState(x, y - 1, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos) {
		return GrowthCraftGrapes.items.grapeSeeds.getItem();
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	this.icons = new IIcon[3];
//
	//	icons[0] = reg.registerIcon("grcgrapes:trunk");
	//	icons[1] = reg.registerIcon("grcgrapes:leaves");
	//	icons[2] = reg.registerIcon("grcgrapes:leaves_opaque");
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//
	//{
	//	return icons[0];
	//}

	//@SideOnly(Side.CLIENT)
	////public IIcon getLeafTexture()
	//{
	//	graphicFlag = Blocks.LEAVES.isOpaqueCube();
	//	return !this.graphicFlag ? icons[1] : icons[2];
	//}

	/************
	 * RENDER
	 ************/
	//@Override
	//public int getRenderType() {
	//	return RenderGrapeVine1.id;
	//}

	/************
	 * BOXES
	 ************/
	//@Override
	//@SuppressWarnings({"rawtypes", "unchecked"})
	//public void getCollisionBoundingBox(World world, BlockPos pos, AxisAlignedBB aabb, List list, Entity entity)
	//{
	//	final int meta = world.getBlockState(pos);
	//	final float f = 0.0625F;

	//if (meta == 0)
	//{
	//	this.getBoundingBox(6*f, 0.0F, 6*f, 10*f, 0.5F, 10*f);
	//	super.getCollisionBoundingBox(world, pos, aabb, list, entity);
	//	this.getBoundingBox(4*f, 0.5F, 4*f, 12*f, 1.0F, 12*f);
	//	super.getCollisionBoundingBox(world, pos, aabb, list, entity);
	//}
	//else if (meta == 1)
	//{
	//	this.getBoundingBox(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
	//	super.getCollisionBoundingBox(world, pos, aabb, list, entity);
	//}

	//this.setBlockBoundsBasedOnState(world, pos);
	//}
	//@Override
	//public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		//final int meta = world.getBlockState(pos);
		//final float f = 0.0625F;
//
		//if (meta == 0) {
		//	this.getBoundingBox(4 * f, 0.0F, 4 * f, 12 * f, 1.0F, 12 * f);
		//} else
		//{
		//	this.getBoundingBox(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
		//}
}
