package growthcraft.core.utils;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public final class MapGenHelper
{
	public static void registerVillageStructure(Class<? extends StructureVillagePieces.Village> structure, String name)
	{
		try
		{
			MapGenStructureIO.func_143031_a(structure, name);
		}
		catch (Throwable e) {}
	}
}
