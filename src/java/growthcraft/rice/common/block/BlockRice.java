package growthcraft.rice.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddyCrop;
import growthcraft.core.integration.AppleCore;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.client.renderer.RenderRice;
import growthcraft.rice.util.RiceBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockRice extends GrcBlockBase implements IPaddyCrop, ICropDataProvider, IGrowable {
    public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, RiceStage.MATURE);
    @SideOnly(Side.CLIENT)


    private final float growthRate = GrowthCraftRice.getConfig().riceGrowthRate;

    //@Override
    //public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    //{
    //	this.getBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
    //}

    //@Override
    //public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
    //static {
    //   return null;
    //}

    public BlockRice() {
        super(Material.PLANTS);
        this.setHardness(0.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(null);
        this.setUnlocalizedName("grc.riceBlock");
        //setStepSound(soundTypeGrass);
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

    public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta) {
        return (float) meta / (float) RiceStage.MATURE;
    }

    private void incrementGrowth(World world, BlockPos pos, int meta, IBlockState state) {
        world.setBlockState(pos, state, BlockFlags.SYNC);
        AppleCore.announceGrowthTick(this, world, pos, state, state);
    }

    private void growRice(World world, BlockPos pos, IBlockState state, int meta, Block block) {
        incrementGrowth(world, pos, meta, state);
        final IBlockState paddyBlock = world.getBlockState(pos.down());
        if (RiceBlockCheck.isPaddy((Block) paddyBlock)) {
            ((BlockPaddy) paddyBlock.getBlock()).drainPaddy(world, pos.down());
        }
    }

    /************
     * TICK
     ************/
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(world, pos, state, random);
        if (checkCropChange(world, pos)) {
            return;
        }
        final IBlockState paddyState = world.getBlockState(pos.down());
        final boolean paddyHasWater = false;

        if (getLightValue((IBlockState) world) >= 9 && paddyHasWater) {
            final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random, state);
            if (allowGrowthResult == Event.Result.DENY)
                return;

            if ((int) state.getValue(GROWTH) < RiceStage.MATURE) {
                final float f = getGrowthRate(world, pos, state);

                if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int) (growthRate / f) + 1) == 0)) {
                    grow(world, random, pos, state);
                }
            }
        }
    }

    private float getGrowthRate(World world, BlockPos pos, IBlockState state) {
        float f = 1.0F;
        final IBlockState l = world.getBlockState(pos.north());
        final IBlockState i1 = world.getBlockState(pos.south());
        final IBlockState j1 = world.getBlockState(pos.west());
        final IBlockState k1 = world.getBlockState(pos.east());
        final IBlockState l1 = world.getBlockState(pos.north().west());
        final IBlockState i2 = world.getBlockState(pos.south().west());
        final IBlockState j2 = world.getBlockState(pos.south().east());
        final IBlockState k2 = world.getBlockState(pos.north().east());
        final boolean flag = j1.getBlock() == this || k1.getBlock() == this;
        final boolean flag1 = l.getBlock() == this || i1.getBlock() == this;
        final boolean flag2 = l1.getBlock() == this || i2.getBlock() == this || j2.getBlock() == this || k2.getBlock() == this;

        for (int loop_i = pos.getX() - 1; loop_i <= pos.getX() + 1; ++loop_i) {
            for (int loop_k = pos.getZ() - 1; loop_k <= pos.getZ() + 1; ++loop_k) {
                final BlockPos soilPos = new BlockPos(loop_i, pos.getY(), loop_k);
                final IBlockState soil = world.getBlockState(soilPos);
                float f1 = 0.0F;

                if (RiceBlockCheck.isPaddy((Block) soil)) {
                    f1 = 1.0F;

                    if ((int) state.getValue(GROWTH) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (loop_i != pos.getX() || loop_k != pos.getZ()) {
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

    private boolean checkCropChange(World world, BlockPos pos) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, world.getBlockState(pos), 0);
            world.setBlockToAir(pos);
        }
        return false;
    }

    /************
     * TRIGGERS
     ************/
    @Override
    public void onNeighborChange(World world, BlockPos pos, Block par5) {
        this.checkCropChange(world, pos);
    }

    /**
     * @param block - block to place on
     * @return can the rice be placed on this block?
     */
    private boolean canThisPlantGrowOnThisBlockID(Block block) {
        return RiceBlockCheck.isPaddy(block);
    }

    @Override
    private boolean canBlockStay(World world, BlockPos pos) {
        return (world.getLight(pos) >= 8 ||
                world.canSeeSky(pos)) &&
                this.canThisPlantGrowOnThisBlockID((Block) world.getBlockState(pos));
    }

    /************
     * STUFF
     ************/
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, BlockPos pos) {
        return GrowthCraftRice.items.rice.getItem();
    }

    /************
     * DROPS
     ************/
    @Override
    public Item getItemDropped(int meta, Random random, int par3) {
        return GrowthCraftRice.items.rice.getItem();
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, int par5, float par6, int par7) {
        super.dropBlockAsItemWithChance(world, pos, state, par6, 0);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, BlockPos pos, IBlockState state, int metadata, int fortune) {
        final List<ItemStack> ret = super.getDrops(world, pos, state, fortune);

        if (metadata >= 7) {
            for (int n = 0; n < 3 + fortune; n++) {
                if (world.rand.nextInt(15) <= metadata) {
                    ret.add(GrowthCraftRice.items.rice.asStack(1));
                }
            }
        }

        return (ArrayList<ItemStack>) ret;
    }

    //@Override
    //@SideOnly(Side.CLIENT)

    //{
    //	icons = new IIcon[5];
//
    //	for (int i = 0; i < icons.length; ++i)
    //	{
    //		icons[i] = reg.registerIcon("grcrice:rice_" + i);
    //	}
    //}

    //@Override
    //@SideOnly(Side.CLIENT)
//
    //{
    //	if (meta < 0 || meta > 7)
    //	{
    //		meta = 7;
    //	}
//
    //	int i = 0;
    //	switch (meta)
    //	{
    //		case 0: case 1: i = 0; break;
    //		case 2: case 3: i = 1; break;
    //		case 4: case 5: i = 2; break;
    //		case 6: case 7: i = 3; break;
    //		default: i = 2;
    //	}
///
    //	return icons[i];
    //}

    /************
     * RENDERS
     ************/
    @Override
    public int getRenderType() {
        return RenderRice.id;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public static class RiceStage {
        public static final int MATURE = 7;

        private RiceStage() {
        }
    }

    //@Override
    //@SideOnly(Side.CLIENT)
    //public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, BlockPos pos)
    //{
    //	this.setBlockBoundsBasedOnState(world, pos);
    //	return super.getSelectedBoundingBoxFromPool(world, pos);
    //}
}
