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
package growthcraft.netherloid.common.block;

import growthcraft.core.common.block.Materials;
import growthcraft.netherloid.netherloid;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

public abstract class BlockNetherMaliceLeaves extends BlockLeaves implements IShearable, IGrowable {
    private final int growth = netherloid.getConfig().maliceLeavesGrowthRate;
    @SideOnly(Side.CLIENT)


    private int[] adjacentTreeBlocks;

    public BlockNetherMaliceLeaves() {
        super(Materials.fireproofLeaves, false);
        setTickRandomly(true);
        setHardness(0.2F);
        setLightOpacity(1);
        //setStepSound(soundTypeGrass);
        setUnlocalizedName("grcnetherloid.netherMaliceLeaves");
        setCreativeTab(netherloid.tab);
    }

    public void growFruit(World world, Random random, BlockPos pos) {
        if (world.isAirBlock(x, y - 1, z)) {
            world.setBlockState(x, y - 1, z, netherloid.blocks.netherMaliceFruit.getBlockState());
        }
    }

    /* Bonemeal? Client side */
    @Override
    public boolean func_149851_a(World world, BlockPos pos, boolean isClient) {
        return world.isAirBlock(x, y - 1, z) && (world.getBlockState(x, y, z) & 3) == 0;
    }

    /* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
    @Override
    public boolean func_149852_a(World world, Random random, BlockPos pos) {
        return true;
    }

    /* Apply bonemeal effect */
    @Override
    public void func_149853_b(World world, Random random, BlockPos pos) {
        growFruit(world, random, x, y, z);
    }

    /************
     * TICK
     ************/
    @Override
    public void updateTick(World world, BlockPos pos, Random random) {
        if (!world.isRemote) {
            final int meta = world.getBlockState(x, y, z);

            if ((meta & 4) == 0) {
                // Spawn Fruit
                if (world.rand.nextInt(this.growth) == 0) {
                    growFruit(world, random, pos);
                }
            }

            if ((meta & LeavesStage.DECAY_MASK) != 0 && (meta & 4) == 0) {
                final byte b0 = 4;
                final int i1 = b0 + 1;
                final byte b1 = 32;
                final int j1 = b1 * b1;
                final int k1 = b1 / 2;

                if (this.adjacentTreeBlocks == null) {
                    this.adjacentTreeBlocks = new int[b1 * b1 * b1];
                }

                int l1;

                if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                final Block block = world.getBlockState(x + l1, y + i2, z + j2);

                                if (!block.canSustainLeaves(world, x + l1, y + i2, z + j2)) {
                                    if (block.isLeaves(world, x + l1, y + i2, z + j2)) {
                                        this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    } else {
                                        this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                } else {
                                    this.adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            for (j2 = -b0; j2 <= b0; ++j2) {
                                for (int k2 = -b0; k2 <= b0; ++k2) {
                                    if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1) {
                                        if (this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2) {
                                            this.adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.adjacentTreeBlocks[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0) {
                    world.setBlockState(x, y, z, meta & -9, 4);
                } else {
                    this.removeLeaves(world, x, y, z);
                }
            }
        }
    }

    private void removeLeaves(World world, BlockPos pos) {
        this.dropBlockAsItem(world, x, y, z, world.getBlockState(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, Random random, IBlockState stateIn) {
        super.randomDisplayTick(stateIn, world, pos, random);
        if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1) {
            final double d0 = (double) ((float) x + random.nextFloat());
            final double d1 = (double) y - 0.05D;
            final double d2 = (double) ((float) z + random.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    /************
     * TRIGGERS
     ************/
    @Override
    public void breakBlock(World world, BlockPos pos, Block block, int par6) {
        final byte b0 = 1;
        final int i1 = b0 + 1;

        if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
            for (int j1 = -b0; j1 <= b0; ++j1) {
                for (int k1 = -b0; k1 <= b0; ++k1) {
                    for (int l1 = -b0; l1 <= b0; ++l1) {
                        final Block block2 = world.getBlockState(x + j1, y + k1, z + l1);
                        if (block2.isLeaves(world, x + j1, y + k1, z + l1)) {
                            block2.beginLeavesDecay(world, x + j1, y + k1, z + l1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, int par6, TileEntity entity, IBlockState state, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, entity, stack);
    }

    /************
     * STUFF
     ************/
    @Override
    public void beginLeavesDecay(World world, BlockPos pos) {
        world.setBlockState(pos, world.getBlockState(pos) | LeavesStage.DECAY_MASK, 4);
    }

    @Override
    public boolean isLeaves(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata) {
        return false;
    }

    /************
     * DROPS
     ************/
    @Override
    public Item getItemDropped(int meta, Random random, int par3) {
        return netherloid.blocks.netherMaliceSapling.getItem();
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, int meta, float par6, int fortune, IBlockState state) {
        if (!world.isRemote) {
            int random = 20;

            if (fortune > 0) {
                random -= 2 << fortune;

                if (random < 10) {
                    random = 10;
                }
            }

            if (world.rand.nextInt(random) == 0) {
                this.dropBlockAsItem(world, pos, state, new ItemStack(this.getItemDropped(meta, world.rand, fortune), 1, 0));
            }
        }
    }

    /************
     * RENDERS
     ************/
    @Override
    public boolean isOpaqueCube() {
        return Blocks.LEAVES.isOpaqueCube();
    }

    /************
     * TEXTURES
     ************/
    //@Override
    //@SideOnly(Side.CLIENT)

    //{
    //	icons = new IIcon[2];
//
    //	icons[0] = reg.registerIcon("grcnetherloid:leaves_malice");
    //	icons[1] = reg.registerIcon("grcnetherloid:leaves_malice_opaque");
    //}

    //@Override
    //@SideOnly(Side.CLIENT)
    //
    //{
    //	return this.icons[this.isOpaqueCube() ? 1 : 0];
    //}
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side) {
        return true;
    }

    /************
     * COLORS
     ************/
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, BlockPos pos) {
        return 0xFFFFFF;
    }

    /************
     * SHEARS
     ************/
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Blocks.LEAVES, 1, world.getBlockState(pos) & 3));
        return ret;
    }

    public static class LeavesStage {
        public static final int DECAY_MASK = 8;

        private LeavesStage() {
        }
    }
}
