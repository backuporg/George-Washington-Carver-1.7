package growthcraft.hops.common.item;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.item.GrcItemBase;


public class ItemHops extends GrcItemBase {
    public ItemHops() {
        super();
        this.setUnlocalizedName("grc.hops");
        this.setCreativeTab(GrowthCraftCore.creativeTab);
    }

    /************
     * TEXTURES
     ************/
    //@Override
    //@SideOnly(Side.CLIENT)
    //public void registerIcons(IIconRegister reg)
    //{
    //	this.itemIcon = reg.registerIcon("grchops:hops");
    //}
}
