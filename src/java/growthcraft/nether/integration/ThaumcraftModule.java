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
package growthcraft.nether.integration;

import growthcraft.nether.Nether;
import growthcraft.core.integration.ThaumcraftModuleBase;
import growthcraft.cellar.integration.ThaumcraftBoozeHelper;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;

public class ThaumcraftModule extends ThaumcraftModuleBase
{
	public ThaumcraftModule()
	{
		super(Nether.MOD_ID);
	}

	@Override
	protected void integrate()
	{
		ThaumcraftApi.registerObjectTag(Nether.items.ectoplasm.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherMaliceFruit.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherMuertecap.asStack(), new AspectList().add(Aspect.DEATH, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherPepper.asStack(), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherRashSpores.asStack(), new AspectList().add(Aspect.POISON, 1).add(Aspect.DEATH, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherSquashSeeds.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.items.netherGhastPowder.asStack(), new AspectList().add(Aspect.FIRE, 1));

		ThaumcraftApi.registerObjectTag(Nether.blocks.netherBaalsRot.asStack(), new AspectList().add(Aspect.POISON, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherCinderrot.asStack(), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherFireLily.asStack(), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherKnifeBush.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMaliceFruit.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMaliceLeaves.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMaliceLog.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMalicePlanks.asStack(), new AspectList().add(Aspect.TREE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMaliceSapling.asStack(), new AspectList().add(Aspect.PLANT, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMaraLotus.asStack(), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherMuertecap.asStack(), new AspectList().add(Aspect.DEATH, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherPaddyField.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherPaddyFieldFilled.asStack(), new AspectList().add(Aspect.SOUL, 1).add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherPepper.asStack(), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherSquash.asStack(), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftApi.registerObjectTag(Nether.blocks.netherSquashStem.asStack(), new AspectList().add(Aspect.SOUL, 1));

		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.fireBrandy.asStack(1, 0), new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.fireBrandy.asStack(1, 1), new AspectList().add(Aspect.FIRE, 1));
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.fireBrandy.asStack(1, 2), new AspectList().add(Aspect.FIRE, 2));
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.fireBrandy.asStack(1, 3), new AspectList().add(Aspect.FIRE, 1));

		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.fireBrandyBuckets[0], new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.fireBrandyBuckets[1], new AspectList().add(Aspect.FIRE, 3));
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.fireBrandyBuckets[2], new AspectList().add(Aspect.FIRE, 6));
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.fireBrandyBuckets[3], new AspectList().add(Aspect.FIRE, 3));

		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.fireBrandyFluids[0], new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.fireBrandyFluids[1], new AspectList().add(Aspect.FIRE, 3));
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.fireBrandyFluids[2], new AspectList().add(Aspect.FIRE, 6));
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.fireBrandyFluids[3], new AspectList().add(Aspect.FIRE, 3));

		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.maliceCider.asStack(1, 0), new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.maliceCider.asStack(1, 1), new AspectList().add(Aspect.SOUL, 1));
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.maliceCider.asStack(1, 2), new AspectList().add(Aspect.SOUL, 2));
		ThaumcraftBoozeHelper.instance().registerAspectsForBottleStack(Nether.fluids.maliceCider.asStack(1, 3), new AspectList().add(Aspect.SOUL, 1));

		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.maliceCiderBuckets[0], new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.maliceCiderBuckets[1], new AspectList().add(Aspect.SOUL, 3));
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.maliceCiderBuckets[2], new AspectList().add(Aspect.SOUL, 6));
		ThaumcraftBoozeHelper.instance().registerAspectsForBucket(Nether.fluids.maliceCiderBuckets[3], new AspectList().add(Aspect.SOUL, 3));

		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.maliceCiderFluids[0], new AspectList());
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.maliceCiderFluids[1], new AspectList().add(Aspect.SOUL, 3));
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.maliceCiderFluids[2], new AspectList().add(Aspect.SOUL, 6));
		ThaumcraftBoozeHelper.instance().registerAspectsForFluidBlock(Nether.fluids.maliceCiderFluids[3], new AspectList().add(Aspect.SOUL, 3));
	}
}
