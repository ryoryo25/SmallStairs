package ryoryo.smallstairs.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ModBlocks {
	public static final Block BLOCK_SS_OAK_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_oak");
	public static final Block BLOCK_SS_SPRUCE_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_spruce");
	public static final Block BLOCK_SS_BIRCH_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_birch");
	public static final Block BLOCK_SS_JUNGLE_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_jungle");
	public static final Block BLOCK_SS_ACACIA_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_acacia");
	public static final Block BLOCK_SS_DARK_OAK_PLANKS = new BlockSmallStairs(Blocks.PLANKS.getDefaultState(), "planks_dark_oak");
	public static final Block BLOCK_SS_COBBLESTONE = new BlockSmallStairs(Blocks.COBBLESTONE.getDefaultState(), "cobblestone");
	public static final Block BLOCK_SS_SANDSTONE = new BlockSmallStairs(Blocks.SANDSTONE.getDefaultState(), "sandstone");
	public static final Block BLOCK_SS_RED_SANDSTONE = new BlockSmallStairs(Blocks.RED_SANDSTONE.getDefaultState(), "red_sandstone");
	public static final Block BLOCK_SS_BRICK = new BlockSmallStairs(Blocks.BRICK_BLOCK.getDefaultState(), "brick");
	public static final Block BLOCK_SS_STONE_BRICK = new BlockSmallStairs(Blocks.STONEBRICK.getDefaultState(), "stonebrick");
	public static final Block BLOCK_SS_NETHER_BRICK = new BlockSmallStairs(Blocks.NETHER_BRICK.getDefaultState(), "nether_brick");
	public static final Block BLOCK_SS_RED_NETHER_BRICK = new BlockSmallStairs(Blocks.RED_NETHER_BRICK.getDefaultState(), "red_nether_brick");
	public static final Block BLOCK_SS_STONE = new BlockSmallStairs(Blocks.STONE.getDefaultState(), "stone");
	public static final Block BLOCK_SS_RED_WOOL = new BlockSmallStairs(Blocks.WOOL.getDefaultState(), "wool_red");
	public static final Block BLOCK_SS_YELLOW_WOOL = new BlockSmallStairs(Blocks.WOOL.getDefaultState(), "wool_yellow");
	public static final Block BLOCK_SS_QUARTZ_BLOCK = new BlockSmallStairs(Blocks.QUARTZ_BLOCK.getDefaultState(), "quartz_block");

	public static void init() {
		// NO-OP
		// To create instances of blocks/items in PreInit,
		// Call this in PreInit
	}
}