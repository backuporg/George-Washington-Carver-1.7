package growthcraft.fishtrap;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.api.fishtrap.FishTrapEntry;
import growthcraft.api.fishtrap.user.UserFishTrapConfig;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.GrcGuiProvider;
import growthcraft.fishtrap.common.block.BlockFishTrap;
import growthcraft.fishtrap.common.CommonProxy;
import growthcraft.fishtrap.common.tileentity.TileEntityFishTrap;

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
	public static final GrcGuiProvider guiProvider = new GrcGuiProvider(new GrcLogger(MOD_ID + ":GuiProvider"));

	private final ILogger logger = new GrcLogger(MOD_ID);
	private final GrcFishtrapConfig config = new GrcFishtrapConfig();
	private final ModuleContainer modules = new ModuleContainer();
	private final UserFishTrapConfig userFishTrapConfig = new UserFishTrapConfig();

	public static GrcFishtrapConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/fishtrap.conf");
		if (config.debugEnabled) modules.setLogger(logger);
		userFishTrapConfig.setConfigFile(event.getModConfigurationDirectory(), "growthcraft/fishtrap/entries.json");
		modules.add(userFishTrapConfig);
		//if (config.enableThaumcraftIntegration) modules.add(new growthcraft.fishtrap.integration.ThaumcraftModule());
		modules.add(CommonProxy.instance);
		modules.freeze();
		fishTrap = new BlockDefinition(new BlockFishTrap());
		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(fishTrap.getBlock(), "grc.fishTrap");

		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "grc.tileentity.fishTrap");

		// Will use same chances as Fishing Rods
		//JUNK
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.LEATHER_BOOTS), 10).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.LEATHER), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.BONE), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.POTIONITEM), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.STRING), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.FISHING_ROD), 2).setDamage(0.9F));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.BOWL), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.STICK), 5));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.DYE, 10), 1));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Blocks.TRIPWIRE_HOOK), 10));
		userFishTrapConfig.addDefault("junk", new FishTrapEntry(new ItemStack(Items.ROTTEN_FLESH), 10));
		//TREASURE
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Blocks.WATERLILY), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.NAME_TAG), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.SADDLE), 1));
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.BOW), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.FISHING_ROD), 1).setDamage(0.25F).setEnchantable());
		userFishTrapConfig.addDefault("treasure", new FishTrapEntry(new ItemStack(Items.BOOK), 1).setEnchantable());
		//FISH
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.func_150976_a()), 60));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2));
		userFishTrapConfig.addDefault("fish", new FishTrapEntry(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13));

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(fishTrap.asStack(1), "ACA", "CBC", "ACA", 'A', "plankWood", 'B', Items.LEAD, 'C', Items.STRING));

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		userFishTrapConfig.loadUserConfig();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiProvider);
		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
