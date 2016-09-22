package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.client.renderer.RenderBamboo;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.util.BlockCheck;
import growthcraft.core.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockBambooStalk extends GrcBlockBase
{
	@SideOnly(Side.CLIENT)
	public static IIcon[] tex;

	//constants
	private final int growth = GrowthCraftBamboo.getConfig().bambooStalkGrowthRate;

	public BlockBambooStalk()
	{
		super(Material.WOOD);
		setStepSound(soundTypeWood);
		setHardness(2.0F);
		setTickRandomly(true);
		setBlockName("grc.bambooStalk");
		setCreativeTab(null);
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, BlockPos pos)
	{
		return true;
	}

	/************
	 * TICK
	 ************/
	@Override
	public int tickRate(World world)
	{
		return 5;
	}

	@Override
	public void updateTick(World world, BlockPos pos, Random rand, IBlockState state)
	{
		if (world.getBlockState(pos) == 0)
		{
			int x1 = pos.getX();
			int y1 = pos.getY();
			int z1 = pos.getZ();

			if (isBambooOnGround(world, pos))
			{
				if (rand.nextInt(this.growth) == 0)
				{
					final byte b = 9;
					int amount = 10;
					final BlockBambooShoot bambooShoot = GrowthCraftBamboo.blocks.bambooShoot.getBlockState();

					for (x1 = pos.getX() - b; x1 <= pos.getX() + b; ++x1)
					{
						for (z1 = pos.getZ() - b; z1 <= pos.getZ() + b; ++z1)
						{
							for (y1 = pos.getY() - 1; y1 <= pos.getY() + 1; ++y1)
							{
								final boolean flag1 = world.getBlockState(pos) == this && isBambooOnGround(world, pos);
								final boolean flag2 = world.getBlockState(pos) == bambooShoot;
								if (flag1 || flag2)
								{
									--amount;
									if (amount <= 0)
									{
										return;
									}
								}
							}
						}
					}

					x1 = pos.getX() + rand.nextInt(3) - 1;
					y1 = pos.getY() + rand.nextInt(2) - rand.nextInt(2);
					z1 = pos.getZ() + rand.nextInt(3) - 1;

					for (int loop = 0; loop < 4; ++loop)
					{
						if (world.isAirBlock(pos) && bambooShoot.canBlockStay(world, pos))
						{
							x = x1;
							y = y1;
							z = z1;
						}

						x1 = pos.getX() + rand.nextInt(3) - 1;
						y1 = pos.getY() + rand.nextInt(2) - rand.nextInt(2);
						z1 = pos.getZ() + rand.nextInt(3) - 1;
					}

					if (world.isAirBlock(pos) && bambooShoot.canBlockStay(world, pos));
					{
						world.setBlockState(pos);
					}
				}

			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block s)
	{
		boolean flag = false;

		if (world.getBlockState(pos) != this)
		{
			if (!isBambooOnGround(world, pos))
			{
				flag = true;
			}
		}

		if (flag && world.getBlockState(pos) == 0)
		{
			this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
			world.setBlockToAir(pos);
		}

		super.onNeighborBlockChange(world, pos, s);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, Block par5, int par6)
	{
		if (world.getBlockState(pos) == 0)
		{
			final byte b0 = 4;
			final int j1 = b0 + 1;

			if (world.isBlockLoaded(pos))
			{
				for (int x1 = -b0; x1 <= b0; ++x1)
				{
					for (int y1 = -b0; y1 <= b0; ++y1)
					{
						for (int z1 = -b0; z1 <= b0; ++z1)
						{
							final Block block = world.getBlockState(pos);
							if (block != null)
							{
								block.beginLeavesDecay(state, world, pos);
							}
						}
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftBamboo.items.bamboo.getItem();
	}

	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos) == 0 ? true : false;
	}

	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	public boolean isBambooOnGround(World world, BlockPos pos)
	{
		if (!BlockCheck.canSustainPlant(world, pos, pos, pos, EnumFacing.UP, GrowthCraftBamboo.blocks.bambooShoot.getBlockState())) return false;
		return this == world.getBlockState(pos);
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
	{
		return false;
	}

	private boolean canFence(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos) == GrowthCraftBamboo.blocks.bambooFence.getBlockState() ||
			world.getBlockState(pos) == Blocks.OAK_FENCE_GATE ||
			world.getBlockState(pos) == GrowthCraftBamboo.blocks.bambooFenceGate.getBlockState();
	}

	private boolean canWall(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos) == GrowthCraftBamboo.blocks.bambooWall.getBlockState();
	}

	private boolean canDoor(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos) instanceof BlockDoor;
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return GrowthCraftBamboo.items.bamboo.getItem();
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		tex = new IIcon[5];

		tex[0] = reg.registerIcon("grcbamboo:plant_top");
		tex[1] = reg.registerIcon("grcbamboo:plant_nocolor");
		tex[2] = reg.registerIcon("grcbamboo:plant_color");
		tex[3] = reg.registerIcon("grcbamboo:block");
		tex[4] = reg.registerIcon("grcbamboo:fence");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return side == 1 ? tex[0] : ( side == 0 ? tex[0] : (meta == 0 ? tex[1] : tex[2]));
	}

	@Override
	public int getRenderType()
	{
		return RenderBamboo.id;
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
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int s)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		final double d0 = 0.5D;
		final double d1 = 1.0D;
		return ColorizerGrass.getGrassColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int par1)
	{
		return par1 == 0 ? 0xFFFFFF : ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos)
	{
		if (world.getBlockState(pos) == 0)
		{
			int r = 0;
			int g = 0;
			int b = 0;

			for (int l1 = -1; l1 <= 1; ++l1)
			{
				for (int i2 = -1; i2 <= 1; ++i2)
				{
					final int color = world.getBiome(x + i2, z + l1).getBiomeFoliageColor(x + i2, y, z + l1);
					r += (color & 16711680) >> 16;
					g += (color & 65280) >> 8;
					b += color & 255;
				}
			}

			return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
		}
		else
		{
			return 0xFFFFFF;
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		float x1 = 0.25F;
		float x2 = 0.75F;
		float z1 = 0.25F;
		float z2 = 0.75F;

		if (world.getBlockState(x, y, z) != 0)
		{
			if (this.canFence(world, x, y, z - 1) || this.canWall(world, x, y, z - 1) || this.canDoor(world, x, y, z - 1))
			{
				z1 = 0.0F;
			}

			if (this.canFence(world, x, y, z + 1) || this.canWall(world, x, y, z + 1) || this.canDoor(world, x, y, z + 1))
			{
				z2 = 1.0F;
			}

			if (this.canFence(world, x - 1, y, z) || this.canWall(world, x - 1, y, z) || this.canDoor(world, x - 1, y, z))
			{
				x1 = 0.0F;
			}

			if (this.canFence(world, x + 1, y, z) || this.canWall(world, x + 1, y, z) || this.canDoor(world, x + 1, y, z))
			{
				x2 = 1.0F;
			}
		}

		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, AxisAlignedBB axis, List list, Entity entity)
	{
		final float x1 = 0.25F;
		final float x2 = 0.75F;
		final float z1 = 0.25F;
		final float z2 = 0.75F;

		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		if (world.getBlockState(x, y, z) != 0)
		{
			if (this.canFence(world, x, y, z - 1))
			{
				renderFence(world, axis, list, entity, x, y, z, RenderUtils.Face.ZNEG);
			}
			else if (this.canWall(world, x, y, z - 1))
			{
				renderWall(world, axis, list, entity, x, y, z, RenderUtils.Face.ZNEG);
			}
			else if (this.canDoor(world, x, y, z - 1))
			{
				renderDoor(world, axis, list, entity, x, y, z, RenderUtils.Face.ZNEG);
			}

			if (this.canFence(world, x, y, z + 1))
			{
				renderFence(world, axis, list, entity, x, y, z, RenderUtils.Face.ZPOS);
			}
			else if (this.canWall(world, x, y, z + 1))
			{
				renderWall(world, axis, list, entity, x, y, z, RenderUtils.Face.ZPOS);
			}
			else if (this.canDoor(world, x, y, z + 1))
			{
				renderDoor(world, axis, list, entity, x, y, z, RenderUtils.Face.ZPOS);
			}

			if (this.canFence(world, x - 1, y, z))
			{
				renderFence(world, axis, list, entity, x, y, z, RenderUtils.Face.XNEG);
			}
			else if (this.canWall(world, x - 1, y, z))
			{
				renderWall(world, axis, list, entity, x, y, z, RenderUtils.Face.XNEG);
			}
			else if (this.canDoor(world, x - 1, y, z))
			{
				renderDoor(world, axis, list, entity, x, y, z, RenderUtils.Face.XNEG);
			}

			if (this.canFence(world, x + 1, y, z))
			{
				renderFence(world, axis, list, entity, x, y, z, RenderUtils.Face.XPOS);
			}
			else if (this.canWall(world, x + 1, y, z))
			{
				renderWall(world, axis, list, entity, x, y, z, RenderUtils.Face.XPOS);
			}
			else if (this.canDoor(world, x + 1, y, z))
			{
				renderDoor(world, axis, list, entity, x, y, z, RenderUtils.Face.XPOS);
			}
		}

		this.setBlockBoundsBasedOnState(world, x, y, z);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderFence(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, RenderUtils.Face m)
	{
		float x1 = x;
		float x2 = x + 1.0F;
		float z1 = z;
		float z2 = z + 1.0F;

		float y1 = 0.75F;
		float y2 = 0.9375F;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}

		this.setBlockBounds(x1, y1, z1, x2, y2, z2);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);

		y1 = 0.375F;
		y2 = 0.5625F;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.4375F;
			x2 = 0.5625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.4375F;
			z2 = 0.5625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}

		this.setBlockBounds(x1, y1, z1, x2, y2, z2);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderWall(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, RenderUtils.Face m)
	{
		float x1 = x;
		float x2 = x + 1.0F;
		float z1 = z;
		float z2 = z + 1.0F;

		final double y1 = 0.0F;
		final double y2 = 1.0F;

		if (m == RenderUtils.Face.ZNEG)
		{
			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.0F;
			z2 = 0.25F;
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			x1 = 0.375F;
			x2 = 0.625F;
			z1 = 0.75F;
			z2 = 1.0F;
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			z1 = 0.375F;
			z2 = 0.625F;
			x1 = 0.0F;
			x2 = 0.25F;
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			z1 = 0.375F;
			z2 = 0.625F;
			x1 = 0.75F;
			x2 = 1.0F;
		}

		this.setBlockBounds(x1, 0.0F, z1, x2, 1.0F, z2);
		super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void renderDoor(World world, AxisAlignedBB axis, List list, Entity entity, BlockPos pos, RenderUtils.Face m)
	{
		float x1 = x;
		float x2 = x + 1.0F;
		float z1 = z;
		float z2 = z + 1.0F;

		final float y1 = 0.0F;
		final float y2 = 1.0F;

		int tm0;
		int tm;

		if (m == RenderUtils.Face.ZNEG)
		{
			tm0 = world.getBlockState(x, y, z - 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockState(x, y - 1, z - 1);
			}
			tm = tm0 & 3;
			if (tm == 0)
			{
				x1 = 0.0F;
				x2 = 0.375F;
				z1 = 0.0F;
				z2 = 0.25F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 2)
			{
				x1 = 0.625F;
				x2 = 1.0F;
				z1 = 0.0F;
				z2 = 0.25F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == RenderUtils.Face.ZPOS)
		{
			tm0 = world.getBlockState(x, y, z + 1);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockState(x, y - 1, z + 1);
			}

			tm = tm0 & 3;
			if (tm == 0)
			{
				x1 = 0.0F;
				x2 = 0.375F;
				z1 = 0.75F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 2)
			{
				x1 = 0.625F;
				x2 = 1.0F;
				z1 = 0.75F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == RenderUtils.Face.XNEG)
		{
			tm0 = world.getBlockState(x - 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockState(x - 1, y - 1, z);
			}
			tm = tm0 & 3;
			if (tm == 1)
			{

				x1 = 0.0F;
				x2 = 0.25F;
				z1 = 0.0F;
				z2 = 0.375F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 3)
			{

				x1 = 0.0F;
				x2 = 0.25F;
				z1 = 0.625F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
		else if (m == RenderUtils.Face.XPOS)
		{
			tm0 = world.getBlockState(x + 1, y, z);
			if ((tm0 & 8) > 7)
			{
				tm0 = world.getBlockState(x + 1, y - 1, z);
			}
			tm = tm0 & 3;
			if (tm == 1)
			{
				x1 = 0.75F;
				x2 = 1.0F;
				z1 = 0.0F;
				z2 = 0.375F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}

			if (tm == 3)
			{
				x1 = 0.75F;
				x2 = 1.0F;
				z1 = 0.625F;
				z2 = 1.0F;

				this.setBlockBounds(x1, y1, z1, x2, y2, z2);
				super.addCollisionBoxesToList(world, x, y, z, axis, list, entity);
			}
		}
	}
}
