package growthcraft.hops.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.IBlockRope;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.hops.GrowthCraftHops;
import growthcraft.hops.client.renderer.RenderHops;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public abstract class BlockHops extends GrcBlockBase implements IBlockRope, IPlantable, ICropDataProvider, IGrowable
{
	public static class HopsStage
	{
		public static final int BINE = 0;
		public static final int SMALL = 1;
		public static final int BIG = 2;
		public static final int FRUIT = 3;

		private HopsStage() {}
	}

	public static Boolean graphicFlag;

	@SideOnly(Side.CLIENT)


	private final float hopVineGrowthRate = GrowthCraftHops.getConfig().hopVineGrowthRate;
	private final float hopVineFlowerSpawnRate = GrowthCraftHops.getConfig().hopVineFlowerSpawnRate;

	public BlockHops()
	{
		super(Material.PLANTS);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.hopVine");
		this.setCreativeTab(null);
	}

	public boolean isMature(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		final int meta = world.getBlockState(state);
		return meta >= HopsStage.FRUIT;
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)HopsStage.FRUIT;
	}

	protected void incrementGrowth(World world, BlockPos pos, int meta)
	{
		final int previousMetadata = meta;
		++meta;
		world.setBlockState(pos, meta, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, previousMetadata);
	}

	public void spreadLeaves(World world, BlockPos pos)
	{
		world.setBlockState(x, y + 1, z, this, HopsStage.SMALL, BlockFlags.UPDATE_AND_SYNC);
	}

	public boolean canSpreadLeaves(World world, BlockPos pos)
	{
		return BlockCheck.isRope(world.getBlockState(x, y + 1, z)) && this.canBlockStay(world, x, y + 1, z);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random random)
	{
		if (!this.canBlockStay(world, pos))
		{
			world.setBlockState(pos, GrowthCraftCore.blocks.ropeBlock.getBlockState());
		}
		else
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final int meta = world.getBlockState(pos);
			final float f = this.getGrowthRateLoop(world, pos);

			if (meta < HopsStage.BIG)
			{
				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.hopVineGrowthRate / f) + 1) == 0))
				{
					incrementGrowth(world, x, y, z, meta);
				}
			}
			else if ((meta >= HopsStage.BIG) && canSpreadLeaves(world, x, y, z))
			{
				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.hopVineGrowthRate / f) + 1) == 0))
				{
					spreadLeaves(world, x, y, z);
				}
			}
			else
			{
				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.hopVineFlowerSpawnRate / f) + 1) == 0))
				{
					incrementGrowth(world, x, y, z, meta);
				}
			}
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		final int meta = world.getBlockState(x, y, z);
		return (meta < HopsStage.FRUIT) || canSpreadLeaves(world, x, y, z);
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos)
	{
		final int meta = world.getBlockState(x, y, z);
		if (meta < HopsStage.BIG)
		{
			incrementGrowth(world, x, y, z, meta);
		}
		else if (meta >= HopsStage.BIG && canSpreadLeaves(world, x, y, z))
		{
			spreadLeaves(world, x, y, z);
		}
		else
		{
			incrementGrowth(world, x, y, z, meta);
		}
	}

	private float getGrowthRateLoop(World world, BlockPos pos)
	{
		if (BlockCheck.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, this))
		{
			return getGrowthRate(world, x, y, z);
		}
		else
		{
			for (int loop = 1; loop < 5; ++loop)
			{
				if (world.getBlockState(x, y - loop, z) != this)
				{
					return getGrowthRate(world, x, y, z);
				}

				if (isVineRoot(world, x, y - loop, z))
				{
					return getGrowthRate(world, x, y - loop, z);
				}
			}

			return getGrowthRate(world, x, y, z);
		}
	}

	private float getGrowthRate(World world, BlockPos pos)
	{
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
		float f = 1.0F;

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				final Block block = BlockCheck.getFarmableBlock(world, l2, y - 1, i3, EnumFacing.UP, this);
				float f1 = 0.0F;

				if (block != null)
				{
					f1 = 1.0F;

					if (block.isFertile(world, l2, y - 1, i3))
					{
						f1 = 3.0F;
					}
				}

				if (l2 != x || i3 != z)
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

	public void removeFruit(World world, BlockPos pos)
	{
		world.setBlockState(x, y, z, HopsStage.BIG, BlockFlags.UPDATE_AND_SYNC);
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int EnumFacing, float par7, float par8, float par9)
	{
		if (world.getBlockState(x, y, z) >= HopsStage.FRUIT)
		{
			if (!world.isRemote)
			{
				removeFruit(world, x, y, z);
				dropBlockAsItem(world, x, y, z, GrowthCraftHops.items.hops.asStack(1 + world.rand.nextInt(8)));
			}
			return true;
		}
		return false;
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (BlockCheck.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, this))
		{
			return true;
		}
		else
		{
			int loop = 1;

			while (loop < 5)
			{
				if (world.getBlockState(x, y - loop, z) != this)
				{
					return false;
				}

				if (isVineRoot(world, x, y - loop, z))
				{
					return true;
				}
				loop++;
			}

			return false;
		}
	}

	private boolean isVineRoot(World world, BlockPos pos)
	{
		return world.getBlockState(x, y, z) == this &&
			BlockCheck.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, this) &&
			world.getBlockState(x, y, z) >= HopsStage.BIG;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		final int meta = world.getBlockState(pos);
		return meta < HopsStage.FRUIT ? GrowthCraftHops.items.hopSeeds.getItem() : GrowthCraftHops.items.hops.getItem();
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
	{
		return false;
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, BlockPos pos)
	{
		if (world.getBlockState(pos) instanceof IBlockRope)
		{
			return true;
		}
		return false;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(GrowthCraftCore.items.rope.asStack());
		if (world.getBlockState(pos) >= HopsStage.BIG)
		{
			ret.add(GrowthCraftHops.items.hops.asStack(1 + world.rand.nextInt(8)));
		}
		return ret;
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	icons = new IIcon[7];
//
	//	icons[0] = reg.registerIcon("grchops:leaves");
	//	icons[1] = reg.registerIcon("grchops:leaves_opaque");
	//	icons[2] = reg.registerIcon("grchops:bine");
	//	icons[3] = reg.registerIcon("grchops:leaves_hops");
	//	icons[4] = reg.registerIcon("grccore:rope_1");
	//	icons[5] = reg.registerIcon("grchops:leaves_x");
	//	icons[6] = reg.registerIcon("grchops:leaves_opaque_x");
	//}

	//@SideOnly(Side.CLIENT)
	//public IIcon getIconByIndex(int index)
	//{
	//	return icons[index];
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public IIcon getIcon(int side, int meta)
	//{
		//if (meta != 0)
		//{
		//	graphicFlag = !Blocks.LEAVES.isOpaqueCube();
		//	if (meta >= HopsStage.FRUIT)
		//	{
		//		return graphicFlag ? icons[5] : icons[6];
		//	}
		//	else
		//	{
		//		return graphicFlag ? icons[0] : icons[1];
		//	}
		//}
		//return this.icons[2];
	//}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderHops.id;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/************
	 * COLORS
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		final double d0 = 0.5D;
		final double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public int colorMultiplier(IBlockAccess world, BlockPos pos)
	//{
	//	final int meta = world.getBlockState(pos);
//
	//	int r = 0;
	//	int g = 0;
	//	int b = 0;
//
	//	for (int l1 = -1; l1 <= 1; ++l1)
	//	{
	//		for (int i2 = -1; i2 <= 1; ++i2)
	//		{
	//			final int j2 = world.getBiome(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
	//			r += (j2 & 16711680) >> 16;
	//			g += (j2 & 65280) >> 8;
	//			b += j2 & 255;
	//		}
	//	}
//
	//	return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	//}

	/************
	 * BOXES
	 ************/
//	@Override
	//@SuppressWarnings({"rawtypes", "unchecked"})
//	public void getCollisionBoundingBox(World world, BlockPos pos, AxisAlignedBB aabb, List list, Entity entity)
	//{
	//	final boolean flag = this.canConnectRopeTo(world, x, y, z - 1);
//		final boolean flag1 = this.canConnectRopeTo(world, x, y, z + 1);
	//	final boolean flag2 = this.canConnectRopeTo(world, x - 1, y, z);
	//	final boolean flag3 = this.canConnectRopeTo(world, x + 1, y, z);
	//	final boolean flag4 = this.canConnectRopeTo(world, x, y - 1, z);
//		final boolean flag5 = this.canConnectRopeTo(world, x, y + 1, z);
//		float f = 0.4375F;
	//	float f1 = 0.5625F;
	//	float f2 = 0.4375F;
	//	float f3 = 0.5625F;
	//	float f4 = 0.4375F;
	//	float f5 = 0.5625F;
//
	//	if (flag)
	//	{
	//		f2 = 0.0F;
	//	}
//
	//	if (flag1)
	//	{
	//		f3 = 1.0F;
	//	}
//
	//	if (flag || flag1)
	//	{
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, x, y, z, aabb, list, entity);
	//	}
