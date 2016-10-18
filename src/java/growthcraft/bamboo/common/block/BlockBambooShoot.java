package growthcraft.bamboo.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.RenderType;
import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.common.world.WorldGenBamboo;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.util.BlockCheck;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBambooShoot extends BlockBush implements ICropDataProvider, IGrowable
{
	//constants
	private final int growth = GrowthCraftBamboo.getConfig().bambooShootGrowthRate;

	public BlockBambooShoot()
	{
		super(Material.PLANTS);
		setSoundType(SoundType.PLANT);
		setHardness(0.0F);
		setTickRandomly(true);
		setBlockTextureName("grcbamboo:shoot");
		final float f = 0.4F;
		getBoundingBox(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setUnlocalizedName("grc.bambooShoot");
		setCreativeTab(null);
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)(meta / 1.0);
	}

	/************
	 * TICK
	 ************/
	@Override
	public void updateTick(World world, BlockPos pos, Random rand)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, x, y, z, rand);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(this.growth) == 0)
			{
				this.markOrGrowMarked(world, x, y, z, rand);
			}
		}
	}

	/************
	 * EVENTS
	 ************/
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		super.onNeighborBlockChange(world, x, y, z, par5);
		checkShootChange(world, x, y, z);
	}

	protected final void checkShootChange(World world, BlockPos pos)
	{
		if (!canBlockStay(world, x, y, z))
		{
			dropBlockAsItem(world, x, y, z, world.getBlockState(x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}

	/************
	 * CONDITIONS
	 ************/

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z)) &&
			BlockCheck.canSustainPlant(world, x, y - 1, z, EnumFacing.UP, this);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftBamboo.items.bambooShootFood.getItem();
	}

	public void growBamboo(World world, BlockPos pos, Random rand)
	{
		if (!TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;

		final int meta = world.getBlockState(x, y, z) & 3;
		final WorldGenerator generator = new WorldGenBamboo(true);

		world.setBlockToAir(x, y, z);

		if (!generator.generate(world, rand, x, y, z))
		{
			world.setBlockState(x, y, z, this, meta, BlockFlags.ALL);
		}
	}

	public void markOrGrowMarked(World world, BlockPos pos, Random random)
	{
		final int meta = world.getBlockState(x, y, z);

		if ((meta & 8) == 0)
		{
			world.setBlockState(x, y, z, meta | 8, BlockFlags.SUPRESS_RENDER);
		}
		else
		{
			this.growBamboo(world, x, y, z, random);
		}
	}

	/* Both side */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		return true;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos)
	{
		if (random.nextFloat() < 0.45D)
		{
			markOrGrowMarked(world, x, y, z, random);
		}
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
	{
		return false;
	}

	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return GrowthCraftBamboo.items.bambooShootFood.getItem();
	}

	@Override
	public int getRenderType()
	{
		return RenderType.BUSH;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
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
