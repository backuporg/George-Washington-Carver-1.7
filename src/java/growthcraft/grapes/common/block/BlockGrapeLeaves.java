package growthcraft.grapes.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.block.IBlockRope;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.client.renderer.RenderGrapeLeaves;
import growthcraft.grapes.util.GrapeBlockCheck;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockGrapeLeaves extends BlockLeaves implements IBlockRope
{
	@SideOnly(Side.CLIENT)


	private final int grapeLeavesGrowthRate = GrowthCraftGrapes.getConfig().grapeLeavesGrowthRate;
	private final int grapeSpawnRate = GrowthCraftGrapes.getConfig().grapeSpawnRate;
	// how far can a grape leaf grow before it requires support from a trunk
	private final int grapeVineSupportedLength = GrowthCraftGrapes.getConfig().grapeVineSupportedLength;

	public BlockGrapeLeaves()
	{
		super(Material.LEAVES, false);
		setTickRandomly(true);
		setHardness(0.2F);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.grapeLeaves");
		setCreativeTab(null);
	}

	private boolean isTrunk(World world, BlockPos pos, IBlockState state)
	{
		return GrapeBlockCheck.isGrapeVineTrunk(world.getBlockState());
	}

	public boolean isSupportedByTrunk(World world, BlockPos pos)
	{
		return isTrunk(world, x, y - 1, z);
	}

	/**
	 * Use this method to check if the block can grow outwards on a rope
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowOutwardsOnRope(World world, BlockPos pos)
	{
		if (BlockCheck.isRope(world.getBlockState(x + 1, y, z))) return true;
		if (BlockCheck.isRope(world.getBlockState(x - 1, y, z))) return true;
		if (BlockCheck.isRope(world.getBlockState(x, y, z + 1))) return true;
		if (BlockCheck.isRope(world.getBlockState(x, y, z - 1))) return true;
		return false;
	}

	public boolean canGrowOutwards(World world, BlockPos pos)
	{
		final boolean leavesTotheSouth = world.getBlockState(x, y, z + 1) == this;
		final boolean leavesToTheNorth = world.getBlockState(x, y, z - 1) == this;
		final boolean leavesToTheEast = world.getBlockState(x + 1, y, z) == this;
		final boolean leavesToTheWest = world.getBlockState(x - 1, y, z) == this;

		if (!leavesTotheSouth && !leavesToTheNorth && !leavesToTheEast && !leavesToTheWest) return false;

		for (int i = 1; i <= grapeVineSupportedLength; ++i)
		{
			if (leavesTotheSouth && isTrunk(world, x, y - 1, z + i)) return true;
			if (leavesToTheNorth && isTrunk(world, x, y - 1, z - i)) return true;
			if (leavesToTheEast && isTrunk(world, x + i, y - 1, z)) return true;
			if (leavesToTheWest && isTrunk(world, x - i, y - 1, z)) return true;
		}
		return false;
	}

	/**
	 * Variation of canGrowOutwards, use this method to check rope blocks
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowHere(World world, BlockPos pos)
	{
		if (BlockCheck.isRope(world.getBlockState(x, y, z)))
		{
			return canGrowOutwards(world, pos);
		}
		return false;
	}

	private void setGrapeBlock(World world, BlockPos pos)
	{
		world.setBlockState(x, y, z, GrowthCraftGrapes.blocks.grapeBlock.getBlockState(), 0, BlockFlags.UPDATE_AND_SYNC);
	}

	public boolean growGrapeBlock(World world, BlockPos pos)
	{
		if (world.isAirBlock(x, y - 1, z))
		{
			if (!world.isRemote)
			{
				setGrapeBlock(world, x, y - 1, z);
			}
			return true;
		}
		return false;
	}

	private void grow(World world, BlockPos pos, Random random)
	{
		if (world.isAirBlock(x, y - 1, z) && (random.nextInt(this.grapeSpawnRate) == 0))
		{
			setGrapeBlock(world, x, y - 1, z);
		}

		if (world.rand.nextInt(this.grapeLeavesGrowthRate) == 0)
		{
			if (canGrowOutwards(world, pos))
			{
				final EnumFacing EnumFacing = BlockCheck.DIR4[random.nextInt(4)];

				if (canGrowHere(world, x + EnumFacing.offsetX, y, z + EnumFacing.offsetZ))
				{
					world.setBlockState(x + EnumFacing.offsetX, y, z + EnumFacing.offsetZ, this, 0, BlockFlags.UPDATE_AND_SYNC);
				}
			}
		}
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random random)
	{
		if (!this.canBlockStay(world, pos))
		{
			world.setBlockState(pos, (IBlockState) GrowthCraftCore.blocks.ropeBlock.getBlockState());
		}
		else
		{
			grow(world, pos, random);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random)
	{
		if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
		{
			final double d0 = (double)((float)x + random.nextFloat());
			final double d1 = (double)y - 0.05D;
			final double d2 = (double)((float)z + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (this.isSupportedByTrunk(world, pos))
		{
			return true;
		}
		else
		{
			for (EnumFacing EnumFacing : BlockCheck.DIR4)
			{
				for (int i = 1; i <= grapeVineSupportedLength; ++i)
				{
					final int bx = pos.getX() + EnumFacing.offsetX * i;
					final int bz = pos.getZ() + EnumFacing.offsetZ * i;
					if (world.getBlockState(pos) != this)
					{
						break;
					}
					else if (isSupportedByTrunk(world, pos))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapeSeeds.getItem();
	}

	@Override
	public boolean isLeaves(IBlockAccess world, BlockPos pos)
	{
		return true;
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
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return GrowthCraftCore.items.rope.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return null;
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)

	//{
	//	this.icons = new IIcon[4];
//
	//	icons[0] = reg.registerIcon("grcgrapes:leaves");
	//	icons[1] = reg.registerIcon("grcgrapes:leaves_opaque");
	//	icons[2] = reg.registerIcon("grccore:rope_1");
	//	icons[3] = reg.registerIcon("grcgrapes:leaves_half");
	//}

	//@SideOnly(Side.CLIENT)
	//public IIcon getIconByIndex(int index)
	//{
	//	return icons[index];
	//}

	//@Override
	///@SideOnly(Side.CLIENT)
	//
	//{
	//	return icons[isOpaqueCube() ? 1 : 0];
	//}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderGrapeLeaves.id;
	}

	//@Override
	//public boolean isOpaqueCube()
	//{
	//	return Blocks.LEAVES.isOpaqueCube();
	//}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
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

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos)
	{
		final IBlockState meta = world.getBlockState(pos);

		int r = 0;
		int g = 0;
		int b = 0;

		for (int l1 = -1; l1 <= 1; ++l1)
		{
			for (int i2 = -1; i2 <= 1; ++i2)
			{
				final int j2 = world.getBiome(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
				r += (j2 & 16711680) >> 16;
				g += (j2 & 65280) >> 8;
				b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return null;
	}
}
