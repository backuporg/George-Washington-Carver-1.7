package growthcraft.apples.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.client.renderer.RenderBlockFruit;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockApple extends GrcBlockBase implements IGrowable, ICropDataProvider
{

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

	}

	public static class AppleStage
	{
		public static final int YOUNG = 0;
		public static final int MID = 1;
		public static final int MATURE = 2;
		public static final int COUNT = 3;

		private AppleStage() {}
	}

	public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, 3);

	@SideOnly(Side.CLIENT)


	private final int growth = GrowthCraftApples.getConfig().appleGrowthRate;
	private final boolean dropRipeApples = GrowthCraftApples.getConfig().dropRipeApples;
	private final int dropChance = GrowthCraftApples.getConfig().appleDropChance;

	public BlockApple()
	{
		super(Material.PLANTS);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setResistance(5.0F);
		//setStepSound(soundTypeWood);
		this.setUnlocalizedName("grc.appleBlock");
		this.setCreativeTab(null);
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)AppleStage.MATURE;
	}

	void incrementGrowth(World world, BlockPos pos, IBlockState state, int meta)
	{
		world.setBlockState(pos, state, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	/* IGrowable interface
	 *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */

	/* Can this accept bonemeal? */
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return state.getValue(GROWTH) < AppleStage.MATURE;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos, int meta)
	{
		incrementGrowth(world, pos, world.getBlockState(pos), meta);
	}

	/************
	 * TICK
	 ************/
	public void updateTick(World world, BlockPos pos, Random random, IBlockState state)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
		else
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final boolean continueGrowth = random.nextInt(this.growth) == 0;
			if (allowGrowthResult == Event.Result.ALLOW || continueGrowth)
			{
				final int meta = state.getValue(GROWTH);
				if (meta < AppleStage.MATURE)
				{
					incrementGrowth(world, pos, state, meta);
				}
				else if (dropRipeApples && world.rand.nextInt(this.dropChance) == 0)
				{
					fellBlockAsItem(world, pos);
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int EnumFacing, float par7, float par8, float par9, IBlockState state)
	{
		if (state.getValue(GROWTH) >= AppleStage.MATURE)
		{
			if (!world.isRemote)
			{
				fellBlockAsItem(world, pos);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	public boolean canBlockStay(World world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos.up());
		return GrowthCraftApples.blocks.appleLeaves.equals(state.getBlock());
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Items.APPLE;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int meta, Random random, int par3)
	{
		return meta >= AppleStage.MATURE ? Items.APPLE : null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, pos, state, par6, 0);
	}


	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBlockFruit.id;
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
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		state = world.getBlockState(pos);
		final int meta = state.getValue(GROWTH);
		final float f = 0.0625F;
		if (meta == AppleStage.YOUNG)
		{
			this.getBoundingBox(state, world, pos);
		}
		else if (meta == AppleStage.MID)
		{
			this.getBoundingBox(state, world, pos);
		}
		else
		{
			this.getBoundingBox(state, world, pos);
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
	{
		this.setBlockBoundsBasedOnState(world, pos, state);
		return super.getCollisionBoundingBox(state, world, pos);
	}
}
