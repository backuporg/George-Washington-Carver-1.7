package growthcraft.bees.common.inventory;

import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.common.inventory.GrcContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;

public class ContainerBeeBox extends GrcContainer
{
	public static class SlotId
	{
		public static final int BEE = 0;
		public static final int HONEY_COMB_START = 1;
		public static final int HONEY_COMB_END = HONEY_COMB_START + 27;

		private SlotId() {}
	}

	private TileEntityBeeBox teBeeBox;

	public ContainerBeeBox(InventoryPlayer player, TileEntityBeeBox beeBox, BlockPos pos)
	{
		super(beeBox);
		// Slot Indices:
		// 0            bee
		// 1 - 27 (28)  honeycomb
		// 28 - 54 (55) player.inv.backpack
		// 55 - 63 (64) player.inv.hotbar

		this.teBeeBox = beeBox;
		addSlotToContainer(new SlotBee(this, teBeeBox, pos, SlotId.BEE));

		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(
					new SlotHoneyComb(
						this,
						teBeeBox,
						pos
					)
				);
			}
		}

		bindPlayerInventory(player, 8, 118);
	}
}
