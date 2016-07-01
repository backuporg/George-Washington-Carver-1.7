/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.netherloid;

import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.netherloid.client.event.TextureStitchEventHandler;
import growthcraft.netherloid.common.CommonProxy;
import growthcraft.netherloid.creativetab.CreativeTabsGrowthcraftNether;
import growthcraft.netherloid.init.netherloidItems;
import growthcraft.netherloid.init.netherloidFluids;
import growthcraft.netherloid.init.netherloidBlocks;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Mod(
	modid = netherloid.MOD_ID,
	name = netherloid.MOD_NAME,
	version = netherloid.MOD_VERSION,
	dependencies = netherloid.MOD_DEPENDENCIES
)
public class netherloid
{
	public static final String MOD_ID = "netherloid";
	public static final String MOD_NAME = "netherloid";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEPENDENCIES = "required-after:Growthcraft;required-after:Growthcraft|Cellar";

	@Instance(MOD_ID)
	public static netherloid instance;

	public static CreativeTabs tab;

	public static netherloidBlocks blocks = new netherloidBlocks();
	public static netherloidItems items = new netherloidItems();
	public static netherloidFluids fluids = new netherloidFluids();

	private ILogger logger = new GrcLogger(MOD_ID);
	private netherloidConfig config = new netherloidConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static netherloidConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		config.load(event.getModConfigurationDirectory(), "growthcraft/nether.conf");

		tab = new CreativeTabsGrowthcraftNether();

		modules.add(blocks);
		modules.add(items);
		modules.add(fluids);

		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.netherloid.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		modules.freeze();

		modules.preInit();
		modules.register();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.instance.initRenders();

		modules.init();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitchPost(TextureStitchEvent.Post event)
	{
		if (event.map.getTextureType() == 0)
		{
			for (Booze bz : fluids.fireBrandyBooze)
			{
				bz.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
			for (Booze bz : fluids.maliceCiderBooze)
			{
				bz.setIcons(GrowthCraftCore.liquidSmoothTexture);
			}
		}
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
