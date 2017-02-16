package growthcraft.core.integration;

import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import squeek.applecore.api.AppleCoreAPI;

import java.util.Random;

public class AppleCore extends ModIntegrationBase {
    public static final String MOD_ID = "AppleCore";
    private static boolean appleCoreLoaded;

    public AppleCore() {
        super(GrowthCraftCore.MOD_ID, MOD_ID);
    }

    // abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
    @Optional.Method(modid = MOD_ID)
    private static Event.Result validateGrowthTick_AC(Block block, World world, BlockPos pos, Random random, IBlockState state) {
        return AppleCoreAPI.dispatcher.validatePlantGrowth(block, world, pos, state, random);
    }

    public static Event.Result validateGrowthTick(Block block, World world, BlockPos pos, Random random, IBlockState state) {
        if (appleCoreLoaded)
            return validateGrowthTick_AC(block, world, pos, random, state);

        return Event.Result.DEFAULT;
    }

    // abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
    @Optional.Method(modid = MOD_ID)
    private static void announceGrowthTick_AC(Block block, World world, BlockPos pos, IBlockState previousMetadata, IBlockState state) {
        AppleCoreAPI.dispatcher.announcePlantGrowth(block, world, pos, state);
    }

    public static void announceGrowthTick(Block block, World world, BlockPos pos, IBlockState previousMetadata, IBlockState state) {
        if (appleCoreLoaded)
            announceGrowthTick_AC(block, world, pos, previousMetadata, state);
    }

    @Override
    public void doInit() {
        appleCoreLoaded = Loader.isModLoaded(modID);
    }
}
