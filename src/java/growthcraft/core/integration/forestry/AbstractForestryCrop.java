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
 */
/**
 * NOTICE:
 *   This file has been modified from its original source for use in
 *   Growthcraft CE.
 */
package growthcraft.core.integration.forestry;

import forestry.api.farming.ICrop;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.Point3;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.Collection;

/**
 * This is a copy of Forestry's Crop implementation, modified for Growthcraft
 */
@Optional.Interface(iface = "forestry.api.farming.ICrop", modid = "ForestryAPI|farming")
public abstract class AbstractForestryCrop implements ICrop {
    protected final World world;
    protected final Point3 position;

    public AbstractForestryCrop(World pworld, Point3 pos) {
        this.world = pworld;
        this.position = pos;
    }

    protected final void setBlockState(Point3 pos, Block block, int meta) {
        world.setBlockState(pos.x, pos.y, pos.z, block, meta, BlockFlags.SYNC);
    }

    protected final Block getBlockState(Point3 pos) {
        return world.getBlockState(pos.x, pos.y, pos.z);
    }

    protected final int getBlockMeta(Point3 pos) {
        return world.getBlockState(pos.x, pos.y, pos.z);
    }

    protected abstract boolean isCrop(Point3 pos);

    protected abstract Collection<ItemStack> harvestBlock(Point3 pos);

    @Override
    public Collection<ItemStack> harvest() {
        if (!isCrop(position)) return null;
        return harvestBlock(position);
    }
}
