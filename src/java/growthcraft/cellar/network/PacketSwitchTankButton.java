package growthcraft.cellar.network;

import growthcraft.cellar.common.tileentity.TileEntityBrewKettle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketSwitchTankButton extends AbstractPacketButton
{
	public PacketSwitchTankButton() {}

	public PacketSwitchTankButton(BlockPos pos)
	{
		super(pos);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{

	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player, BlockPos pos)
	{
		final World world = player.worldObj;
		final TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityBrewKettle)
		{
			((TileEntityBrewKettle)te).switchTanks();
		}
	}
}
