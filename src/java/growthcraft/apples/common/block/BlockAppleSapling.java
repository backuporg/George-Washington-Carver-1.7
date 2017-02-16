package growthcraft.apples.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.apples.common.world.WorldGenAppleTree;
import growthcraft.core.GrowthCraftCore;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public class BlockAppleSapling extends BlockBush implements IGrowable {
    private final int growth = GrowthCraftApples.getConfig().appleSaplingGrowthRate;

    public BlockAppleSapling() {
        super(Material.PLANTS);
        setHardness(0.0F);
        //setStepSound(soundTypeGrass);
        setUnlocalizedName("grc.appleSapling");
        setTickRandomly(true);
        setCreativeTab(GrowthCraftCore.creativeTab);
        //setBlockTextureName("grcapples:apple_sapling");
        final float f = 0.4F;
        getBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }

    /************
     * MAIN
     ************/
    public void updateTick(World world, BlockPos pos, Random random, IBlockState state) {
        if (!world.isRemote) {
            super.updateTick(world, pos, state, random);

            if (world.getLight(x, y + 1, z) >= 9 && random.nextInt(this.growth) == 0) {
                this.markOrGrowMarked(world, pos, random);
            }
        }
    }

    public void markOrGrowMarked(World world, BlockPos pos, Random random) {
        final int meta = world.getBlockState(pos);

        if ((meta & 8) == 0) {
            world.setBlockState(pos, meta | 8, BlockFlags.SUPRESS_RENDER);
        } else {
            this.growTree(world, pos, random);
        }
    }

    public void growTree(World world, BlockPos pos, Random random) {
        if (!TerrainGen.saplingGrowTree(world, random, pos)) return;

        final int meta = world.getBlockState(pos) & 3;
        final WorldGenerator generator = new WorldGenAppleTree(true);

        world.setBlockToAir(pos);

        if (!generator.generate(world, random, pos)) {
            world.setBlockState(pos, this, meta, BlockFlags.ALL);
        }
    }

    /* Both side */
    @Override
    public boolean func_149851_a(World world, BlockPos pos, boolean isClient) {
        return (world.getBlockState(pos) & 8) == 0;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

    }
}
