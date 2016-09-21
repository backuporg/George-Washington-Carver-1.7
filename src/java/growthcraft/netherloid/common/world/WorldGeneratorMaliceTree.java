/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.netherloid.common.world;

import java.util.Random;

import growthcraft.netherloid.netherloid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.util.EnumFacing;

public class WorldGeneratorMaliceTree extends WorldGenerator
{
	private final int minTreeHeight = 4;
	private final int metaWood      = 0;
	private final int metaLeaves    = 0;
	private final Block log         = netherloid.blocks.netherMaliceLog.getBlockState();
	private final Block leaves      = netherloid.blocks.netherMaliceLeaves.getBlockState();

	public WorldGeneratorMaliceTree(boolean doblocknotify)
	{
		super(doblocknotify);
	}

	public boolean generate(World world, Random random, BlockPos pos)
	{
		final int l = random.nextInt(3) + this.minTreeHeight;
		boolean flag = true;

		if (y >= 1 && y + l + 1 <= 256)
		{
			byte b0;
			int k1;
			Block block;

			for (int i1 = y; i1 <= y + 1 + l; ++i1)
			{
				b0 = 1;

				if (i1 == y)
				{
					b0 = 0;
				}

				if (i1 >= y + 1 + l - 2)
				{
					b0 = 2;
				}

				for (int j1 = x - b0; j1 <= x + b0 && flag; ++j1)
				{
					for (k1 = z - b0; k1 <= z + b0 && flag; ++k1)
					{
						if (i1 >= 0 && i1 < 256)
						{
							block = world.getBlockState(j1, i1, k1);

							if (!this.isReplaceable(world, j1, i1, k1))
							{
								flag = false;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}

			if (!flag)
			{
				return false;
			}
			else
			{
				final Block block2 = world.getBlockState(x, y - 1, z);
				final boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, (BlockSapling)Blocks.SAPLING);

				if (isSoil && y < 256 - l - 1)
				{
					block2.onPlantGrow(world, x, y - 1, z, x, y, z);
					b0 = 3;
					final byte b1 = 0;
					int l1;
					int i2;
					int j2;
					int i3;

					for (k1 = y - b0 + l; k1 <= y + l; ++k1)
					{
						i3 = k1 - (y + l);
						l1 = b1 + 1 - i3 / 2;

						for (i2 = x - l1; i2 <= x + l1; ++i2)
						{
							j2 = i2 - x;

							for (int k2 = z - l1; k2 <= z + l1; ++k2)
							{
								final int l2 = k2 - z;

								if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || random.nextInt(2) != 0 && i3 != 0)
								{
									final Block block1 = world.getBlockState(i2, k1, k2);

									if (block1.isAir(world, i2, k1, k2) || block1.isLeaves(world, i2, k1, k2))
									{
										this.setBlockAndNotifyAdequately(world, i2, k1, k2, leaves, this.metaLeaves);
									}
								}
							}
						}
					}

					for (k1 = 0; k1 < l; ++k1)
					{
						block = world.getBlockState(x, y + k1, z);

						if (block.isAir(world, x, y + k1, z) || block.isLeaves(world, x, y + k1, z))
						{
							this.setBlockAndNotifyAdequately(world, x, y + k1, z, log, this.metaWood);
						}
					}

					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}

	protected boolean func_150523_a(Block block)
	{
		return block.getMaterial() == Material.AIR ||
			block.getMaterial() == Material.LEAVES ||
			block == Blocks.GRASS ||
			block == Blocks.DIRT ||
			block == Blocks.LOG ||
			block == Blocks.LOG2 ||
			block == Blocks.SAPLING ||
			block == Blocks.VINE;
	}

	protected boolean isReplaceable(World world, BlockPos pos)
	{
		final Block block = world.getBlockState(x, y, z);
		return block.isAir(world, x, y, z) ||
			block.isLeaves(world, x, y, z) ||
			block.isWood(world, x, y, z) ||
			func_150523_a(block);
	}
}
