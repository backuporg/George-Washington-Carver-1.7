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
package growthcraft.netherloid.common.block;

import growthcraft.netherloid.netherloid;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockNetherCinderrot extends BlockNetherFungusBase
{
	private final float cinderrotSpreadRate = netherloid.getConfig().cinderrotSpreadRate;

	public BlockNetherCinderrot()
	{
		super();
		setUnlocalizedName("grcnetherloid.netherCinderrot");
		setCreativeTab(netherloid.tab);
		setBlockTextureName("grcnetherloid:cinderrot");
		getBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 0.375F, 0.625F);
	}

	@Override
	protected float getSpreadRate(World world, BlockPos pos)
	{
		return cinderrotSpreadRate;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		if (world.isRemote) return;
		entity.setFire(15);
	}
}
