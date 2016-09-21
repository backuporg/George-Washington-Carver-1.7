package growthcraft.core.integration.nei;

import codechicken.nei.api.API;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.integration.NEIModuleBase;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NEIModule extends NEIModuleBase
{
	public NEIModule()
	{
		super(GrowthCraftCore.MOD_ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid=NEIPlatform.MOD_ID)
	public void integrateClient()
	{
		API.registerRecipeHandler(new RecipeHandlerShapelessMulti());
		API.registerUsageHandler(new RecipeHandlerShapelessMulti());
		
		API.registerRecipeHandler(new RecipeHandlerShapedMulti());
		API.registerUsageHandler(new RecipeHandlerShapedMulti());
	}
}
