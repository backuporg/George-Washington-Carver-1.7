package growthcraft.bees.common.block;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.bees.client.renderer.RenderBeeBox;
import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.integration.minecraft.EnumMinecraftWoodType;
import growthcraft.core.util.ItemUtils;
import net.minecraft.block.SoundType;
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

import java.util.List;
import java.util.Random;


public class BlockBeeBox extends GrcBlockContainer {
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	// bonus
	private int flammability;
	private int fireSpreadSpeed;

	public BlockBeeBox(Material material) {
		super(material);
		setBlockTextureName("grcbees:beebox");
		setTickRandomly(true);
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
		setBlockName("grc.BeeBox.Minecraft");
		setCreativeTab(GrowthCraftBees.tab);
		setTileEntityType(TileEntityBeeBox.class);
	}

	public BlockBeeBox() {
		this(Material.WOOD);
	}

	public String getMetaname(int meta) {
		if (meta >= 0 && meta < EnumMinecraftWoodType.VALUES.length) {
			return EnumMinecraftWoodType.VALUES[meta].name;
		}
		return "" + meta;
	}

	public BlockBeeBox setFlammability(int flam) {
		this.flammability = flam;
		return this;
	}

	public BlockBeeBox setFireSpreadSpeed(int speed) {
		this.fireSpreadSpeed = speed;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return fireSpreadSpeed;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list) {
		for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES) {
			list.add(new ItemStack(block, 1, woodType.meta));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, Random rand, IBlockState state) {
		super.updateTick(world, pos, state, rand);
		final TileEntityBeeBox te = getTileEntity(world, pos);
		if (te != null) te.updateBlockTick();
	}

	@SideOnly(Side.CLIENT)
	public boolean randomDisplayTick(World world, BlockPos pos, Random rand) {
		if (rand.nextInt(24) == 0) {
			final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(pos);
			if (te != null) {
				if (te.hasBees()) {
					if (te.hasBees()) {
						world.playSound(
								(double) pos.getX() + 0.5D,
								(double) pos.getY() + 0.5D,
								(double) pos.getZ() + 0.5D,
								"grcbees:buzz",
								1.0F + rand.nextFloat(),
								0.3F + rand.nextFloat() * 0.7F,
								false);
					}
				}
			}
		}

		/************
		 * TRIGGERS
		 ************/
		@Override
		public boolean onBlockActivated (World world, BlockPos pos, EntityPlayer player, int meta, float par7, float par8, float par9);
		{
			if (super.onBlockActivated(world, pos, player, meta, par7, par8, par9)) return true;
			if (world.isRemote) {
				return true;
			} else {
				final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(pos);
				if (te != null) {
					player.openGui(GrowthCraftBees.instance, 0, world, pos);
					return true;
				}
				return false;
			}
		}

		@Override
		public void breakBlock (World world, BlockPos pos, IBlockState state, int par5)
		{
			final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(pos);

			if (te != null) {
				for (int index = 0; index < te.getSizeInventory(); ++index) {
					final ItemStack stack = te.getStackInSlot(index);

					ItemUtils.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack, rand);
				}

				world.updateComparatorOutputLevel(pos, par5);
			}

			super.breakBlock(world, pos);
		}

		/************
		 * CONDITIONS
		 ************/
		@Override
		public boolean isSideSolid (IBlockAccess world, BlockPos pos, EnumFacing side)
		{
			return EnumFacing.UP == side;
		}

		@Override
		public TileEntity createNewTileEntity (World world, int par2)
		{
			return new TileEntityBeeBox();
		}

		/************
		 * DROPS
		 ************/
		@Override
		public int damageDropped (int damage)
		{
			return damage;
		}

		@Override
		public int quantityDropped (Random random)
		{
			return 1;
		}

		/************
		 * TEXTURES
		 ************/
		@SideOnly(Side.CLIENT)
		protected void registerBeeBoxIcons (IIconRegister reg, String basename, int offset)
		{
			icons[offset * 4] = reg.registerIcon(getTextureName() + basename + "bottom");
			icons[offset * 4 + 1] = reg.registerIcon(getTextureName() + basename + "top");
			icons[offset * 4 + 2] = reg.registerIcon(getTextureName() + basename + "side");
			icons[offset * 4 + 3] = reg.registerIcon(getTextureName() + basename + "side_honey");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void registerBlockIcons (IIconRegister reg)
		{
			this.icons = new IIcon[6 * 4];

			for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES) {
				registerBeeBoxIcons(reg, String.format("/minecraft/%s/", woodType.name), woodType.meta);
			}
		}

		@SideOnly(Side.CLIENT)
		protected int calculateIconOffset ( int meta)
		{
			return MathHelper.clamp_int(meta, 0, icons.length / 4 - 1) * 4;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public IIcon getIcon = (IBlockAccess world, BlockPos pos, int side, IBlockState state)
		{
			final int meta = world.getBlockState(pos);
			final int offset = calculateIconOffset(meta);
			if (side == 0) {
				return icons[offset];
			} else if (side == 1) {
				return icons[offset + 1];
			} else {
				final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(x, y, z);
				if (te != null && te.isHoneyEnough(6)) {
					return icons[offset + 3];
				}
			}
			return icons[offset + 2];
		}

		@Override
		@SideOnly(Side.CLIENT)
		public IIcon getIcon (IBlockState state, int side, int meta)
		{
			final int offset = calculateIconOffset(meta);
			if (side == 0) {
				return icons[offset];
			} else if (side == 1) {
				return icons[offset + 1];
			}
			return icons[offset + 2];
		}

		@SideOnly(Side.CLIENT)
		public IIcon[] getIcons ()
		{
			return icons;
		}

		/************
		 * RENDERS
		 ************/
		@Override
		public int getRenderType (IBlockState state);
		{
			return RenderBeeBox.id;
		}

		@Override
		public boolean isOpaqueCube ()
		{
			return false;
		}

		@Override
		public boolean renderAsNormalBlock ()
		{
			return false;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean shouldSideBeRendered (IBlockAccess world, BlockPos pos, int side, IBlockState state, IBlockAccess source)
		{
			return true;
		}

		/************
		 * BOXES
		 ************/
		@Override
		public void setBlockBoundsForItemRender ()
		{
			final AxisAlignedBB axis = null;
			final IBlockState state = null;
			final List list = null;
			final Entity entity = null;
			final IBlockAccess source = null;
			getBoundingBox(state, source, pos);
		}

		@Override
		@SuppressWarnings({"rawtypes", "unchecked"})
		public void getCollisionBoundingBox (World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List list, Entity entity, IBlockAccess source)
		{
			final AxisAlignedBB axis = null;
			final IBlockState state = null;
			final List list = null;
			final Entity entity = null;
			final IBlockAccess source = null;

			final float f = 0.0625F;
			// LEGS
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox(state, world, pos);
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			// BODY
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			// ROOF
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			getBoundingBox(state, source, pos);
			super.getCollisionBoundingBox (state, world, pos);
			setBlockBoundsForItemRender();
		}

		/************
		 * COMPARATOR
		 ************/
		@Override
		public boolean hasComparatorInputOverride ()
		{
			return true;
		}

		@Override
		public int getComparatorInputOverride (World world, BlockPos pos, int par5)
		{
			final TileEntityBeeBox te = (TileEntityBeeBox) world.getTileEntity(pos);
			return te.countHoney() * 15 / 27;
		}
	}
}
