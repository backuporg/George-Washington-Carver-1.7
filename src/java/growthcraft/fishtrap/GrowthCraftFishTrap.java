package growthcraft.fishtrap;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.user.UserBaitConfig;
import growthcraft.api.fishtrap.user.UserCatchGroupConfig;
import growthcraft.api.fishtrap.user.UserFishTrapConfig;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.GrcGuiProvider;
import growthcraft.fishtrap.common.block.BlockFishTrap;
import growthcraft.fishtrap.common.CommonProxy;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;
import growthcraft.fishtrap.creativetab.CreativeTabsGrowthcraftFishtrap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftFishTrap.MOD_ID,
	name = GrowthCraftFishTrap.MOD_NAME,
	version = GrowthCraftFishTrap.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@"
)
public class GrowthCraftFishTrap
{
	public static final String MOD_ID = "Growthcraft|Fishtrap";
	public static final String MOD_NAME = "Growthcraft Fishtrap";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftFishTrap instance;
	public static BlockDefinition fishTrap;
	public static CreativeTabs creativeTab;
	public static final GrcGuiProvider guiProvider = new GrcGuiProvider(new GrcLogger(MOD_ID + ":GuiProvider"));

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcFishtrapConfig config = new GrcFishtrapConfig();
	private final ModuleContainer modules = new ModuleContainer();
	private final UserBaitConfig userBaitConfig = new UserBaitConfig();
	private final UserCatchGroupConfig userCatchGroupConfig = new UserCatchGroupConfig();
	private final UserFishTrapConfig userFishTrapConfig = new UserFishTrapConfig();

	public static GrcFishtrapConfig getConfig()
	{
		return instance.config;
	}

	public static ILogger getLogger()
	{
		return instance.logger;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/fishtrap.conf");
		userBaitConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/baits.json");
		userCatchGroupConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/catch_groups.json");
		userFishTrapConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/entries.json");
		modules.add(userBaitConfig);
		modules.add(userCatchGroupConfig);
		modules.add(userFishTrapConfig);
		//if (config.enableThaumcraftIntegration) modules.add(new growthcraft.fishtrap.integration.ThaumcraftModule());
		modules.add(CommonProxy.instance);
		if (config.debugEnabled) modules.setLogger(logger);
		modules.freeze();
		creativeTab = new CreativeTabsGrowthcraftFishtrap("creative_tab_grcfishtrap");
		fishTrap = new BlockDefinition(new BlockFishTrap());
		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fishTrap.getBlockState(), "grc.fishTrap");

		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "grc.tileentity.fishTrap");

		// Gives a +0.2 bonus on base and a 1.1f bonus multiplier
		userBaitConfig.addDefault(new ItemStack(Items.rotten_flesh), 0.2f, 1.1f);

		// Catch Groups
		userCatchGroupConfig.addDefault("junk", 3, "Useless Stuff");
		userCatchGroupConfig.addDefault("treasure", 5, "Fancy Stuff");
		userCatchGroupConfig.addDefault("fish", 1, "Fishes");
		userCatchGroupConfig.addDefault("mineral", 7, "Ingots and other metallic stuff");
		userCatchGroupConfig.addDefault("legendary", 10, "Stuff you probably would never find on average");

		// Will use same chances as Fishing Rods
		// Junk
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.LEATHER_BOOTS), 10).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.LEATHER), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.BONE), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.POTIONITEM), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.STRING), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.FISHING_ROD), 2).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.BOWL), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.STICK), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.DYE, 10), 1));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.ROTTEN_FLESH), 10));
		// Treasure
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Blocks.WATERLILY), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.NAME_TAG), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.SADDLE), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.BOW), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.FISHING_ROD), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.BOOK), 1).setEnchantable());
		// Fishes
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.func_150976_a()), 60));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13));
		// Minerals
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Blocks.TRIPWIRE_HOOK), 1));
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Items.IRON_INGOT), 10));
		userFishTrapConfig.addDefault("mineral", new FishTrapEntry(new ItemStack(Items.GOLD_NUGGET), 5));
		// Legendary
		userFishTrapConfig.addDefault("legendary", new FishTrapEntry(new ItemStack(Items.GOLD_INGOT), 1));
		userFishTrapConfig.addDefault("legendary", new FishTrapEntry(new ItemStack(Items.DIAMOND), 10));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fishTrap.asStack(1), "ACA", "CBC", "ACA", 'A', "plankWood", 'B', Items.LEAD, 'C', Items.STRING));

		modules.register();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		userBaitConfig.loadUserConfig();
		userCatchGroupConfig.loadUserConfig();
		userFishTrapConfig.loadUserConfig();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiProvider);
		modules.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
