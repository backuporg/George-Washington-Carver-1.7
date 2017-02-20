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

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.GrowthCraftMilk;
import growthcraft.milk.client.render.RenderCheesePress;
import growthcraft.milk.common.tileentity.TileEntityCheesePress;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCheesePress extends GrcBlockContainer {
    public BlockCheesePress() {
        super(Material.WOOD);
        setResistance(5.0F);
        setHardness(2.0F);
        //setStepSound(soundTypeWood);
        setUnlocalizedName("grcmilk.CheesePress");
        setCreativeTab(GrowthCraftMilk.creativeTab);
        setTileEntityType(TileEntityCheesePress.class);
        //setBlockTextureName("grcmilk:cheese_press");
    }

    @Override
    public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    public void doRotateBlock(World world, BlockPos pos, EnumFacing side) {
        //world.setBlockState(pos, world.getBlockState(pos) ^ 1, BlockFlags.SYNC);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        //this.setDefaultDirection(world, pos);
    }

    //private void setDefaultDirection(World world, BlockPos pos)
    //{
    //if (!world.isRemote)
    //{
            /*final Block block = world.getBlockState(x, y, z - 1);
            final Block block1 = world.getBlockState(x, y, z + 1);
			final Block block2 = world.getBlockState(x - 1, y, z);
			final Block block3 = world.getBlockState(x + 1, y, z);
			byte meta = 3;

			if (block.isFullBlock() && !block1.isFullBlock())
			{
				meta = 3;
			}

			if (block1.isFullBlock() && !block.isFullBlock())
			{
				meta = 2;
			}

			if (block2.isFullBlock() && !block3.isFullBlock())
			{
				meta = 5;
			}

			if (block3.isFullBlock() && !block2.isFullBlock())
			{
				meta = 4;
			}

			world.setBlockState(pos, meta, BlockFlags.UPDATE_AND_SYNC);
		}
	}*/

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack, IBlockState state) {
        super.onBlockPlacedBy(world, pos, entity, stack);
        final int a = MathHelper.floor_double((entity.rotationYaw * 4.0D / 360.0D) + 0.5D) & 3;
        if (a == 0 || a == 2) {
            world.setBlockState(pos, state, 0, BlockFlags.SYNC);
        } else if (a == 1 || a == 3) {
            world.setBlockState(pos, state, 1, BlockFlags.SYNC);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float par7, float par8, float par9) {
        if (super.onBlockActivated(world, pos, player, meta, par7, par8, par9)) return true;
        if (GrowthCraftMilk.getConfig().cheesePressHandOperated) {
            if (!player.isSneaking()) {
                final TileEntityCheesePress cheesePress = getTileEntity(world, pos);
                if (cheesePress != null) {
                    if (cheesePress.toggle()) {
                        world.playSoundEffect((double) x, (double) y, (double) z, "random.wood_click", 0.3f, 0.5f);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void updatePressState(World world, BlockPos pos) {
        final boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
        final TileEntityCheesePress cheesePress = getTileEntity(world, pos);
        if (cheesePress != null) {
            if (cheesePress.toggle(isPowered)) {
                world.playSoundEffect((double) x, (double) y, (double) z, "random.wood_click", 0.3f, 0.5f);
            }
        }
    }

    @Override
    public void onNeighborChange(World world, BlockPos pos, Block block, IBlockState state) {
        super.onNeighborChange(world, pos, pos);
        if (!world.isRemote) {
            if (GrowthCraftMilk.getConfig().cheesePressRedstoneOperated) {
                this.updatePressState(world, pos);
            }
        }
    }

    @Override
    public int getRenderType() {
        return RenderCheesePress.RENDER_ID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side) {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, BlockPos pos, int par5) {
        final TileEntityCheesePress te = getTileEntity(world, pos);
        if (te != null) {
            return Container.calcRedstoneFromInventory(te);
        }
        return 0;
    }
}
