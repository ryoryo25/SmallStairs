package ryoryo.smallstairs.itemblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ryoryo.smallstairs.block.BlockSmallStairs;

public class ItemBlockSmallStairs extends ItemBlock
{
	public ItemBlockSmallStairs(Block block)
	{
		super(block);

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if(!block.isReplaceable(world, pos))
		{
			pos = pos.offset(facing);
		}

		if(player.canPlayerEdit(pos, facing, stack) && this.block.canPlaceBlockAt(world, pos))
		{
			EnumFacing enumfacing = EnumFacing.fromAngle((double) player.rotationYaw);
			placeStairs(world, pos, enumfacing, this.block, player, hitY);
			SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.FAIL;
		}
	}

	public static void placeStairs(World world, BlockPos pos, EnumFacing facing, Block stairs, EntityPlayer player, float hitY)
	{
		IBlockState iblockstate = stairs.getDefaultState().withProperty(BlockSmallStairs.FACING, facing).withProperty(BlockSmallStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
		world.setBlockState(pos, facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate.withProperty(BlockSmallStairs.HALF, BlockStairs.EnumHalf.BOTTOM) : iblockstate.withProperty(BlockSmallStairs.HALF, BlockStairs.EnumHalf.TOP), 2);
	}
}
