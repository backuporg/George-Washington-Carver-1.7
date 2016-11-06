package growthcraft.bees.common.block;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.client.renderer.RenderBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.integration.minecraft.EnumMinecraftWoodType;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.List;
import java.util.Random;


public class BlockBeeBox extends GrcBlockContainer
{
	@SideOnly(Side.CLIENT)

	// bonus
	private int flammability;
	private int fireSpreadSpeed;

	public BlockBeeBox(Material material)
	{
		super(material);
		setBlockTextureName("grcbees:beebox");
		setTickRandomly(true);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.BeeBox.Minecraft");
		setCreativeTab(GrowthCraftBees.tab);
		setTileEntityType(TileEntityBeeBox.class);
	}

	public BlockBeeBox()
	{
		this(Material.WOOD);
	}

	public String getMetaname(int meta)
	{
		if (meta >= 0 && meta < EnumMinecraftWoodType.VALUES.length)
		{
			return EnumMinecraftWoodType.VALUES[meta].name;
		}
		return "" + meta;
	}

	public BlockBeeBox setFlammability(int flam)
	{
		this.flammability = flam;
		return this;
	}

	public BlockBeeBox setFireSpreadSpeed(int speed)
	{
		this.fireSpreadSpeed = speed;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, EnumFacing face)
	{
		return flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, EnumFacing face)
	{
		return fireSpreadSpeed;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES)
		{
			list.add(new ItemStack(block, 1, woodType.meta));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, Random rand, IBlockState state)
	{
		super.updateTick(world, pos, state, rand);
		final TileEntityBeeBox te = getTileEntity(world, pos);
		if (te != null) te.updateBlockTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random)
	{
		if (random.nextInt(24) == 0)
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				if (te.hasBees())
				{
					world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F),
						"grcbees:buzz", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, pos, player, meta, par7, par8, par9)) return true;
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				player.openGui(GrowthCraftBees.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, EnumFacing side)
	{
		return EnumFacing.UP == side;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityBeeBox();
	}

	@Override
	public int damageDropped(int damage)
	{
		return damage;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	//@SideOnly(Side.CLIENT)
	//protected void registerBeeBoxIcons(IIconRegister reg, String basename, int offset)
	//{
	//	icons[offset * 4] = reg.registerIcon(getTextureName() + basename + "bottom");
	//	icons[offset * 4 + 1] = reg.registerIcon(getTextureName() + basename + "top");
	//	icons[offset * 4 + 2] = reg.registerIcon(getTextureName() + basename + "side");
	//	icons[offset * 4 + 3] = reg.registerIcon(getTextureName() + basename + "side_honey");
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//public void registerBlockIcons(IIconRegister reg)
	//{
	//	this.icons = new IIcon[6 * 4];
//
	//	for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES)
	//	{
	//		registerBeeBoxIcons(reg, String.format("/minecraft/%s/", woodType.name), woodType.meta);
	//	}
	//}

	@SideOnly(Side.CLIENT)
	protected int calculateIconOffset(int meta)
	{
		return MathHelper.clamp_int(meta, 0, icons.length / 4 - 1) * 4;
	}

	//@Override
	//@SideOnly(Side.CLIENT)
	////public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	//{
	//	final int meta = world.getBlockMetadata(x, y, z);
	//	final int offset = calculateIconOffset(meta);
	//	if (side == 0)
	//	{
	//		return icons[offset];
	//	}
	//	else if (side == 1)
	//	{
	//		return icons[offset + 1];
	//	}
	//	else
	///	{
	//		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(x, y, z);
	//		if (te != null && te.isHoneyEnough(6))
	//		{
	//			return icons[offset + 3];
	//		}
	//	}
	//	return icons[offset + 2];
	//}

	//@Override
	//@SideOnly(Side.CLIENT)
	//
	//{
	//	final int offset = calculateIconOffset(meta);
	//	if (side == 0)
	//	{
	//		return icons[offset];
	//	}
	//	else if (side == 1)
	//	{
	//		return icons[offset + 1];
	//	}
	//	return icons[offset + 2];
	//}

	//@SideOnly(Side.CLIENT)
	////public IIcon[] getIcons()
	//{
	//	return icons;
	//}

	@Override
	public int getRenderType()
	{
		return RenderBeeBox.id;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	//@Override
	//public void setBlockBoundsForItemRender()
	//{
	//	setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	//}

//	@Override
//	@SuppressWarnings({"rawtypes", "unchecked"})
//	public void getCollisionBoundingBox (World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List list, Entity entity)
//	{
//		final AxisAlignedBB axis = null;
//		final BlockPos pos = null;
//		final IBlockState state = null;
//		final List list = null;
//		final Entity entity = null;
//		final World world = null;
//
//		final float f = 0.0625F;
//		// LEGS
//		getBoundingBox(3 * f, 0.0F, 3 * f, 5 * f, 3 * f, 5 * f);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		getBoundingBox(11 * f, 0.0F, 3 * f, 13 * f, 3 * f, 5 * f);
//		super.getCollisionBoundingBox(world, pos, state, axis, list, entity);
//		getBoundingBox(3 * f, 0.0F, 11 * f, 5 * f, 3 * f, 13 * f);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		getBoundingBox(11 * f, 0.0F, 11 * f, 13 * f, 3 * f, 13 * f);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		// BODY
//		getBoundingBox(1 * f, 3 * f, 1 * f, 15 * f, 10 * f, 15 * f);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		// ROOF
//		getBoundingBox(0.0F, 10 * f, 0.0F, 1.0F, 13 * f, 1.0F);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		getBoundingBox(2 * f, 13 * f, 2 * f, 14 * f, 1.0F, 14 * f);
//		super.getCollisionBoundingBox (world, pos, state, axis, list, entity);
//		setBlockBoundsForItemRender();
//	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos, int par5)
	{
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
		if (te != null)
		{
			return te.countHoney() * 15 / te.getHoneyCombMax();
		}
		return 0;
	}

	@Override
	public boolean wrenchBlock(World world, BlockPos pos, EntityPlayer player, ItemStack wrench) {
		return false;
	}
}