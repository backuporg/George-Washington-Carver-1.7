package growthcraft.bees.util;

import growthcraft.bees.common.item.ItemBee;
import growthcraft.bees.common.item.ItemBeesWax;
import growthcraft.bees.common.item.ItemBlockBeeBox;
import growthcraft.bees.common.item.ItemHoneyCombEmpty;
import growthcraft.bees.common.item.ItemHoneyCombFilled;
import growthcraft.bees.common.item.ItemHoneyJar;
import growthcraft.bees.common.item.EnumBeesWax;
import growthcraft.bees.init.GrcBeesItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BeesItemList {

    public static Item ItemBeesWax;
    public static Item ItemBee;
    public static Item ItemBlockBeeBox;
    public static Item ItemHoneyCombEmpty;
    public static Item ItemHoneyCombFilled;
    public static Item ItemHoneyJar;

    public static void init() {
        ItemBee = new ItemBee("bee");
        ItemBeesWax = new ItemBeesWax("beeswax");
        ItemBlockBeeBox = new ItemBlockBeeBox("beebox");
        ItemHoneyCombEmpty = new ItemHoneyCombEmpty("empty_honeycomb");
        ItemHoneyCombFilled = new ItemHoneyCombFilled("full_honeycomb");
        ItemHoneyJar = new ItemHoneyJar("honey_jar");
    }
}
