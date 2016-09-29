package growthcraft.cellar.common.inventory.slot;

import growthcraft.core.common.inventory.slot.SlotInput;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SlotInputYeast extends SlotInput
{
	public SlotInputYeast(IInventory inv, BlockPos pos)
	{
		super(inv, pos);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}
}
