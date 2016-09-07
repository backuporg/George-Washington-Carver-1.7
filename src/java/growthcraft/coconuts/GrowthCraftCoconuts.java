package growthcraft.coconuts;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.creativetab.CreativeTabs;

import growthcraft.coconuts.creativetab.CreativeTabsGrowthcraftCoconuts;

@Mod(
	modid = GrowthCraftCoconuts.MOD_ID,
	name = GrowthCraftCoconuts.MOD_NAME,
	version = GrowthCraftCoconuts.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftCoconuts
{
	public static final String MOD_ID = "Growthcraft|Coconuts";
	public static final String MOD_NAME = "Growthcraft Coconuts";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftCoconuts instance;
	public static CreativeTabs creativeTab;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
	creativeTab = new CreativeTabsGrowthcraftCoconuts("creative_tab_grccoconuts");
	}
}