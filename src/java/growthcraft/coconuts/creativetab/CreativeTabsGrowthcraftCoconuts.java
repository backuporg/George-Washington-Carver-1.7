package growthcraft.core.creativetab;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CreativeTabsGrowthcraftCoconuts extends CreativeTabs
{
	public CreativeTabsGrowthcraftCoconuts(String name)
	{
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return Item.getItemFromBlock(Blocks.SAND);
	}
}
