package growthcraft.bees.common.village;

import growthcraft.bees.GrowthCraftBees;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageTradeHandler;

import java.util.Random;

public class VillageHandlerBees implements IVillageTradeHandler
{
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 1 + random.nextInt(2)), GrowthCraftBees.fluids.honeyMeadBottle.asStack(1, 1)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2 + random.nextInt(2)), GrowthCraftBees.fluids.honeyMeadBottle.asStack(1, 2)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2 + random.nextInt(2)), GrowthCraftBees.fluids.honeyMeadBottle.asStack(1, 3)));
	}
}
