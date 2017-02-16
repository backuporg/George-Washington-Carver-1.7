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
package growthcraft.cellar.eventhandler;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerCauldronUseItem {
    @SubscribeEvent
    public void handle(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (event.getEntityPlayer() == null) return;
        if (event.getWorld() == null) return;
        if (event.getWorld().isRemote) return;

        // if you need more items, be sure to change this event to pick items
        // from a HashMap, however for now, I'll just hard code the WaterBag
        final Block target = event.getWorld().getBlockState(event.x, event.y, event.z);

        if (target instanceof BlockCauldron) {
            final BlockCauldron cauldron = (BlockCauldron) target;
            final ItemStack itemstack = event.getEntityPlayer().getCurrentEquippedItem();

            if (itemstack == null) return;

            if (GrowthCraftCellar.items.waterBag.equals(itemstack.getItem())) {
                final int meta = event.getWorld().getBlockState(event.x, event.y, event.z);
                if (meta > 0) {
                    event.setCanceled(true);
                    // 16 - is the default water bag color
                    itemstack.setItemDamage(16);
                    ItemUtils.replacePlayerCurrentItem(event.entityPlayer, itemstack);
                    cauldron.setWaterLevel(event.getWorld(), event.x, event.y, event.z, meta - 1);
                }
            }
        }
    }
}
