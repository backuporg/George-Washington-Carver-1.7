package growthcraft.grapes.integration;

import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.init.GrcGrapesItems;
import growthcraft.api.core.item.ItemKey;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInterModComms;

import java.util.Map;
import net.minecraft.client.Minecraft;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

import fossilsarcheology.api.EnumDiet;
import fossilsarcheology.api.FoodMappings;

public class FAModule extends FAModuleBase
{
	public FAModule()
	{
		super(GrowthCraftGrapes.MOD_ID);
	}
	public void addPlant(Object ItemGrapes, int food){
		if(ItemGrapes instanceof Block){
			this.addToBlockMappings((Block)ItemGrapes, 10, EnumDiet.HERBIVORE, true);
			this.addToBlockMappings((Block)ItemGrapes, 10, EnumDiet.OMNIVORE, true);
		}
		else if(ItemGrapes instanceof Item){
			this.addToItemMappings((Item)ItemGrapes, 10, EnumDiet.HERBIVORE);
			this.addToItemMappings((Item)ItemGrapes, 10, EnumDiet.OMNIVORE);
		}
		else if(ItemGrapes instanceof Class){
			this.addToEntityMappings((Class)ItemGrapes, 10, EnumDiet.HERBIVORE);
			this.addToEntityMappings((Class)ItemGrapes, 10, EnumDiet.OMNIVORE);
		}
	}
}