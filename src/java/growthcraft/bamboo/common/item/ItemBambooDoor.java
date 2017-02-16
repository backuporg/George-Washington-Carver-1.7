package growthcraft.bamboo.common.item;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.item.GrcItemBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBambooDoor extends GrcItemBase {
    public ItemBambooDoor() {
        super();
        this.maxStackSize = 1;
        setUnlocalizedName("grc.bambooDoor");
        setCreativeTab(GrowthCraftBamboo.creativeTab);
    }

    public static void placeDoorBlock(World world, int i, int j, int k, int side, Block block) {
        byte b0 = 0;
        byte b1 = 0;

        if (side == 0) {
            b1 = 1;
        }

        if (side == 1) {
            b0 = -1;
        }

        if (side == 2) {
            b1 = -1;
        }

        if (side == 3) {
            b0 = 1;
        }

        final int i1 = (world.getBlockState(i - b0, j, k - b1).isNormalCube(world, i - b0, j, k - b1) ? 1 : 0) + (world.getBlockState(i - b0, j + 1, k - b1).isNormalCube(world, i - b0, j + 1, k - b1) ? 1 : 0);
        final int j1 = (world.getBlockState(i + b0, j, k + b1).isNormalCube(world, i + b0, j, k + b1) ? 1 : 0) + (world.getBlockState(i + b0, j + 1, k + b1).isNormalCube(world, i + b0, j + 1, k + b1) ? 1 : 0);
        final boolean flag = world.getBlockState(i - b0, j, k - b1) == block || world.getBlockState(i - b0, j + 1, k - b1) == block;
        final boolean flag1 = world.getBlockState(i + b0, j, k + b1) == block || world.getBlockState(i + b0, j + 1, k + b1) == block;
        boolean flag2 = false;

        if (flag && !flag1) {
            flag2 = true;
        } else if (j1 > i1) {
            flag2 = true;
        }

        world.setBlockState(i, j, k, block, side, 2);
        world.setBlockState(i, j + 1, k, block, 8 | (flag2 ? 1 : 0), 2);
        world.notifyBlocksOfNeighborChange(i, j, k, block);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, block);
    }

    /************
     * MAIN
     ************/
    public boolean onItemUse(ItemStack stack, EnumFacing face, EntityPlayer player, World world, BlockPos pos, int side, float par8, float par9, float par10) {
        if (side != 1) {
            return false;
        } else {
            ++y;
            final Block block = GrowthCraftBamboo.blocks.bambooDoor.getBlockState();

            if (player.canPlayerEdit(pos, face, stack) && player.canPlayerEdit(x, y + 1, z, side, stack)) {
                if (!block.canPlaceBlockAt(world, pos)) {
                    return false;
                } else {
                    final int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                    placeDoorBlock(world, pos, i1, block);
                    --stack.stackSize;
                    return true;
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
    //	this.itemIcon = reg.registerIcon("grcbamboo:door");
    //}
}
