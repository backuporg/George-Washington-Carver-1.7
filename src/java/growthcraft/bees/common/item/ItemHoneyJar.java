package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemFoodBase;
import net.minecraft.init.Items;

public class ItemHoneyJar extends GrcItemFoodBase {
    public ItemHoneyJar() {
        super(6, false);
        this.setUnlocalizedName("grc.honeyJar");
        this.setCreativeTab(GrowthCraftBees.tab);
        this.setContainerItem(Items.FLOWER_POT);
        this.setMaxStackSize(1);
    }

    public ItemHoneyJar(String honey_jar) {
        super();
    }

    //@Override
    //@SideOnly(Side.CLIENT)
    //public void registerIcons(IIconRegister reg)
    //{
    //	this.itemIcon = reg.registerIcon("grcbees:honeyjar");
    //}
}
