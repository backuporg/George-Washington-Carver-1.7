package growthcraft.cellar.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.client.render.RenderFruitPresser;
import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.core.common.block.IRotatableBlock;
import growthcraft.core.common.block.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFruitPresser extends BlockCellarContainer implements IWrenchable, IRotatableBlock
{
	@SideOnly(Side.CLIENT)


	public BlockFruitPresser(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		super(Material.PISTON);
		this.isBlockContainer = true;
		setTileEntityType(TileEntityFruitPresser.class);
		setHardness(0.5F);
		setStepSound(soundTypePiston);
		setUnlocalizedName("grc.fruitPresser");
		setCreativeTab(null);
		getBoundingBox(state, source, pos);
	}

	public String getPressStateName(int meta)
	{
		switch(meta)
		{
			case 0:
			case 1:
				return "unpressed";
			case 2:
			case 3:
				return "pressed";
			default:
				return "invalid";
		}
	}

	/************
	 * TRIGGERS
	 ************/

	/* IRotatableBLock */
	@Override
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		final Block below = world.getBlockState(x, y - 1, z);
		if (below instanceof IRotatableBlock)
		{
			return ((IRotatableBlock)below).isRotatable(world, x, y - 1, z, side);
		}
		return false;
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing side)
	{
		if (isRotatable(world, pos, side))
		{
			final Block below = world.getBlockState(x, y - 1, z);
			return below.rotateBlock(world, x, y - 1, z, side);
		}
		return false;
	}

	/* IWrenchable */
	@Override
	public boolean wrenchBlock(World world, BlockPos pos, EntityPlayer player, ItemStack wrench)
	{
		final Block below = world.getBlockState(x, y - 1, z);
		if (below instanceof BlockFruitPress)
		{
			return ((BlockFruitPress)below).wrenchBlock(world, x, y - 1, z, player, wrench);
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote) return true;
		final Block below = world.getBlockState(x, y - 1, z);
		if (below instanceof BlockFruitPress)
		{
			return ((BlockFruitPress)below).tryWrenchItem(player, world, x, y - 1, z);
		}
		return false;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState oldState, IBlockState newState, int flags, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		final int m = world.getBlockState(x,  y - 1, z);
		world.setBlockState(pos, state, BlockFlags.UPDATE_AND_SYNC);

		if (!world.isRemote)
		{
			this.updatePressState(world, pos, oldState, newState, flags);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack, IBlockState oldState, IBlockState newState, int flags)
	{
		final int m = world.getBlockState(x,  y - 1, z);
		world.setBlockState(x, y, z, m, BlockFlags.UPDATE_AND_SYNC);

		if (!world.isRemote)
		{
			this.updatePressState(world, pos, oldState, newState, flags);
		}
	}

	@Override
	public void onNeighborChange(World world, BlockPos pos, Block block, IBlockState oldState, IBlockState newState, int flags)
	{
		if (!this.canBlockStay(world, pos))
		{
			world.destroyBlock(pos, true);
		}

		if (!world.isRemote)
		{
			this.updatePressState(world, pos, oldState, newState, flags);
		}
	}

	private void updatePressState(World world, BlockPos pos, IBlockState newState, IBlockState oldState, int flags)
	{
		final int     meta = world.getBlockState(pos);
		final boolean flag = world.isBlockIndirectlyGettingPowered(pos);

		if (flag && (meta == 0 || meta == 1))
		{
			world.setBlockState(x, y, z, meta | 2, BlockFlags.UPDATE_AND_SYNC);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
		}
		else if (!flag && (meta == 2 || meta == 3))
		{
			world.setBlockState(x, y, z, meta & 1, BlockFlags.UPDATE_AND_SYNC);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.in", 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
		}

		world.notifyBlockUpdate(pos, oldState, newState, flags);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return GrowthCraftCellar.blocks.fruitPress.getBlockState() == world.getBlockState(x, y - 1, z);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		final int meta = world.getBlockState();

		if (meta == 0 || meta == 2)
		{
			return side == EnumFacing.EAST || side == EnumFacing.WEST;
		}
		else if (meta == 1 || meta == 3)
		{
			return side == EnumFacing.NORTH || side == EnumFacing.SOUTH;
		}

		return isNormalCube(world, pos);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftCellar.blocks.fruitPress.getItem();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	//@Override
	//@SideOnly(Side.CLIENT)
//
	//{
	//	icons = new IIcon[4];
//
	//	icons[0] = reg.registerIcon("grccellar:fruitpresser_0");
	//	icons[1] = reg.registerIcon("grccellar:fruitpresser_1");
	//	icons[2] = reg.registerIcon("grccellar:fruitpresser_2");
	//	icons[3] = reg.registerIcon("planks_oak");
	//}

	//@SideOnly(Side.CLIENT)
	////public IIcon getIconByIndex(int index)
	//{
	//	return icons[index];
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//
	//{
	//	return side == 0 ? icons[0] : (side == 1 ? icons[1] : icons[2]);
	//}

	/************
	 * RENDERS
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderFruitPresser.RENDER_ID;
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
}
