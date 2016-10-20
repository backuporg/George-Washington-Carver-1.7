package growthcraft.rice.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddyCrop;
import growthcraft.core.integration.AppleCore;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.client.renderer.RenderRice;
import growthcraft.rice.util.RiceBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRice extends GrcBlockBase implements IPaddyCrop, ICropDataProvider, IGrowable
{
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

	}

	public static class RiceStage
	{
		public static final int MATURE = 7;

		private RiceStage() {}
	}

	@SideOnly(Side.CLIENT)


	private final float growth = GrowthCraftRice.getConfig().riceGrowthRate;

	public BlockRice()
	{
		super(Material.PLANTS);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(null);
		this.setUnlocalizedName("grc.riceBlock");
		this.setStepSound(soundTypeGrass);
	}

	public boolean isMature(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockState(x, y, z);
		return meta >= RiceStage.MATURE;
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)RiceStage.MATURE;
	}

	private void incrementGrowth(World world, BlockPos pos, int meta, IBlockState state)
	{
		world.setBlockState(pos, state, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	private void growRice(World world, BlockPos pos, IBlockState state, int meta)
	{
		incrementGrowth(world, pos, meta, state);
		final Block paddyBlock = world.getBlockState(x, y - 1, z);
		if (RiceBlockCheck.isPaddy(paddyBlock))
		{
			((BlockPaddy)paddyBlock).drainPaddy(world, x, y - 1, z);
		}
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random random, IBlockState state)
	{
		this.checkCropChange(world, pos);

		if (world.getBlockLightValue(x, y + 1, z) >= 9 && world.getBlockState(x, y - 1, z) > 0)
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final int meta = world.getBlockState(pos);

			if (meta < RiceStage.MATURE)
			{
				final float f = this.getGrowthRate(world, pos);

				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.growth / f) + 1) == 0))
				{
					growRice(world, pos, state);
				}
			}
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		return world.getBlockState(pos) < RiceStage.MATURE;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos, IBlockState state)
	{
		final IBlockState meta = world.getBlockState((BlockPos) state);
		if (meta < RiceStage.MATURE)
		{
			growRice(world, pos, state);
		}
	}

	private float getGrowthRate(World world, BlockPos pos)
	{
		float f = 1.0F;
		final Block l = world.getBlockState(x, y, z - 1);
		final Block i1 = world.getBlockState(x, y, z + 1);
		final Block j1 = world.getBlockState(x - 1, y, z);
		final Block k1 = world.getBlockState(x + 1, y, z);
		final Block l1 = world.getBlockState(x - 1, y, z - 1);
		final Block i2 = world.getBlockState(x + 1, y, z - 1);
		final Block j2 = world.getBlockState(x + 1, y, z + 1);
		final Block k2 = world.getBlockState(x - 1, y, z + 1);
		final boolean flag = j1 == this || k1 == this;
		final boolean flag1 = l == this || i1 == this;
		final boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

		for (int loop_i = x - 1; loop_i <= x + 1; ++loop_i)
		{
			for (int loop_k = z - 1; loop_k <= z + 1; ++loop_k)
			{
				final Block soil = world.getBlockState(loop_i, y - 1, loop_k);
				float f1 = 0.0F;

				if (soil != null && RiceBlockCheck.isPaddy(soil))
				{
					f1 = 1.0F;

					if (world.getBlockState(loop_i, y - 1, loop_k) > 0)
					{
						f1 = 3.0F;
					}
				}

				if (loop_i != x || loop_k != z)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	protected final void checkCropChange(World world, BlockPos pos)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockToAir(pos);
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		this.checkCropChange(world, pos);
	}

	/************
	 * CONDITIONS
	 ************/

	/**
	 * @param block - block to place on
	 * @return can the rice be placed on this block?
	 */
	protected boolean canThisPlantGrowOnThisBlockID(Block block)
	{
		return RiceBlockCheck.isPaddy(block);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return (world.getFullBlockLightValue(x, y, z) >= 8 ||
			world.canBlockSeeTheSky(x, y, z)) &&
			this.canThisPlantGrowOnThisBlockID(world.getBlockState(x, y - 1, z));
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftRice.items.rice.getItem();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return GrowthCraftRice.items.rice.getItem();
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, pos, state, par6, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, IBlockState state, int metadata, int fortune)
	{
		final List<ItemStack> ret = super.getDrops(world, pos, state, fortune);

		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(15) <= metadata)
				{
					ret.add(GrowthCraftRice.items.rice.asStack(1));
				}
			}
		}

		return ret;
	}

	/************
	 * TEXTURE
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	icons = new IIcon[5];
//
	//	for (int i = 0; i < icons.length; ++i)
	//	{
	//		icons[i] = reg.registerIcon("grcrice:rice_" + i);
	//	}
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
//
	//{
	//	if (meta < 0 || meta > 7)
	//	{
	//		meta = 7;
	//	}
//
	//	int i = 0;
	//	switch (meta)
	//	{
	//		case 0: case 1: i = 0; break;
	//		case 2: case 3: i = 1; break;
	//		case 4: case 5: i = 2; break;
	//		case 6: case 7: i = 3; break;
	//		default: i = 2;
	//	}
///
	//	return icons[i];
	//}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderRice.id;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/************
	 * BOXES
	 ************/
	//@Override
	//public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	//{
	//	this.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	//}

	//@Override
	//public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
	}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, BlockPos pos)
	//{
	//	this.setBlockBoundsBasedOnState(world, pos);
	//	return super.getSelectedBoundingBoxFromPool(world, pos);
	//}
}
