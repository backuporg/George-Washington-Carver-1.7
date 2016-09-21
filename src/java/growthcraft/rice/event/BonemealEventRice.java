package growthcraft.rice.event;

import java.util.Random;

import growthcraft.rice.GrowthCraftRice;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.AuxFX;
import growthcraft.rice.util.RiceBlockCheck;

import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class BonemealEventRice
{
	private void appleBonemealEffect(World world, Random rand, BlockPos pos)
	{
		final int r =  MathHelper.getRandomIntegerInRange(rand, 2, 5);
		int mplus;
		int mminus;

		for (int i = x - 1; i <= x + 1; ++i)
		{
			for (int k = z - 1; k <= z + 1; ++k)
			{
				final boolean isRiceBlock = (GrowthCraftRice.blocks.riceBlock.getBlockState() == world.getBlockState(i, y, k)) &&
					(world.getBlockState(i, y, k) != 7);
				final boolean isPaddyBelow = RiceBlockCheck.isPaddy(world.getBlockState(i, y - 1, k)) &&
					(world.getBlockState(i, y - 1, k) != 0);

				if (isRiceBlock && isPaddyBelow)
				{
					mplus = world.getBlockState(i, y, k) + r;
					mminus = world.getBlockState(i, y - 1, k) - r;
					if (mplus > 7)
					{
						mplus = 7;
					}
					if (mminus < 0)
					{
						mminus = 0;
					}
					world.setBlockState(i, y, k, mplus, BlockFlags.SYNC);
					world.setBlockState(i, y - 1, k, mminus, BlockFlags.SYNC);
					world.playAuxSFX(AuxFX.BONEMEAL, i, y, k, 0);
					world.notifyBlockChange(i, y, k, Blocks.AIR);
					world.notifyBlockChange(i, y - 1, k, Blocks.AIR);
				}
			}
		}
	}

	@SubscribeEvent
	public void onUseBonemeal(BonemealEvent event)
	{
		if (GrowthCraftRice.blocks.riceBlock.getBlockState() == event.block)
		{
			if (!event.world.isRemote)
			{
				this.appleBonemealEffect(event.world, event.world.rand, event.x, event.y, event.z);
			}
			event.setResult(Result.ALLOW);
		}
	}
}
