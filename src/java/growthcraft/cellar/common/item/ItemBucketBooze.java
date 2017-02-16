package growthcraft.cellar.common.item;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.BoozeUtils;
import growthcraft.core.common.item.ItemBucketFluid;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBucketBooze extends ItemBucketFluid {
    public ItemBucketBooze(Block block, Fluid buze, CreativeTabs creativeTab) {
        super(block, buze, creativeTab);
        setUnlocalizedName("grc.boozeBucket");
    }

    public ItemBucketBooze(Block block, Fluid buze) {
        this(block, buze, GrowthCraftCellar.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        super.addInformation(stack, player, list, bool);
        BoozeUtils.addInformation(getFluid(stack), stack, player, list, bool);
    }
}
