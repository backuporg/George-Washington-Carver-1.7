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
import growthcraft.api.core.effect.EffectAddPotionEffect;
import growthcraft.api.core.effect.EffectWeightedRandomList;
import growthcraft.api.core.effect.SimplePotionEffectFactory;
import growthcraft.api.core.item.OreItemStacks;
import growthcraft.api.core.util.TickUtils;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.common.definition.BlockBoozeDefinition;
import growthcraft.cellar.common.definition.ItemBucketBoozeDefinition;
import growthcraft.cellar.common.item.ItemBoozeBottle;
import growthcraft.cellar.util.BoozeRegistryHelper;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.GrcModuleBase;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.netherloid.netherloid;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
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
		this.maliceCiderBooze = new Booze[8];
		this.maliceCiderFluids = new BlockBoozeDefinition[maliceCiderBooze.length];
		this.maliceCiderBuckets = new ItemBucketBoozeDefinition[maliceCiderBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.maliceCider", maliceCiderBooze);
		for (Booze booze : maliceCiderBooze)
		{
			booze.setColor(netherloid.getConfig().maliceCiderColor).setDensity(1120);
		}
		BoozeRegistryHelper.initializeBooze(maliceCiderBooze, maliceCiderFluids, maliceCiderBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(maliceCiderBooze, 1, -0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(maliceCiderBooze[1], 1, 0.3f);

		this.maliceCider = new ItemDefinition(new ItemBoozeBottle(maliceCiderBooze));
		
		this.fireBrandyBooze = new Booze[8];
		this.fireBrandyFluids = new BlockBoozeDefinition[fireBrandyBooze.length];
		this.fireBrandyBuckets = new ItemBucketBoozeDefinition[fireBrandyBooze.length];
		BoozeRegistryHelper.initializeBoozeFluids("grc.fireBrandy", fireBrandyBooze);
		for (Booze booze : fireBrandyBooze)
		{
			booze.setColor(netherloid.getConfig().fireBrandyColor).setDensity(1120);
		}
		BoozeRegistryHelper.initializeBooze(fireBrandyBooze, fireBrandyFluids, fireBrandyBuckets);
		BoozeRegistryHelper.setBoozeFoodStats(fireBrandyBooze, 1, -0.3f);
		BoozeRegistryHelper.setBoozeFoodStats(fireBrandyBooze[1], 1, 0.3f);
		
		maliceCiderBooze[4].setColor(netherloid.getConfig().amritaColor);
		maliceCiderFluids[4].getBlockState().refreshColor();
		maliceCiderBooze[5].setColor(netherloid.getConfig().gelidBoozeColor);
		maliceCiderFluids[5].getBlockState().refreshColor();
		
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
				new OreItemStacks("fruitMalum"),
				TickUtils.seconds(2),
				40,
				Residue.newDefault(0.3F));

		// Brewers Yeast, Nether Wart
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[1])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[0], new OreItemStacks("yeastBrewers"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.NETHER_WART), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(MobEffects.REGENERATION, TickUtils.minutes(3), 0)
				.addPotionEntry(MobEffects.STRENGTH, TickUtils.minutes(1), 1);

		// Glowstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[2])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POTENT, BoozeTag.INFERNAL)
			.fermentsFrom(fs[1], new OreItemStacks("dustGlowstone"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("dustGlowstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.07f), TickUtils.seconds(90))
				.addPotionEntry(MobEffects.REGENERATION, TickUtils.minutes(1), 1)
				.addPotionEntry(MobEffects.STRENGTH, TickUtils.minutes(1), 2);

		// Redstone Dust
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[3])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.EXTENDED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[1], new OreItemStacks("dustRedstone"), fermentTime)
			.fermentsFrom(fs[2], new OreItemStacks("dustRedstone"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(MobEffects.REGENERATION, TickUtils.minutes(6), 0)
				.addPotionEntry(MobEffects.STRENGTH, TickUtils.minutes(2), 1);

		// Amrita - Ethereal Yeast
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[4])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.HYPER_EXTENDED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[2], new OreItemStacks("yeastEthereal"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastEthereal"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.053f), TickUtils.seconds(90))
				.addPotionEntry(MobEffects.REGENERATION, TickUtils.minutes(6), 1)
				.addPotionEntry(MobEffects.STRENGTH, TickUtils.minutes(2), 2);
				
		// Gelid Booze [WIP]
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[5])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INFERNAL, BoozeTag.CHILLED)
			.fermentsFrom(fs[0], new OreItemStacks("yeastLager"), fermentTime)
			.fermentsFrom(fs[0], new ItemStack(Items.NETHER_WART), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.addPotionEntry(MobEffects.REGENERATION, TickUtils.minutes(3), 0)
				.addPotionEntry(MobEffects.STRENGTH, TickUtils.minutes(1), 1);

		// Intoxicated
		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[6])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.INTOXICATED, BoozeTag.INFERNAL)
			.fermentsFrom(fs[2], new OreItemStacks("yeastOrigin"), fermentTime)
			.fermentsFrom(fs[3], new OreItemStacks("yeastOrigin"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.15f), TickUtils.seconds(90))
				.addEffect(new EffectWeightedRandomList()
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(MobEffects.REGENERATION, TickUtils.minutes(3), 0)))
					.add(8, new EffectAddPotionEffect(new SimplePotionEffectFactory(MobEffects.STRENGTH, TickUtils.minutes(1), 1)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(MobEffects.POISON, TickUtils.minutes(3), 1)))
					.add(2, new EffectAddPotionEffect(new SimplePotionEffectFactory(MobEffects.WEAKNESS, TickUtils.minutes(1), 1))));

		GrowthCraftCellar.boozeBuilderFactory.create(maliceCiderBooze[7])
			.tags(BoozeTag.CIDER, BoozeTag.FERMENTED, BoozeTag.POISONED, BoozeTag.INFERNAL)
			.fermentsTo(fs[1], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[2], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[3], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[4], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[5], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[6], new OreItemStacks("yeastPoison"), fermentTime)
			.fermentsTo(fs[7], new OreItemStacks("yeastPoison"), fermentTime)
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.05f), TickUtils.seconds(90))
				.createPotionEntry(MobEffects.POISON, TickUtils.seconds(90), 0).toggleDescription(!GrowthCraftCore.getConfig().hidePoisonedBooze);
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
			.fermentsFrom(fs[0], new ItemStack(Items.NETHER_WART), (int)(fermentTime * 0.66))
			.getEffect()
				.setTipsy(BoozeUtils.alcoholToTipsy(0.0419f), TickUtils.seconds(45))
				.addPotionEntry(MobEffects.FIRE_RESISTANCE, TickUtils.seconds(350), 0);
	}
	
	private void registerFermentations()
	{
		registerMaliceCider();
		registerFireBrandy();
		
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
