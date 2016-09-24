package growthcraft.bees.util;

import growthcraft.bees.common.item.*;
import net.minecraft.item.Item;

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
