package growthcraft.grapes.block;

import growthcraft.core.utils.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This is the Grape Vine sapling block
 */
public class BlockGrapeVine0 extends BlockGrapeVineBase
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	public BlockGrapeVine0()
	{
		super();
		this.setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineSeedlingGrowthRate);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setStepSound(soundTypeGrass);
		this.setBlockName("grc.grapeVine0");
		this.setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	protected boolean canUpdateGrowth(World world, int x, int y, int z)
	{
		return world.getBlockLightValue(x, y + 1, z) >= 9;
	}

	@Override
	protected void doGrowth(World world, int x, int y, int z, int meta)
	{
		if (meta == 0)
		{
			incrementGrowth(world, x, y, z, meta);
		}
		else
		{
			world.setBlock(x, y, z, GrowthCraftGrapes.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_CLIENT);
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, int x, int y, int z)
	{
		return GrowthCraftGrapes.grapeSeeds.getItem();
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[2];

		tex[0] = reg.registerIcon("grcgrapes:vine_0");
		tex[1] = reg.registerIcon("grcgrapes:vine_1");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.tex[MathHelper.clamp_int(meta, 0, 1)];
	}

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return 1;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
		}
		else
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
