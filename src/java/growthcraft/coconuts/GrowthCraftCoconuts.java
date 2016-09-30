package growthcraft.coconuts;

import growthcraft.coconuts.creativetab.*;
import growthcraft.core.creativetab.CreativeTabsGrowthcraftCoconuts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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