/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
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
package growthcraft.core.util;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.block.IBlockRope;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockCheck {
    /**
     * 2D directions
     */
    public static final EnumFacing[] DIR4 = new EnumFacing[]{
            EnumFacing.NORTH,
            EnumFacing.SOUTH,
            EnumFacing.WEST,
            EnumFacing.EAST
    };
    public static final BlockDirection[] DIR8 = new BlockDirection[]{
            BlockDirection.NORTH,
            BlockDirection.SOUTH,
            BlockDirection.WEST,
            BlockDirection.EAST,
            BlockDirection.NORTH_WEST,
            BlockDirection.NORTH_EAST,
            BlockDirection.SOUTH_WEST,
            BlockDirection.SOUTH_EAST
    };

    private BlockCheck() {
    }

    /**
     * Randomly selects a direction from the DIR4 array and returns it
     *
     * @param random - random number generator
     * @return a random direction
     */
    public static EnumFacing randomDirection4(Random random) {
        return DIR4[random.nextInt(DIR4.length)];
    }

    /**
     * Randomly selects a direction from the DIR8 array and returns it
     *
     * @param random - random number generator
     * @return a random direction
     */
    public static BlockDirection randomDirection8(Random random) {
        return DIR8[random.nextInt(DIR8.length)];
    }

    /**
     * Determines if block is a water block
     *
     * @param block - the block to check
     * @return true if the block is a water, false otherwise
     */
    public static boolean isWater(Block block) {
        if (block == null) return false;
        return block.getMaterial() == Material.WATER;
    }

    /**
     * Determines if block is a rope block
     *
     * @param block - the block to check
     * @return true if the block is a rope block, false otherwise
     */
    public static boolean isRopeBlock(IBlockState block) {
        return block instanceof IBlockRope;
    }

    /**
     * Determines if block is a "rope"
     *
     * @param block - the block to check
     * @return true if the block is a Rope, false otherwise
     */
    public static boolean isRope(IBlockState block) {
        return GrowthCraftCore.blocks.ropeBlock.equals(block);
    }

    /**
     * Determines if block at the specified location is a valid rope block
     *
     * @param world - World, duh.
     * @param x     - x coord
     * @param y     - y coord
     * @param z     - z coord
     * @return true if the block is a Rope, false otherwise
     */
    public static boolean isRope(IBlockAccess world, BlockPos pos, IBlockState state) {
        final IBlockState block = world.getBlockState((BlockPos) state);
        // TODO: IBlockRope is used for any block which can grow on Ropes,
        // as well as Ropes themselves, we need someway to seperate them,
        // either, IBlockRope.isRope(world, x, y, z) OR an additional interface
        // IBlockRopeCrop, IRope
        return isRope((IBlockState) block);
    }

    /**
     * Determines if the block at the specified location can sustain an
     * IPlantable plant.
     *
     * @param soil       - The soil block
     * @param world      - World
     * @param x          - x coord
     * @param y          - y coord
     * @param z          - z coord
     * @param EnumFacing - direction in which the plant will grow
     * @param plant      - the plant in question
     * @return true if the block can be planted, false otherwise
     */
    public static boolean canSustainPlantOn(IBlockAccess world, BlockPos pos, EnumFacing face, IPlantable plant, Block soil, IBlockState state) {
        return soil != null && soil.canSustainPlant(state, world, pos, face, plant);
    }

    /**
     * Determines if the block at the specified location can sustain an
     * IPlantable plant.
     *
     * @param world      - World, duh.
     * @param x          - x coord
     * @param y          - y coord
     * @param z          - z coord
     * @param EnumFacing - direction in which the plant will grow
     * @param plant      - the plant in question
     * @return true if the block can be planted, false otherwise
     */
    public static boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing EnumFacing, IPlantable plant, IBlockState state, Block block) {
        final IBlockState soil = world.getBlockState((BlockPos) state);
        return canSustainPlantOn(world, pos, EnumFacing, plant, block, state);
    }

    /**
     * Determines if the block at the specified location can sustain an IPlantable plant, returns the block if so, else returns null;
     *
     * @param world      - World
     * @param x          - x coord
     * @param y          - y coord
     * @param z          - z coord
     * @param EnumFacing - direction in which the plant will grow
     * @param plant      - the plant in question
     * @return block if it can be planted upon, else null
     */
    public static IBlockState getFarmableBlock(IBlockAccess world, BlockPos pos, EnumFacing EnumFacing, IPlantable plant, IBlockState state, Block block) {
        final IBlockState soil = world.getBlockState((BlockPos) state);
        if (canSustainPlantOn(world, pos, EnumFacing, plant, block, state))
            return soil;
        return null;
    }

    /**
     * Determines if a block can be placed on the given side of the coords
     *
     * @param world      - World
     * @param x          - x coord
     * @param y          - y coord
     * @param z          - z coord
     * @param EnumFacing - direction the block will be placed against
     */
    public static boolean isBlockPlacableOnSide(World world, BlockPos pos, EnumFacing face, IBlockState state) {
        if (world.isAirBlock(pos)) return false;
        final Block b = (Block) world.getBlockState((BlockPos) state);
        if (b != null) {
            return b.isBlockSolid(world, pos, face);
        }
        return false;
    }

    /* An extension of EnumFacing, supports 26 directions */
    public enum BlockDirection {
        DOWN(0, -1, 0),
        UP(0, 1, 0),
        NORTH(0, 0, -1),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0),
        EAST(1, 0, 0),
        UNKNOWN(0, 0, 0),

        NORTH_WEST(-1, 0, -1),
        NORTH_EAST(1, 0, -1),
        SOUTH_WEST(-1, 0, 1),
        SOUTH_EAST(1, 0, 1),

        DOWN_NORTH(0, -1, -1),
        DOWN_SOUTH(0, -1, 1),
        DOWN_WEST(-1, -1, 0),
        DOWN_EAST(1, -1, 0),
        DOWN_NORTH_WEST(-1, -1, -1),
        DOWN_NORTH_EAST(1, -1, -1),
        DOWN_SOUTH_WEST(-1, -1, 1),
        DOWN_SOUTH_EAST(1, -1, 1),

        UP_NORTH(0, 1, -1),
        UP_SOUTH(0, 1, 1),
        UP_WEST(-1, 1, 0),
        UP_EAST(1, 1, 0),
        UP_NORTH_WEST(-1, 1, -1),
        UP_NORTH_EAST(1, 1, -1),
        UP_SOUTH_WEST(-1, 1, 1),
        UP_SOUTH_EAST(1, 1, 1);


        public final int offsetX;
        public final int offsetY;
        public final int offsetZ;
        public final int flag;

        BlockDirection(BlockPos pos) {
            offsetX = x;
            offsetY = y;
            offsetZ = z;
            flag = 1 << ordinal();
        }
    }
}
