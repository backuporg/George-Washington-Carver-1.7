package growthcraft.apples.common.village;

import growthcraft.apples.common.world.WorldGenAppleTree;
import growthcraft.core.util.SchemaToVillage;
import growthcraft.core.util.SchemaToVillage.BlockEntry;
import growthcraft.core.util.SchemaToVillage.IBlockEntries;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public abstract class ComponentVillageAppleFarm extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x
	private static final String[][] appleFarmSchema = {
		{
			"x---x x---x",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"|         |",
			"x---------x"
		},
		{
			"fffffgfffff",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"f         f",
			"fffffffffff"
		},
		{
			"t   t t   t",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"t         t"
		},
	};

	// DO NOT REMOVE
	public ComponentVillageAppleFarm() {}

	public ComponentVillageAppleFarm(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
	{
		super(startPiece, par2);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = boundingBox;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageAppleFarm buildComponent(Start startPiece, List list, Random random, BlockPos pos, int coordBaseMode, int par7, EnumFacing face)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 11, 11, 11, face);
		if (canVillageGoDeeper(structureboundingbox))
		{
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null)
			{
				return new ComponentVillageAppleFarm(startPiece, par7, random, structureboundingbox, coordBaseMode);
			}
		}
		return null;
	}

	public void placeBlockAtCurrentPositionPub(World world, Block block, int meta, BlockPos pos, Vec3i vec, StructureBoundingBox box)
	{
		placeBlockAtCurrentPosition(world, block, meta, pos, box);
	}

	protected void placeWorldGenAt(World world, Random random, int tx, int ty, int tz, StructureBoundingBox bb, WorldGenerator generator, BlockPos pos, Vec3i vec)
	{
		final int x = this.getXWithOffset(tx, tz);
		final int y = this.getYWithOffset(ty);
		final int z = this.getZWithOffset(tx, tz);

		if (bb.isVecInside(vec))
		{
			generator.generate(world, random, pos);
		}
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		if (averageGroundLvl < 0)
		{
			this.averageGroundLvl = this.getAverageGroundLevel(world, box);

			if (averageGroundLvl < 0)
			{
				return true;
			}

			boundingBox.offset(0, averageGroundLvl - boundingBox.maxY + 9, 0);
		}

		// clear entire bounding box
		fillWithBlocks(world, box, 0, 0, 0, 11, 4, 11, Blocks.AIR, Blocks.AIR, false);
		// Fill floor with grass blocks
		fillWithBlocks(world, box, 0, 0, 0, 11, 0, 11, Blocks.GRASS, Blocks.GRASS, false);

		final boolean vert = coordBaseMode == 0 || coordBaseMode == 2;
		final HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		map.put('x', new BlockEntry(Blocks.LOG, 0));
		map.put('-', new BlockEntry(Blocks.LOG, vert ? 4 : 8));
		map.put('|', new BlockEntry(Blocks.LOG, vert ? 8 : 4));

		map.put('f', new BlockEntry(Blocks.OAK_FENCE, 0));
		map.put('g', new BlockEntry(Blocks.OAK_FENCE_GATE, this.getMetadataWithOffset(Blocks.OAK_FENCE_GATE, 0)));
		map.put('t', new BlockEntry(Blocks.TORCH, 0));

		SchemaToVillage.drawSchema(this, world, random, box, appleFarmSchema, map, 0, 1, 0);

		final WorldGenAppleTree genAppleTree = new WorldGenAppleTree(true);
		placeWorldGenAt(world, random, 3, 1, 3, box, genAppleTree);
		placeWorldGenAt(world, random, 7, 1, 3, box, genAppleTree);
		placeWorldGenAt(world, random, 3, 1, 7, box, genAppleTree);
		placeWorldGenAt(world, random, 7, 1, 7, box, genAppleTree);

		for (int row = 0; row < 11; ++row)
		{
			for (int col = 0; col < 11; ++col)
			{
				clearCurrentPositionBlocksUpwards(world, col, 7, row, box);
				func_151554_b(world, Blocks.DIRT, 0, col, -1, row, box);
			}
		}
		return true;
	}
}
