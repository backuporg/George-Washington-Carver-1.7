package growthcraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public interface IPaddy {
    @Nonnull
    Block getFluidBlock();

    @Nonnull
    Fluid getFillingFluid();

    int getMaxPaddyMeta(IBlockAccess world, BlockPos pos);

    boolean isFilledWithFluid(IBlockAccess world, BlockPos pos, int meta);

    boolean canConnectPaddyTo(IBlockAccess world, BlockPos pos, int meta);

    boolean isBelowFillingFluid(IBlockAccess world, BlockPos pos);
}
