package growthcraft.grapes.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.common.block.BlockGrapeVine0;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemGrapeSeeds extends GrcItemBase implements IPlantable {
    public ItemGrapeSeeds() {
        super();
        this.setUnlocalizedName("grc.grapeSeeds");
        this.setCreativeTab(GrowthCraftGrapes.creativeTab);
    }

    /************
     * MAIN
     ************/
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state, int flags, int EnumFacing, float par8, float par9, float par10, EnumFacing face) {
        if (EnumFacing != 1) {
            return false;
        } else {
            if (player.canPlayerEdit(pos, face, stack) && player.canPlayerEdit(pos, face, stack)) {
                final BlockGrapeVine0 block = GrowthCraftGrapes.blocks.grapeVine0.getBlockState();
                if (BlockCheck.canSustainPlant(world, pos, face, block)) {
                    if (world.isAirBlock(pos)) {
                        world.setBlockState(pos, state, flags);
                        --stack.stackSize;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /************
     * TEXTURES
     ************/
    //@Override
    //@SideOnly(Side.CLIENT)
    //public void registerIcons(IIconRegister reg)
    //{
    //	this.itemIcon = reg.registerIcon("grcgrapes:grape_seeds");
    //}
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return (IBlockState) GrowthCraftGrapes.blocks.grapeVine0.getBlockState();
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, BlockPos pos) {
        return 0;
    }
}
