package growthcraft.bamboo.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public abstract class BlockBambooLeaves extends BlockLeaves implements IShearable
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	private int[] adjacentTreeBlocks;

	public BlockBambooLeaves()
	{
		super(Material.LEAVES, false);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setHardness(0.2F);
		setTickRandomly(true);
		setBlockName("grc.bambooLeaves");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	private void removeLeaves(World world, BlockPos pos)
	{
		this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
		world.setBlockToAir(pos);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random rand, IBlockState state)
	{
		if (!world.isRemote)
		{
			final int meta = world.getBlockState(pos);

			if ((meta & 8) != 0 && (meta & 4) == 0)
			{
				final byte b0 = 4;
				final int i1 = b0 + 1;
				final byte b1 = 32;
				final int j1 = b1 * b1;
				final int k1 = b1 / 2;

				if (this.adjacentTreeBlocks == null)
				{
					this.adjacentTreeBlocks = new int[b1 * b1 * b1];
				}

				int l1;

				if (world.checkChunksExist(pos - i1, pos - i1, pos - i1, pos + i1, pos + i1, pos + i1))
				{
					int i2;
					int j2;
					int k2;

					for (l1 = -b0; l1 <= b0; ++l1)
					{
						for (i2 = -b0; i2 <= b0; ++i2)
						{
							for (j2 = -b0; j2 <= b0; ++j2)
							{
								final Block block = world.getBlockState(x + l1, y + i2, z + j2);

								if (block != null && block.canSustainLeaves(world, x + l1, y + i2, z + j2))
								{
									this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
								}
								else if (block != null && block.isLeaves(world, pos.getX() + l1, pos.getY() + i2, pos.getZ() + j2))
								{
									this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
								}
								else
								{
									this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
								}
							}
						}
					}

					for (l1 = 1; l1 <= 4; ++l1)
					{
						for (i2 = -b0; i2 <= b0; ++i2)
						{
							for (j2 = -b0; j2 <= b0; ++j2)
							{
								for (k2 = -b0; k2 <= b0; ++k2)
								{
									if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
									{
										if (this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
										}

										if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
										{
											this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
										}
									}
								}
							}
						}
					}
				}

				l1 = this.adjacentTreeBlocks[k1 * j1 + k1 * b1 + k1];

				if (l1 >= 0)
				{
					world.setBlockMetadataWithNotify(x, y, z, meta & -9, BlockFlags.SUPRESS_RENDER);
				}
				else
				{
					this.removeLeaves(world, x, y, z);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random, IBlockState state)
	{
		super.randomDisplayTick(state, world, pos, random);
		if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, pos.getX(), pos.getY() - 1, pos.getZ()) && random.nextInt(15) == 1)
		{
			final double d0 = (double)((float)pos.getX() + random.nextFloat());
			final double d1 = (double)pos.getY() - 0.05D;
			final double d2 = (double)((float)pos.getz() + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public void breakBlock(World world, BlockPos pos, Block par5, int par6, IBlockState state)
	{
		final byte b0 = 1;
		final int j1 = b0 + 1;

		if (world.checkChunksExist(pos.getX() - j1, pos.getY() - j1, pos.getZ() - j1, pos.getX() + j1, pos.getY() + j1, pos.getZ() + j1))
		{
			for (int k1 = -b0; k1 <= b0; ++k1)
			{
				for (int l1 = -b0; l1 <= b0; ++l1)
				{
					for (int i2 = -b0; i2 <= b0; ++i2)
					{
						final Block j2 = world.getBlockState(pos + k1, pos + l1, pos + i2);

						if (j2 != null)
						{
							j2.beginLeavesDecay(state, pos + k1, pos + l1, pos + i2, world);
						}
					}
				}
			}
		}
	}

	/************
	 * STUFF
	 ************/
	@Override
	public void beginLeavesDecay(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockState(x, y, z) | 8, BlockFlags.SUPRESS_RENDER);
	}

	@Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	protected ItemStack createStackedBlock(int meta)
	{
		return new ItemStack(this, 1, meta & 3);
	}


	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons = new IIcon[2];

		icons[0] = reg.registerIcon("grcbamboo:leaves");
		icons[1] = reg.registerIcon("grcbamboo:leaves_opaque");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return icons[isOpaqueCube() ? 1 : 0];
	}

	/************
	 * RENDERS
	 ************/
	@Override
	public boolean isOpaqueCube()
	{
		return Blocks.LEAVES.isOpaqueCube();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

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
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		final int meta = world.getBlockState(x, y, z);
		int r = 0;
		int g = 0;
		int b = 0;

		for (int x1 = -1; x1 <= 1; ++x1)
		{
			for (int z1 = -1; z1 <= 1; ++z1)
			{
				final int j2 = world.getBiomeGenForCoords(x + z1, z + x1).getBiomeFoliageColor(x + z1, y, z + x1);
				r += (j2 & 16711680) >> 16;
				g += (j2 & 65280) >> 8;
				b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}

	/************
	 * SHEARS
	 ************/
	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.LEAVES, 1, world.getBlockState(x, y, z) & 3));
		return ret;
	}
}
