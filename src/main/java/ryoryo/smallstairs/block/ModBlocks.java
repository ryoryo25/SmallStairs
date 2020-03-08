package ryoryo.smallstairs.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ryoryo.polishedlib.util.Utils;
import ryoryo.polishedlib.util.interfaces.IModId;

public class ModBlocks
{
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


	public static void init()
	{
		registerSmallStairs(BLOCK_SS_COBBLESTONE, "cobblestone");
		registerSmallStairs(BLOCK_SS_OAK_PLANKS, "planks_oak");
		registerSmallStairs(BLOCK_SS_SPRUCE_PLANKS, "planks_spruce");
		registerSmallStairs(BLOCK_SS_BIRCH_PLANKS, "planks_birch");
		registerSmallStairs(BLOCK_SS_JUNGLE_PLANKS, "planks_jungle");
		registerSmallStairs(BLOCK_SS_ACACIA_PLANKS, "planks_acacia");
		registerSmallStairs(BLOCK_SS_DARK_OAK_PLANKS, "planks_dark_oak");
		registerSmallStairs(BLOCK_SS_SANDSTONE, "sandstone");
		registerSmallStairs(BLOCK_SS_RED_SANDSTONE, "red_sandstone");
		registerSmallStairs(BLOCK_SS_BRICK, "brick");
		registerSmallStairs(BLOCK_SS_STONE_BRICK, "stonebrick");
		registerSmallStairs(BLOCK_SS_NETHER_BRICK, "nether_brick");
		registerSmallStairs(BLOCK_SS_RED_NETHER_BRICK, "red_nether_brick");
		registerSmallStairs(BLOCK_SS_STONE, "stone");
		registerSmallStairs(BLOCK_SS_RED_WOOL, "wool_red");
		registerSmallStairs(BLOCK_SS_YELLOW_WOOL, "wool_yellow");
		registerSmallStairs(BLOCK_SS_QUARTZ_BLOCK, "quartz_block");
	}

	/**
	 * 小階段登録
	 *
	 * @param block
	 * @param name
	 */
	public static void registerSmallStairs(Block block, String name)
	{
		ResourceLocation location = new ResourceLocation(((IModId) block).getModId(), "small_stairs_" + name);
		ForgeRegistries.BLOCKS.register(block.setRegistryName(location));
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));

		if(Utils.isClient())
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(location, "inventory"));
		}
	}
}