package growthcraft.core.common.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Waila data provider for crop blocks
 */
public interface ICropDataProvider {
    float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta);
}
