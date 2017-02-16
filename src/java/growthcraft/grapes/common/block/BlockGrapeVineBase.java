package growthcraft.grapes.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.util.GrapeBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Random;

public abstract class BlockGrapeVineBase extends GrcBlockBase implements IPlantable, ICropDataProvider, IGrowable {
    private ItemStack itemDrop;
    private float growthRateMultiplier;

    public BlockGrapeVineBase() {
        super(Material.PLANTS);
        this.itemDrop = new ItemStack((Item) null, 0);
        this.growthRateMultiplier = 1.0f;
    }

    public ItemStack getItemDrop() {
        return this.itemDrop;
    }

    public void setItemDrop(ItemStack itemstack) {
        this.itemDrop = itemstack;
    }

    @Override
    public Item getItemDropped(int meta, Random par2Random, int par3) {
        return itemDrop.getItem();
    }

    @Override
    public int quantityDropped(Random random) {
        return itemDrop.stackSize;
    }

    public float getGrowthRateMultiplier() {
        return this.growthRateMultiplier;
    }

    public void setGrowthRateMultiplier(float rate) {
        this.growthRateMultiplier = rate;
    }

    public int getGrowthMax() {
        return 1;
    }

    public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta) {
        return (float) meta / (float) getGrowthMax();
    }

    protected boolean isGrapeVine(Block block) {
        return GrapeBlockCheck.isGrapeVine(block);
    }

    public void incrementGrowth(World world, BlockPos pos, IBlockState meta, IBlockState state, Block block) {
        world.setBlockState(pos, state, meta + 1, BlockFlags.SYNC);
        AppleCore.announceGrowthTick(block, world, pos, meta, state);
    }

    protected float getGrowthRate(World world, BlockPos pos) {
        final Block l = world.getBlockState(x, y, z - 1);
        final Block i1 = world.getBlockState(x, y, z + 1);
        final Block j1 = world.getBlockState(x - 1, y, z);
        final Block k1 = world.getBlockState(x + 1, y, z);
        final Block l1 = world.getBlockState(x - 1, y, z - 1);
        final Block i2 = world.getBlockState(x + 1, y, z - 1);
        final Block j2 = world.getBlockState(x + 1, y, z + 1);
        final Block k2 = world.getBlockState(x - 1, y, z + 1);
        final boolean flag = this.isGrapeVine(j1) || this.isGrapeVine(k1);
        final boolean flag1 = this.isGrapeVine(l) || this.isGrapeVine(i1);
        final boolean flag2 = this.isGrapeVine(l1) || this.isGrapeVine(i2) || this.isGrapeVine(j2) || this.isGrapeVine(k2);
        float f = 1.0F;

        for (int l2 = x - 1; l2 <= x + 1; ++l2) {
            for (int i3 = z - 1; i3 <= z + 1; ++i3) {
                final Block block = world.getBlockState(l2, y - 1, i3);
                float f1 = 0.0F;

                if (block != null && block == Blocks.FARMLAND) {
                    f1 = 1.0F;

                    if (block.isFertile(world, l2, y - 1, i3)) {
                        f1 = 3.0F;
                    }
                }

                if (l2 != x || i3 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state, IPlantable plant, Block block) {
        return BlockCheck.canSustainPlant(world, pos, EnumFacing.UP, plant, state, block);
    }

    @Override
    public void onNeighborChange(World world, BlockPos pos, Block block, IBlockState state, IPlantable plant) {
        if (!this.canBlockStay(world, pos, state, plant, block)) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata) {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /************
     * IPLANTABLE
     ************/
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(x, y, z);
    }

    /**
     * If all conditions have passed, do plant growth
     *
     * @param world - world with block
     * @param x     - x coord
     * @param y     - y coord
     * @param z     - z coord
     * @param meta  - block metadata
     */
    protected abstract void doGrowth(World world, BlockPos pos, IBlockState meta);

    /**
     * Are the conditions right for this plant to grow?
     *
     * @param world - world with block
     * @param x     - x coord
     * @param y     - y coord
     * @param z     - z coord
     * @return true, it can grow, false otherwise
     */
    protected abstract boolean canUpdateGrowth(World world, BlockPos pos);

    @Override
    public void updateTick(World world, BlockPos pos, Random random, IBlockState state, Block block) {
        super.updateTick(world, pos, state, random);
        if (canUpdateGrowth(world, pos)) {
            final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(block, world, pos, random, state);
            if (Event.Result.DENY == allowGrowthResult)
                return;

            final IBlockState meta = world.getBlockState((BlockPos) state);
            final float f = this.getGrowthRate(world, pos);

            final boolean continueGrowth = random.nextInt((int) (getGrowthRateMultiplier() / f) + 1) == 0;
            if (Event.Result.ALLOW == allowGrowthResult || continueGrowth) {
                doGrowth(world, pos, meta);
            }
        }
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
