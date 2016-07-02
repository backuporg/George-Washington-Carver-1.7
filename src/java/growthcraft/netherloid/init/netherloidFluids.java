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
package growthcraft.netherloid.init;

import growthcraft.api.cellar.booze.Booze;
import growthcraft.api.cellar.booze.BoozeTag;
import growthcraft.api.cellar.common.Residue;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.core.GrowthCraftCore;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.netherloid.netherloid;
import growthcraft.api.core.item.OreItemStacks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidStack;

public class netherloidFluids extends GrcModuleBase
{
	public Booze[] maliceCiderBooze;
	public BlockBoozeDefinition[] maliceCiderFluids;
	public ItemDefinition maliceCider;
	public ItemBucketBoozeDefinition[] maliceCiderBuckets;
	public ItemDefinition fireBrandy;
	public ItemBucketBoozeDefinition[] fireBrandyBuckets;
	public BlockBoozeDefinition[] fireBrandyFluids;
	public Booze[] fireBrandyBooze;

	@Override
	public void preInit()
	{
		this.maliceCiderBooze = new Booze[100];
		this.maliceCiderFluids = new BlockBoozeDefinition[maliceCiderBooze.length];
		this.maliceCiderBuckets = new ItemBucketBoozeDefinition[maliceCiderBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.maliceCider", maliceCiderBooze);
		for (Booze booze : maliceCiderBooze)
		{
			booze.setColor(netherloid.getConfig().maliceCiderColor).setDensity(1120);
		}
		BoozeRegistryHelper.initializeBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(maliceCiderBooze, 1, -0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(maliceCiderBooze[0], 1, 0.3f);

		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(maliceCiderBooze));
		
		this.fireBrandyBooze = new Booze[100];
		this.fireBrandyFluids = new BlockBoozeDefinition[fireBrandyBooze.length];
		this.fireBrandyBuckets = new ItemBucketBoozeDefinition[fireBrandyBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.fireBrandy", fireBrandyBooze);
		for (Booze booze : fireBrandyBooze)
		{
			booze.setColor(netherloid.getConfig().fireBrandyColor).setDensity(1120);
		}
		BoozeRegistryHelper.initializeBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(fireBrandyBooze, 1, -0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(fireBrandyBooze[0], 1, 0.3f);
		
		this.fireBrandy = new ItemDefinition(new ItemBoozeBottle(fireBrandyBooze));
		
	}

	private void registerMaliceCider()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[maliceCiderBooze.length];
		for (int i = 0; i < maliceCiderBooze.length; ++i)
		{
			fs[i] = new FluidStack(maliceCiderBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.INFERNAL)
			.pressesFrom(
				new OreItemStacks("fruitMalice"),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F));

		// Brewers Yeast, Nether Wart
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[1])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.minutes(3), 0)
				.addPotionEntry(Potion.damageBoost, TickUtils.minutes(1), 1);

		// Glowstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[2])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POTENT, BoozeTag.INFERNAL)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.07f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.minutes(1), 1)
				.addPotionEntry(Potion.damageBoost, TickUtils.minutes(1), 2);

		// Redstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[3])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.EXTENDED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.minutes(6), 0)
				.addPotionEntry(Potion.damageBoost, TickUtils.minutes(2), 1);

		// Amrita - Ethereal Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[4])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.053f), TickUtils.seconds(90))
				.addPotionEntry(Potion.regeneration, TickUtils.minutes(6), 1)
				.addPotionEntry(Potion.damageBoost, TickUtils.minutes(2), 2);

		// Intoxicated
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[6])
			.tags(BoozeTag.WINE, BoozeTag.FERMENTED, BoozeTag.INTOXICATED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.regeneration.id, TickUtils.minutes(3), 0)))
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.damageBoost.id, TickUtils.minutes(1), 1)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.poison.id, TickUtils.minutes(3), 1)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(Potion.weakness.id, TickUtils.minutes(1), 1))));

		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[7])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POISONED, BoozeTag.INFERNAL)
			.fermentsTo(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[4], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[6], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.createPotionEntry(Potion.poison, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
	}

	private void registerFireBrandy()
	{
		final int fermentTime = GrowthCraftCellar.getConfig().fermentTime;
		final FluidStack[] fs = new FluidStack[fireBrandyBooze.length];
		for (int i = 0; i < fs.length; ++i)
		{
			fs[i] = new FluidStack(fireBrandyBooze[i], 1);
		}

		GrowthCraftCellar.boozeBuilderFactory.create(fireBrandyBooze[0])
			.tags(BoozeTag.YOUNG, BoozeTag.INFERNAL)
			.pressesFrom(
				new OreItemStacks("ghostChili"),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F));

		GrowthCraftCellar.boozeBuilderFactory.create(fireBrandyBooze[1])
			.tags(BoozeTag.FERMENTED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.nether_wart), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(Potion.fireResistance, TickUtils.seconds(350), 0);
	}
	
	private void registerFermentations()
	{
		registerMaliceCider();
		registerFireBrandy();
		
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (Booze booze : fireBrandyBooze)
			{
				booze.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
			for (Booze booze : maliceCiderBooze)
			{
				booze.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@Override
	public void register()
	{
		maliceCider.register("grc.maliceCider");
		fireBrandy.register("grc.fireBrandy");

		BoozeRegistryHelper.registerBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets, maliceCider, "grc.maliceCider", null);
		BoozeRegistryHelper.registerBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets, fireBrandy, "grc.fireBrandy", null);
		registerFermentations();
	}
}