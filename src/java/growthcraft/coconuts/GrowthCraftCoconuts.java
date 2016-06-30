package growthcraft.coconuts;

import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;

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
}