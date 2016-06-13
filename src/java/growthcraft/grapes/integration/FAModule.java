package growthcraft.grapes.integration;

import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.api.core.item.ItemKey;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class FAModule extends FAModuleBase
{
	public FAModule()
	{
		super(GrowthCraftGrapes.MOD_ID);
	}
	
	public void addToItemMappings(Item ItemGrapes, int food, EnumDiet diet) {
		if (item != null) {
			switch (diet) {
			case HERBIVORE:
				if (herbivoreItemDiet == null) {
					herbivoreItemDiet = Maps.newHashMap();
				}
				if(!herbivoreItemDiet.containsKey(Item.getIdFromItem(ItemGrapes))){
					herbivoreItemDiet.put(Item.getIdFromItem(ItemGrapes), food);
				}
				break;
			default:
				break;
			}
		}
	}
	
	public int getItemFoodAmount(Item ItemGrapes, EnumDiet diet) {
	switch (diet) {
	case HERBIVORE:
			if (herbivoreItemDiet != null && herbivoreItemDiet.containsKey(Item.getIdFromItem(ItemGrapes))) {
				return herbivoreItemDiet.get(Item.getIdFromItem(ItemGrapes));
			}
			break;
		default:
			return 0;
		}
		return 0;
	}
}