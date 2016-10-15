package growthcraft.grapes.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.RenderType;
import growthcraft.grapes.GrowthCraftGrapes;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is the Grape Vine sapling block
 */
public class BlockGrapeVine0 extends BlockGrapeVineBase
{
	@SideOnly(Side.CLIENT)


	public BlockGrapeVine0()
	{
		super();
		setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineSeedlingGrowthRate);
		setTickRandomly(true);
		setHardness(0.0F);
		setSoundType(SoundType.PLANT);
		setBlockName("grc.grapeVine0");
		setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	protected boolean canUpdateGrowth(World world, BlockPos pos)
	{
		return world.getBlockLightValue(x, y + 1, z) >= 9;
	}

	@Override
	protected void doGrowth(World world, BlockPos pos, int meta)
	{
		if (meta == 0)
		{
			incrementGrowth(world, pos, meta);
		}
		else
		{
			world.setBlockState(pos, GrowthCraftGrapes.blocks.grapeVine1.getBlockState(), 0, BlockFlags.UPDATE_AND_SYNC);
		}
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

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)

	//{
	//	this.icons = new IIcon[2];
//
	//	icons[0] = reg.registerIcon("grcgrapes:vine_0");
	//	icons[1] = reg.registerIcon("grcgrapes:vine_1");
	//}

	@Override
	@SideOnly(Side.CLIENT)
	//public IIcon getIcon(int side, int meta)
	//{
	//	return this.icons[MathHelper.clamp_int(meta, 0, 1)];
	//}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderType.BUSH;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockState(pos);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.getBoundingBox(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
		}
		else
		{
			this.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