//
	//	f2 = 0.4375F;
	//	f3 = 0.5625F;
//
	//	if (flag2)
	//	{
	//		f = 0.0F;
	//	}
//
	//	if (flag3)
	//	{
	//		f1 = 1.0F;
	//	}
//
	//	if (flag2 || flag3)
	//	{
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, x, y, z, aabb, list, entity);
	//	}
///
	//	f = 0.4375F;
	//	f1 = 0.5625F;
//
	//	if (flag4)
	//	{
	//		f4 = 0.0F;
		//}
//
		///if (flag5)
		///{
		//	f5 = 1.0F;
		//}
//
	//	if (flag4 || flag5)
	//	{
	//		this.getBoundingBox(f, f4, f2, f1, f5, f3);
	//		super.getCollisionBoundingBox(world, x, y, z, aabb, list, entity);
	//	}
//
	//	this.setBlockBoundsBasedOnState(world, x, y, z);
	//}

	//@Override
///	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	//{
	//	final int meta = world.getBlockState(x, y, z);
	//	final float f = 0.0625F;
//
	//	switch (meta)
	//	{
	//		case 0:
	//			this.getBoundingBox(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
	//			break;
	//		case 1:
	//			this.getBoundingBox(4*f, 0.0F, 4*f, 12*f, 8*f, 12*f);
	//			break;
	//		default:
	//			this.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	//			break;
	//	}
	//}

	/************
	 * IPLANTABLE
	 ************/
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos);
	}
}
