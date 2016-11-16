package growthcraft.cellar.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.client.render.RenderFermentBarrel;
import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.event.EventBarrelDrained;
import growthcraft.core.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFermentBarrel extends BlockCellarContainer
{
	@SideOnly(Side.CLIENT)


	public BlockFermentBarrel()
	{
		super(Material.WOOD);
		setTileEntityType(TileEntityFermentBarrel.class);
		setHardness(2.5F);
		//setStepSound(soundTypeWood);
		setUnlocalizedName("grc.fermentBarrel");
		setBlockTextureName("grccellar:ferment_barrel");
		setCreativeTab(GrowthCraftCellar.tab);
	}

	@Override
	protected boolean shouldRestoreBlockState(World world, BlockPos pos, ItemStack stack)
	{
		return true;
	}

	@Override
	protected boolean shouldDropTileStack(World world, BlockPos pos, int metadata, int fortune)
	{
		return true;
	}

	@Override
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	protected boolean playerDrainTank(World world, BlockPos pos, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		final FluidStack available = Utils.playerDrainTank(world, pos, tank, held, player);
		if (available != null && available.amount > 0)
		{
			GrowthCraftCellar.CELLAR_BUS.post(new EventBarrelDrained(player, world, pos, available));
			return true;
		}
		return false;
	}

	private void setDefaultDirection(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			final Block southBlock = world.getBlockState(x, y, z - 1);
			final Block northBlock = world.getBlockState(x, y, z + 1);
			final Block westBlock = world.getBlockState(x - 1, y, z);
			final Block eastBlock = world.getBlockState(x + 1, y, z);
			byte meta = 3;

			if (southBlock.isFullBlock() && !northBlock.isFullBlock())
			{
				meta = 3;
			}

			if (northBlock.isFullBlock() && !southBlock.isFullBlock())
			{
				meta = 2;
			}

			if (westBlock.isFullBlock() && !eastBlock.isFullBlock())
			{
				meta = 5;
			}

			if (eastBlock.isFullBlock() && !westBlock.isFullBlock())
			{
				meta = 4;
			}

			world.setBlockState(pos, state, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		setDefaultDirection(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack, IBlockState state)
	{
		super.onBlockPlacedBy(world, pos, entity, stack);
		final int meta = BlockPistonBase.determineOrientation(world, pos, entity);
		world.setBlockState(pos, state, BlockFlags.UPDATE_AND_SYNC);
	}

	//@Override
	//@SideOnly(Side.CLIENT)
//
	//{
//		this.icons = new IIcon[4];
//		final String basename = getTextureName();
//		icons[0] = reg.registerIcon(String.format("%s/minecraft/oak/side", basename));
//		icons[1] = reg.registerIcon(String.format("%s/minecraft/oak/side_alt", basename));
//		icons[2] = reg.registerIcon(String.format("%s/minecraft/oak/top", basename));
//		icons[3] = reg.registerIcon(String.format("%s/minecraft/oak/bottom", basename));
//	}
//
//	@SideOnly(Side.CLIENT)
//	//public IIcon getIconByIndex(int index)
	{
		return icons[index];
	}
//
//	@Override
//	@SideOnly(Side.CLIENT)
//
//	{
//		if (meta == 0 || meta == 1)
//		{
//			return side == 0 || side == 1 ? icons[1] : icons[0];
//		}
//		else if (meta == 2 || meta == 3)
//		{
//			return side == 2 || side == 3 ? icons[1] : icons[0];
//		}
//		else if (meta == 4 || meta == 5)
//		{
//			return side == 4 || side == 5 ? icons[1] : icons[0];
//		}
//		return icons[0];
//	}

	@Override
	public int getRenderType()
	{
		return RenderFermentBarrel.RENDER_ID;
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	/************
	 * COMPARATOR
	 ************/
	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos, int par5)
	{
		final TileEntityFermentBarrel te = getTileEntity(world, pos);
		if (te != null)
		{
			return te.getDeviceProgressScaled(15);
		}
		return 0;
	}

	@Override
	public boolean wrenchBlock(World world, BlockPos pos, EntityPlayer player, ItemStack wrench) {
		return false;
	}
}
