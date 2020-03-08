package ryoryo.smallstairs.proxy;

import static net.minecraftforge.oredict.OreDictionary.*;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ryoryo.polishedlib.util.Utils;
import ryoryo.polishedlib.util.enums.EnumColor;
import ryoryo.polishedlib.util.enums.EnumPlanks;
import ryoryo.smallstairs.SmallStairs;
import ryoryo.smallstairs.block.ModBlocks;
import ryoryo.smallstairs.util.References;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		ModBlocks.init();
	}

	public void init(FMLInitializationEvent event)
	{
		//ミニ階段
		Block[] small_stairs = new Block[]
		{ ModBlocks.BLOCK_SS_OAK_PLANKS, ModBlocks.BLOCK_SS_SPRUCE_PLANKS, ModBlocks.BLOCK_SS_BIRCH_PLANKS, ModBlocks.BLOCK_SS_JUNGLE_PLANKS, ModBlocks.BLOCK_SS_ACACIA_PLANKS, ModBlocks.BLOCK_SS_DARK_OAK_PLANKS };
		for(int i = 0; i < 6; i++)
		{
			addRecipeSmallStairs(EnumPlanks.byMeta(i).getName(), small_stairs[i], new ItemStack(Blocks.PLANKS, 1, i));
		}
		addRecipeSmallStairs("cobblestone", ModBlocks.BLOCK_SS_COBBLESTONE, new ItemStack(Blocks.COBBLESTONE));
		addRecipeSmallStairs("sandstone", ModBlocks.BLOCK_SS_SANDSTONE, new ItemStack(Blocks.SANDSTONE));
		addRecipeSmallStairs("red_sandstone", ModBlocks.BLOCK_SS_RED_SANDSTONE, new ItemStack(Blocks.RED_SANDSTONE));
		addRecipeSmallStairs("brick", ModBlocks.BLOCK_SS_BRICK, new ItemStack(Blocks.BRICK_BLOCK));
		addRecipeSmallStairs("stone_brick", ModBlocks.BLOCK_SS_STONE_BRICK, new ItemStack(Blocks.STONEBRICK, 1, WILDCARD_VALUE));
		addRecipeSmallStairs("nether_brick", ModBlocks.BLOCK_SS_NETHER_BRICK, new ItemStack(Blocks.NETHER_BRICK));
		addRecipeSmallStairs("red_nether_brick", ModBlocks.BLOCK_SS_RED_NETHER_BRICK, new ItemStack(Blocks.RED_NETHER_BRICK));
		addRecipeSmallStairs("stone", ModBlocks.BLOCK_SS_STONE, new ItemStack(Blocks.STONE));
		addRecipeSmallStairs("red_wool", ModBlocks.BLOCK_SS_RED_WOOL, EnumColor.RED.getWoolOreName());
		addRecipeSmallStairs("yellow_wool", ModBlocks.BLOCK_SS_YELLOW_WOOL, EnumColor.YELLOW.getWoolOreName());
		addRecipeSmallStairs("quartz", ModBlocks.BLOCK_SS_QUARTZ_BLOCK, new ItemStack(Blocks.QUARTZ_BLOCK, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
	}

	public void postInit(FMLPostInitializationEvent event)
	{
	}

	public void loadComplete(FMLLoadCompleteEvent event)
	{
	}

	public static void addRecipe(String name, ItemStack output, Object... params)
	{
		Utils.addRecipe(References.MOD_ID, name, output, params);
	}

	public static void addShapelessRecipe(String name, @Nonnull ItemStack output, Object... params)
	{
		Utils.addShapelessRecipe(References.MOD_ID, name, output, params);
	}

	/**
	 * 小さい階段レシピ登録
	 * 名前の前に"small_stairs_"と足される
	 * @param name
	 * @param output
	 * @param material
	 */
	public static void addRecipeSmallStairs(String name, Block output, Object material)
	{
		int quantity = 6;
		if(Loader.isModLoaded("quark"))
			quantity = 9;
		else
			SmallStairs.logger.addInfo("Quark isn't loaded.");

		addRecipe("small_stairs_" + name, new ItemStack(output, quantity), "M ", "MM", 'M', material);
	}
}