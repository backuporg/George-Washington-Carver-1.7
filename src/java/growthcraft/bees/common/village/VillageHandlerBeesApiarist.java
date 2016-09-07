package growthcraft.bees.common.village;

import java.util.Random;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.*;

import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraft.entity.passive.EntityVillager.EmeraldForItems;
import net.minecraft.entity.passive.EntityVillager.ListItemForEmeralds;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.entity.passive.EntityVillager.*;

import java.util.Map;

public class VillageHandlerBeesApiarist {

	public static final VillageHandlerBeesApiarist instance = new VillageHandlerBeesApiarist();

	public static final ResourceLocation PROFESSIONS = new ResourceLocation {"grcbees:apiarist"}
	public VillagerRegistry.VillagerProfession apiarist;

	public Map<Integer, VillagerRegistry.VillagerProfession> professions = Maps.newHashMap();

	//Secretly borrowed information from Alexthe666's Ice and Fire mod to set up professions properly

	{
	public static ();
		apiarist = new VillagerRegistry.VillagerProfession("grcbees:apiarist", "grcbees:textures/entity/apiarist.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(apiarist, "apiarist");
			career.addTrade(1, new ListItemForEmeralds(new ItemStack(GrowthCraftBees.items.honeyJar.asStack(), new PriceInfo(1, 2))));
			career.addTrade(1, new ListItemForEmeralds(new ItemStack(GrowthCraftBees.items.honeyCombFilled.asStack(), new PriceInfo(7))));
			career.addTrade(2, new ListItemForEmeralds(new ItemStack(GrowthCraftBees.items.bee.asStack(), new PriceInfo(3, 5))));
			career.addTrade(2, new ListItemForEmeralds(new ItemStack(GrowthCraftBees.items.beesWax.asStack(), new PriceInfo(1))));
			career.addTrade(1, new EmeraldForItems(new ItemStack(GrowthCraftBees.items.honeyJar.asStack(), new PriceInfo(1, 2))));
			career.addTrade(1, new EmeraldForItems(new ItemStack(GrowthCraftBees.items.honeyCombFilled.asStack(), new PriceInfo(7))));
			career.addTrade(2, new EmeraldForItems(new ItemStack(GrowthCraftBees.items.bee.asStack(), new PriceInfo(3, 5))));
			career.addTrade(2, new EmeraldForItems(new ItemStack(GrowthCraftBees.items.beesWax.asStack(), new PriceInfo(1))));
		}
	}
	public static void addVillagers ()
		{
			VillagerRegistry.instance().register(apiarist);
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
