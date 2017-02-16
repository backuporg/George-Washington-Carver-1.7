package growthcraft.rice.common.item;

import growthcraft.core.GrowthCraftCore;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRice extends GrcItemBase {
    public ItemRice() {
        super();
        this.setUnlocalizedName("grc.rice");
        this.setCreativeTab(GrowthCraftCore.creativeTab);
    }

    /************
     * MAIN
     ************/
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, IBlockState state, World world, BlockPos pos, EnumFacing face, float par8, float par9, float par10) {
        if (face == EnumFacing.UP) {
            if (player.canPlayerEdit(pos, face, stack) && player.canPlayerEdit(pos.up(), face, stack)) {
            } else if (player.canPlayerEdit(pos, face, stack) && player.canPlayerEdit(pos, face, stack)) {
                final IBlockState soil = world.getBlockState((BlockPos) state);
                world.setBlockState(pos, state);
                --stack.stackSize;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
