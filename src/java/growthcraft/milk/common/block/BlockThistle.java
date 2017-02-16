/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
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
package growthcraft.milk.common.block;

import growthcraft.api.core.util.BBox;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.CuboidI;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.logic.FlowerSpread;
import growthcraft.core.logic.ISpreadablePlant;
import growthcraft.milk.GrowthCraftMilk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockThistle extends BlockBush implements ISpreadablePlant, IGrowable {
    public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, 3);
    private final FlowerSpread spreadLogic;


    @SideOnly(Side.CLIENT)


    public BlockThistle() {
        super(Material.PLANTS);
        setTickRandomly(true);
        setUnlocalizedName("grcmilk.Thistle");
        //setStepSound(soundTypeGrass);
        setCreativeTab(GrowthCraftMilk.creativeTab);
        final BBox bb = BBox.newCube(2f, 0f, 2f, 12f, 16f, 12f).scale(1f / 16f);
        //getBoundingBox(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
        this.spreadLogic = new FlowerSpread(new CuboidI(-1, -1, -1, 2, 2, 2));
    }

    @Override
    public boolean canSpreadTo(World world, BlockPos pos) {
        //if (world.isAirBlock(pos) && canBlockStay(world, pos))
        return world.isAirBlock(pos);
    }

    private void runSpread(World world, BlockPos pos, Random random, IBlockState state, World worldIn) {
        spreadLogic.run(this, 0, world, pos, random, state, worldIn);
    }

    private void incrementGrowth(World world, BlockPos pos, IBlockState state, int meta2, Block block) {
        final int meta = state.getValue(GROWTH);
        world.setBlockState(pos, state.withProperty(GROWTH, meta + 1), BlockFlags.SYNC);
        AppleCore.announceGrowthTick(block, world, pos, meta2, state);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random, int meta2, Block block) {
        super.updateTick(world, pos, state, random);
        if (!world.isRemote) {
            final int meta = state.getValue(GROWTH);
            if (meta >= ThistleStage.FLOWER) {
                final int spreadChance = GrowthCraftMilk.getConfig().thistleSpreadChance;
                if (spreadChance > 0) {
                    if (random.nextInt(spreadChance) == 0) {
                        runSpread(world, pos, random, state, world);
                    }
                }
            } else {
                final int growthChance = GrowthCraftMilk.getConfig().thistleGrowthChance;
                final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random, state);
                if (allowGrowthResult == Event.Result.DENY) {
                    return;
                }

                if (allowGrowthResult == Event.Result.ALLOW || (growthChance > 0 && random.nextInt(growthChance) == 0)) {
                    if (meta < ThistleStage.FLOWER) {
                        incrementGrowth(world, pos, state, meta2, block);
                    }
                }
            }
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    /* Can this accept bonemeal? */
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

	/* IGrowable interface
     *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */


	/* IGrowable interface
	 *	Check: http://www.minecraftforge.net/forum/index.php?topic=22571.0
	 *	if you have no idea what this stuff means
	 */

    /* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        final int meta = state.getValue(GROWTH);
        if (meta < ThistleStage.FLOWER) {
            if (GrowthCraftMilk.items.seedThistle != null) {
                return GrowthCraftMilk.items.seedThistle.getItem();
            }
            return null;
        }
        return super.getItemDropped(state, random, fortune);
    }

    /* Apply bonemeal effect */
    @Override
    public void grow(World world, Random random, BlockPos pos, IBlockState state, int meta2, Block block) {
        final int meta = state.getValue(GROWTH);
        if (meta < ThistleStage.FLOWER) {
            final int growthChance = GrowthCraftMilk.getConfig().thistleGrowthChance;
            if (growthChance > 0) {
                if (random.nextInt(growthChance) != 0) return;
            }
            incrementGrowth(world, pos, state, meta2, block);
        } else {
            runSpread(world, pos, random, state, world);
        }
    }

    public static class ThistleStage {
        public static final int SEEDLING = 0;
        public static final int BUD = 1;
        public static final int GROWN = 2;
        public static final int FLOWER = 3;

        private ThistleStage() {
        }
    }

    //@Override
    //@SideOnly(Side.CLIENT)

    //{
    //	this.icons = new IIcon[4];
//
    //	icons[0] = reg.registerIcon("grcmilk:thistle/stage1");
    //	icons[1] = reg.registerIcon("grcmilk:thistle/stage2");
    //	icons[2] = reg.registerIcon("grcmilk:thistle/stage3");
    //	icons[3] = reg.registerIcon("grcmilk:thistle/stage4");
    //}

    //@Override
    //@SideOnly(Side.CLIENT)
    //
    //{
    //	if (meta < 0 || meta >= icons.length)
    //	{
    //		return icons[3];
    //	}
    //	return this.icons[meta];
    //}
}
