package growthcraft.bees.common.village;

import java.util.Random;
import java.util.List;

import growthcraft.bees.GrowthCraftBees;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.item.Item;
import growthcraft.bees.common.item.*;
import growthcraft.bees.init.*;

public class VillageHandlerBeesApiarist implements IVillageTradeHandler, IVillageCreationHandler
{
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.honeyJar.asStack(1, 2)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.honeyCombFilled.asStack(1, 7)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.bee.asStack(1, 7)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.beesWax.asStack(1, 5)));
		recipeList.add(new MerchantRecipe(new Item(GrowthCraftBees.items.honeyJar, 4 +random.nextInt(2)), new ItemStack(Items.emerald, 2, 3)));
		recipeList.add(new MerchantRecipe(new Item(GrowthCraftBees.items.honeyCombFilled, 4 + random.nextInt(2)), new ItemStack(Items.emerald, 2, 3)));
		recipeList.add(new MerchantRecipe(new Item(GrowthCraftBees.items.bee, 4 + random.nextInt(2)), new ItemStack(Items.emerald, 1, 2)));
		recipeList.add(new MerchantRecipe(new Item(GrowthCraftBees.items.beesWax, 1 + random.nextInt(2)), new ItemStack(Items.emerald, 2, 4)));
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i)
	{
		int num = MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i);
		if (!GrowthCraftBees.getConfig().generateApiaristStructure)
			num = 0;

		return new PieceWeight(ComponentVillageApiarist.class, 21, num);
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageApiarist.class;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
	{
		return ComponentVillageApiarist.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
}
