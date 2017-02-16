package growthcraft.bees.common.village;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.util.SchemaToVillage;
import growthcraft.core.util.SchemaToVillage.BlockEntry;
import growthcraft.core.util.SchemaToVillage.IBlockEntries;
import growthcraft.core.util.SchemaToVillage.MultiBlockEntries;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ComponentVillageApiarist extends StructureVillagePieces.Village implements SchemaToVillage.IVillage {
    // Design by Ar97x, modified by IceDragon (I made the tree levaves a 3x3x3 cube, makes it look neat)
    private static final String[][] apiaristExteriorSchema = {
            {
                    "    486  ",
                    "xcccccccx",
                    "cpppppppc",
                    "cpppppppc",
                    "cpppppppc",
                    "cpppppppc",
                    "xcccccccx",
                    "ddddddd  ",
                    "ddddddd  ",
                    "ddddddd  ",
                    "ddddddd  ",
                    "ddddddd  ",
                    "         ",
                    "         "
            },
            {
                    "         ",
                    "xpppp ppx",
                    "p       p",
                    "B       B",
                    "B       B",
                    "p    8  p",
                    "xpp ppppx",
                    "f,,,,,f  ",
                    "f,,,,,f  ",
                    "f,+,+,f  ",
                    "f,,,,,f  ",
                    "ffff ff  ",
                    "         ",
                    "         "
            },
            {
                    "         ",
                    "xpggp ppx",
                    "p       p",
                    "Y       Y",
                    "Y       Y",
                    "p       p",
                    "xpp pgppx",
                    "         ",
                    "         ",
                    "t     t  ",
                    "    H    ",
                    "   t     ",
                    "         ",
                    "         "
            },
            {
                    // the torches for this level are placed manually
                    "888888888",
                    "xpppppppx",
                    "p       p",
                    "B       B",
                    "B       B",
                    "p       p",
                    "xpppppppx",
                    "222222222",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         "
            },
            {
                    "         ",
                    "888888888",
                    "ppppppppp",
                    "Yp     pY",
                    "Yp     pY",
                    "ppppppppp",
                    "222222222",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         "
            },
            {
                    "         ",
                    "         ",
                    "788888889",
                    "4BYBYBYB6",
                    "4BYBYBYB6",
                    "122222223",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         "
            }
    };

    private static final String[][] apiaristInteriorSchema = {
            {
                    " 6    4",
                    " 6    K",
                    " 6    K",
                    "p6    4"
            },
            {
                    "      l",
                    "      K",
                    "      K",
                    "      l"
            },
            {
                    " 8    l",
                    " _    K",
                    " _    K",
                    " 2    l"
            },
            {
                    "       ",
                    " 2   4 ",
                    " 8   4 ",
                    "       "
            }
    };

    // That tree that appears behind the apiary, its a birch tree
    // Since `x` is already used for Oak in the original schema, I've extracted
    // here.
    private static final String[][] apiaristBackyardTreeSchema = {
            {
                    "   ",
                    " x ",
                    "   ",
            },
            {
                    // bee hive appears on this level
                    "   ",
                    " x ",
                    "   ",
            },
            {
                    "lll",
                    "lxl",
                    "lll",
            },
            {
                    "lll",
                    "lxl",
                    "lll",
            },
            {
                    " l ",
                    "lxl",
                    " l ",
            },
            {
                    "   ",
                    " l ",
                    "   ",
            }
    };

    private static final WeightedRandomChestContent[] apiaristChestContents = new WeightedRandomChestContent[]{
            new WeightedRandomChestContent(GrowthCraftBees.items.bee.asStack(), 1, 2, 3),
            new WeightedRandomChestContent(GrowthCraftBees.items.honeyCombEmpty.asStack(), 1, 3, 5),
            new WeightedRandomChestContent(GrowthCraftBees.items.honeyCombFilled.asStack(), 1, 3, 5),
            new WeightedRandomChestContent(GrowthCraftBees.items.honeyJar.asStack(), 1, 1, 10),
            new WeightedRandomChestContent(GrowthCraftBees.blocks.beeBox.asStack(), 1, 2, 5),
            new WeightedRandomChestContent(GrowthCraftBees.items.beesWax.asStack(), 1, 10, 5)
    };

    // DO NOT REMOVE
    public ComponentVillageApiarist() {
    }

    public ComponentVillageApiarist(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int coordBaseMode) {
        super(startPiece, par2);
        this.coordBaseMode = coordBaseMode;
        this.boundingBox = boundingBox;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ComponentVillageApiarist buildComponent(Start startPiece, List list, Random random, BlockPos pos, int coordBaseMode, int par7) {
        final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 8, 14, coordBaseMode);
        if (canVillageGoDeeper(structureboundingbox)) {
            if (StructureComponent.findIntersecting(list, structureboundingbox) == null) {
                return new ComponentVillageApiarist(startPiece, par7, random, structureboundingbox, coordBaseMode);
            }
        }
        return null;
    }

    public void placeBlockAtCurrentPositionPub(World world, Block block, int meta, BlockPos pos, StructureBoundingBox box) {
        placeBlockAtCurrentPosition(world, block, meta, pos, box);
    }

    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, box);

            if (this.averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7, 0);
        }

        // clear entire bounding box
        this.fillWithBlocks(world, box, 0, 0, 0, 9, 8, 14, Blocks.AIR, Blocks.AIR, false);

        final HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();

        // Plop down the tree first
        map.put('x', new BlockEntry(Blocks.LOG, 2));
        map.put('l', new BlockEntry(Blocks.LEAVES, 2));

        SchemaToVillage.drawSchema(this, world, random, box, apiaristBackyardTreeSchema, map, 3, 1, 10);

        map.clear();

        map.put('c', new BlockEntry(Blocks.COBBLESTONE));
        map.put('d', new BlockEntry(Blocks.GRASS));
        map.put('p', new BlockEntry(Blocks.PLANKS));
        map.put('x', new BlockEntry(Blocks.LOG));
        map.put('l', new BlockEntry(Blocks.LEAVES));
        map.put('g', new BlockEntry(Blocks.GLASS_PANE));

        map.put('f', new BlockEntry(Blocks.OAK_FENCE));
        // high slab
        map.put('-', new BlockEntry(Blocks.WOODEN_SLAB, 8));
        // low slab
        map.put('_', new BlockEntry(Blocks.WOODEN_SLAB, 0));
        map.put(',', new MultiBlockEntries(new BlockEntry[]{
                new BlockEntry(Blocks.RED_FLOWER, 4),
                new BlockEntry(Blocks.RED_FLOWER, 5),
                new BlockEntry(Blocks.RED_FLOWER, 6),
                new BlockEntry(Blocks.RED_FLOWER, 7)
        }));

        map.put('1', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2)));
        map.put('2', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2)));
        map.put('3', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2)));

        map.put('4', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 0)));
        map.put('6', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 1)));

        map.put('7', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3)));
        map.put('8', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3)));
        map.put('9', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3)));

        map.put('t', new BlockEntry(Blocks.TORCH));

        map.put('Y', new BlockEntry(Blocks.PLANKS, 2));
        map.put('B', new BlockEntry(Blocks.PLANKS, 1));

        map.put('H', new BlockEntry(GrowthCraftBees.blocks.beeHive.getBlockState(), this.getMetadataWithOffset(GrowthCraftBees.blocks.beeHive.getBlockState(), 3)));
        map.put('+', new BlockEntry(GrowthCraftBees.blocks.beeBox.getBlockState(), this.getMetadataWithOffset(GrowthCraftBees.blocks.beeBox.getBlockState(), random.nextInt(6))));

        SchemaToVillage.drawSchema(this, world, random, box, apiaristExteriorSchema, map, 0, 0, 0);

        // Get ready to recycle for interior design
        map.clear();

        map.put('p', new BlockEntry(Blocks.PLANKS));

        // inverted stairs for decorating interior
        map.put('1', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2) | 4));
        map.put('2', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2) | 4));
        map.put('3', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 2) | 4));

        map.put('4', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 0) | 4));
        map.put('6', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 1) | 4));

        map.put('7', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3) | 4));
        map.put('8', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3) | 4));
        map.put('9', new BlockEntry(Blocks.OAK_STAIRS, this.getMetadataWithOffset(Blocks.OAK_STAIRS, 3) | 4));

        map.put('_', new BlockEntry(Blocks.WOODEN_SLAB, 0));

        map.put('K', new BlockEntry(Blocks.BOOKSHELF, 0));

        // metadata here is (1(spruce leaves) | 4(no decay))
        map.put('l', new BlockEntry(Blocks.LEAVES, 1 | 4));

        SchemaToVillage.drawSchema(this, world, random, box, apiaristInteriorSchema, map, 1, 1, 2);

        // Place torches
        this.placeBlockAtCurrentPosition(world, Blocks.TORCH, 0, 5, 3, 2, box);
        this.placeBlockAtCurrentPosition(world, Blocks.TORCH, 0, 4, 3, 5, box);

		/*
            TODO:
				place signs beside stairs to form a chair -
				If you happen to be a sign rotation expert, PLEASE DO FIX THIS.
		 */
        //this.placeBlockAtCurrentPosition(world, Blocks.WALL_SIGN, 2, 4, 1, 5, box);
        //this.placeBlockAtCurrentPosition(world, Blocks.WALL_SIGN, 2, 6, 1, 5, box);

        // Drop in the front door
        this.placeDoorAtCurrentPosition(world, box, random, 5, 1, 1, this.getMetadataWithOffset(Blocks.OAK_DOOR, 1));
        // Drop in the back door
        this.placeDoorAtCurrentPosition(world, box, random, 3, 1, 6, this.getMetadataWithOffset(Blocks.OAK_DOOR, 1));

        // Slap that nicely placed flower pot on the counter
        this.placeBlockAtCurrentPosition(world, Blocks.FLOWER_POT, 3, 2, 2, 2, box);

        // Shove a chest behind the counter, filled with goodies
        this.generateStructureChestContents(world, box, random, 1, 2, 5, apiaristChestContents, 3 + random.nextInt(6));

        // Fix some structural madness, like buildings spawning in the air and
        // floating on a thin layer of dirt/cobblestone
        for (int row = 0; row < 14; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.clearCurrentPositionBlocksUpwards(world, col, 8, row, box);
                this.replaceAirAndLiquidDownwards(world, Blocks.COBBLESTONE, 0, col, -1, row, box);
            }
        }

        // Trap the villager behind the counter, so he shall forever sells us
        // apiary items... Poor guy.
        this.spawnVillagers(world, box, 1, 2, 3, 1);
        return true;
    }

    @Override
    protected int getVillagerType(int par1) {
        return GrowthCraftBees.getConfig().villagerApiaristID;
    }
}
