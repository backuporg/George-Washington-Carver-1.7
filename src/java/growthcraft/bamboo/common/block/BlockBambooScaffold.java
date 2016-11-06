package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooScaffold;
import growthcraft.core.common.block.GrcBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBambooScaffold extends GrcBlockBase
{
	@SideOnly(Side.CLIENT)


	public BlockBambooScaffold()
	{
		super(Material.WOOD);
		////setStepSound(soundTypeWood);
		setResistance(0.2F);
		setHardness(0.5F);
		setUnlocalizedName("grc.bambooScaffold");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random random)
	{
		this.onNeighborChange(world, x, y, z, null);
	}

	/************
	 * TRIGGERS
	 ************/
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float float7, float float8, float float9)
	{
		final ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null)
		{
			if (itemstack.getItem() == Item.getItemFromBlock(this))
			{
				final int loop = world.getActualHeight();
				for (int j = y + 1; j < loop; j++)
				{
					final Block block = world.getBlockState(x, j, z);
					if ((block == null) || (world.isAirBlock(x, j, z)) || (block.isReplaceable(world, x, j, z)))
					{
						if (!world.isRemote)
						{
							if (world.setBlockState(x, j, z, this, 0, 3) && !player.capabilities.isCreativeMode)
							{
								itemstack.stackSize -= 1;
							}
							return true;
						}
					}
					if (block != this)
					{
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onNeighborChange(World world, BlockPos pos, Block par5)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		entity.fallDistance = 0.0F;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else if (entity.isSneaking())
		{
			final double d = entity.prevPosY - entity.posY;
			entity.boundingBox.minY += d;
			entity.boundingBox.maxY += d;
			entity.posY = entity.prevPosY;
		}
		else
		{
			entity.motionY = -0.1D;
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (world.getBlockState(x, y -1 , z).isSideSolid(world, x, y - 1, z, EnumFacing.UP)) return true;
		if (checkSides(world, pos)) return true;

		return false;
	}

	private boolean checkSides(World world, BlockPos pos)
	{
		final boolean flag = world.getBlockState(x + 1, y, z) == this;
		final boolean flag1 = world.getBlockState(x - 1, y, z) == this;
		final boolean flag2 = world.getBlockState(x, y, z + 1) == this;
		final boolean flag3 = world.getBlockState(x, y, z - 1) == this;

		if (!flag && !flag1 && !flag2 && !flag3) return false;

		if (flag && world.getBlockState(x + 1, y - 1, z).isSideSolid(world, x + 1, y - 1, z, EnumFacing.UP)) return true;
		if (flag1 && world.getBlockState(x - 1, y - 1, z).isSideSolid(world, x - 1, y - 1, z, EnumFacing.UP)) return true;
		if (flag2 && world.getBlockState(x, y - 1, z + 1).isSideSolid(world, x, y - 1, z + 1, EnumFacing.UP)) return true;
		if (flag3 && world.getBlockState(x, y - 1, z - 1).isSideSolid(world, x, y - 1, z - 1, EnumFacing.UP)) return true;

		if (flag && world.getBlockState(x + 2, y - 1, z).isSideSolid(world, x + 2, y - 1, z, EnumFacing.UP)) return true;
		if (flag1 && world.getBlockState(x - 2, y - 1, z).isSideSolid(world, x - 2, y - 1, z, EnumFacing.UP)) return true;
		if (flag2 && world.getBlockState(x, y - 1, z + 2).isSideSolid(world, x, y - 1, z + 2, EnumFacing.UP)) return true;
		if (flag3 && world.getBlockState(x, y - 1, z - 2).isSideSolid(world, x, y - 1, z - 2, EnumFacing.UP)) return true;

		return false;
	}

	/************
	 * STUFF
	 ************/

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return EnumFacing.UP == side;
	}

	/************
	 * TEXTURES
	 ************/

	/************
	 * RENDER
	 ************/
	@Override
	public int getRenderType()
	{
		return RenderBambooScaffold.id;
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		final float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(x + this.minX + f, y + this.minY, z + this.minZ + f, x + this.maxX - f, y + this.maxY, z + this.maxZ - f);
	}
}
