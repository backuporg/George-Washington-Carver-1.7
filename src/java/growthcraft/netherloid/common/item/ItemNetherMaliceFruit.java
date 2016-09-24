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
package growthcraft.netherloid.common.item;

import growthcraft.netherloid.netherloid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemNetherMaliceFruit extends ItemFood
{
	public ItemNetherMaliceFruit()
	{
		super(2, 0.3F, false);
		setUnlocalizedName("grcnetherloid.netherMaliceFruit");
		setTextureName("grcnetherloid:malice_fruit");
		setCreativeTab(netherloid.tab);
		setAlwaysEdible();
	}

	protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 20 * 20, 3));
			player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 40 * 20, 0));
		}
	}
}
