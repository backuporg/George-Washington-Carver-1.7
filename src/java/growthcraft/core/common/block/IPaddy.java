package growthcraft.core.common.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public interface IPaddy
{
	@Nonnull public Block getFluidBlock();
	@Nonnull public Fluid getFillingFluid();
	public int getMaxPaddyMeta(IBlockAccess world, BlockPos pos);
	public boolean isFilledWithFluid(IBlockAccess world, BlockPos pos, int meta);
	public boolean canConnectPaddyTo(IBlockAccess world, BlockPos pos, int meta);
	public boolean isBelowFillingFluid(IBlockAccess world, BlockPos pos);
}
