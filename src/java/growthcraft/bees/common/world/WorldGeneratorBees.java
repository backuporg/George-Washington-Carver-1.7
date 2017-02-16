package growthcraft.bees.common.world;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.Utils;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGeneratorBees implements IWorldGenerator {
    //constants
    //private final int rarity = 8;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0) {
            generateSurface(world, random, chunkX, chunkZ);
        }
    }

    private void generateSurface(World world, Random random, int chunkX, int chunkZ) {
        if (!world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat")) {
            final int i = chunkX * 16 + random.nextInt(16) + 8;
            final int j = random.nextInt(128);
            final int k = chunkZ * 16 + random.nextInt(16) + 8;

            boolean flag = true;
            if (GrowthCraftBees.getConfig().useBiomeDict) {
                final Biome biome = world.getBiome(i, k);
                flag = (BiomeDictionary.isBiomeOfType(biome, Type.FOREST) ||
                        BiomeDictionary.isBiomeOfType(biome, Type.PLAINS))
                        && !BiomeDictionary.isBiomeOfType(biome, Type.SNOWY);
            } else {
                flag = Utils.isIDInList(world.getBiome(i, k).biomeID, GrowthCraftBees.getConfig().beeBiomesList);
            }

            if (flag) {
                new WorldGenBeeHive().generate(world, random, i, j, k);
            }
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

    }
}
