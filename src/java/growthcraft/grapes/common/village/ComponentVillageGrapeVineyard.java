package growthcraft.grapes.common.village;

import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.GrowthCraftGrapes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import java.util.List;
import java.util.Random;

public class ComponentVillageGrapeVineyard extends StructureVillagePieces.Village
{
	public ComponentVillageGrapeVineyard(){}

	public ComponentVillageGrapeVineyard(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int par5)
	{
		super(startPiece, par2);
		this.coordBaseMode = par5;
		this.boundingBox = boundingBox;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageGrapeVineyard buildComponent(Start startPiece, List list, Random random, int par3, int par4, int par5, int par6, int par7)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 6, 9, par6);
		if (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null)
		{
			return new ComponentVillageGrapeVineyard(startPiece, par7, random, structureboundingbox, par6);
		}
		return null;
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		if (this.averageGroundLvl < 0)
		{
			this.averageGroundLvl = this.getAverageGroundLevel(world, box);

			if (this.averageGroundLvl < 0)
			{
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
		}

		this.fillWithBlocks(world, box, 0, 1, 0, 12, 6, 8, Blocks.AIR, Blocks.AIR, false);
		this.fillWithBlocks(world, box, 0, 0, 0, 0, 0, 8, Blocks.LOG, Blocks.LOG, false);
		this.fillWithBlocks(world, box, 12, 0, 0, 12, 0, 8, Blocks.LOG, Blocks.LOG, false);
		this.fillWithBlocks(world, box, 1, 0, 0, 11, 0, 0, Blocks.LOG, Blocks.LOG, false);
		this.fillWithBlocks(world, box, 1, 0, 8, 11, 0, 8, Blocks.LOG, Blocks.LOG, false);
		this.fillWithBlocks(world, box, 1, 0, 1, 11, 0, 7, Blocks.GRASS, Blocks.GRASS, false);
		int loop;
		int loop2;

		for (loop = 1; loop < 12; loop = loop + 2)
		{
			this.fillWithBlocks(world, box, loop, 0, 2, loop, 0, 6, Blocks.WATER, Blocks.WATER, false);
			this.fillWithBlocks(world, box, loop, 0, 4, loop, 0, 4, Blocks.FARMLAND, Blocks.FARMLAND, false);
			this.placeBlockAtCurrentPosition(world, Blocks.OAK_FENCE, 0, loop, 1, 1, box);
			this.placeBlockAtCurrentPosition(world, Blocks.OAK_FENCE, 0, loop, 1, 7, box);
			this.placeBlockAtCurrentPosition(world, Blocks.OAK_FENCE, 0, loop, 2, 1, box);
			this.placeBlockAtCurrentPosition(world, Blocks.OAK_FENCE, 0, loop, 2, 7, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftCore.blocks.fenceRope.getBlockState(), 0, loop, 3, 1, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftCore.blocks.fenceRope.getBlockState(), 0, loop, 3, 7, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.blocks.grapeVine1.getBlockState(), 1, loop, 1, 4, box);
			this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.blocks.grapeVine1.getBlockState(), 1, loop, 2, 4, box);
			for (loop2 = 2; loop2 <= 6; ++loop2)
			{
				this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.blocks.grapeLeaves.getBlockState(), 0, loop, 3, loop2, box);
				if (MathHelper.getRandomIntegerInRange(random, 0, 2) != 0 && loop2 != 4)
				{
					this.placeBlockAtCurrentPosition(world, GrowthCraftGrapes.blocks.grapeBlock.getBlockState(), 0, loop, 2, loop2, box);
				}
			}
		}

		for (loop = 0; loop < 9; ++loop)
		{
			for (loop2 = 0; loop2 < 13; ++loop2)
			{
				this.clearCurrentPositionBlocksUpwards(world, loop2, 6, loop, box);
				this.replaceAirAndLiquidDownwards(world, Blocks.DIRT, 0, loop2, -1, loop, box);
			}
		}

		return true;
	}
}
