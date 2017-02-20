/**
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 * <p>
 * NOTICE:
 * This file has been modified from its original source for use in
 * Growthcraft CE.
 * <p>
 * NOTICE:
 * This file has been modified from its original source for use in
 * Growthcraft CE.
 */
/**
 * NOTICE:
 *   This file has been modified from its original source for use in
 *   Growthcraft CE.
 */
package growthcraft.core.integration.forestry;

import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.Point3;
import growthcraft.core.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

/**
 * This was taken from Forestry
 */
@Optional.Interface(iface = "forestry.api.farming.IFarmable", modid = "ForestryAPI|farming")
public class FarmableBasicGrowthCraft implements IFarmable {
    private final Block block;
    private final int matureMeta;
    private final boolean isRice;
    private final boolean isGrape;

    public FarmableBasicGrowthCraft(Block pblock, int pmatureMeta, boolean pisRice, boolean pisGrape) {
        this.block = pblock;
        this.matureMeta = pmatureMeta;
        this.isRice = pisRice;
        this.isGrape = pisGrape;
    }

    @Override
    @Optional.Method(modid = "ForestryAPI|farming")
    public boolean isSaplingAt(World world, BlockPos pos) {
        return world.getBlockState(x, y, z) == block;
    }

    @Override
    @Optional.Method(modid = "ForestryAPI|farming")
    public ICrop getCropAt(World world, BlockPos pos) {
        if (world.getBlockState(x, y, z) != block) return null;
        if (world.getBlockState(x, y, z) != matureMeta) return null;
        return new CropBasicGrowthCraft(world, block, matureMeta, new Point3(x, y, z), isRice, isGrape);
    }

    @Override
    @Optional.Method(modid = "ForestryAPI|farming")
    public boolean isGermling(ItemStack stack) {
        return ItemUtils.equals(block, stack);
    }

    @Override
    @Optional.Method(modid = "ForestryAPI|farming")
    public boolean plantSaplingAt(EntityPlayer player, ItemStack germling, World world, BlockPos pos) {
        return world.setBlockState(x, y, z, block, 0, BlockFlags.SYNC);
    }

    @Override
    @Optional.Method(modid = "ForestryAPI|farming")
    public boolean isWindfall(ItemStack stack) {
        return false;
    }
}
