package growthcraft.apples.handler;

import growthcraft.apples.GrowthCraftApples;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

/**
 * Created by Alatyami on 8/30/2015.
 * Added per GitHub Issue #55
 */
public class AppleFuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack fuel) {
        if (fuel != null) {
            final Item item = fuel.getItem();
            if (GrowthCraftApples.blocks.appleSapling.getItem().equals(item)) {
                return 100;
            }
        }
        return 0;
    }
}
