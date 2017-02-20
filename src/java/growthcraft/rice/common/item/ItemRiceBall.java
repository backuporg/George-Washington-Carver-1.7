package growthcraft.rice.common.item;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.item.GrcItemFoodBase;


public class ItemRiceBall extends GrcItemFoodBase {
    public ItemRiceBall() {
        super(5, 0.6F, false);
        this.setUnlocalizedName("grc.riceBall");
        this.setCreativeTab(GrowthCraftCore.creativeTab);
    }
}
