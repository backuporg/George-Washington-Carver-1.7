package growthcraft.bees.creativetab;

import growthcraft.bees.GrowthCraftBees;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabsGrowthcraftBees extends CreativeTabs
{
	public CreativeTabsGrowthcraftBees(String name)
	{
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return GrowthCraftBees.items.bee.getItem();
	}
}
