package ryoryo.smallstairs.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ryoryo.polishedlib.util.Utils;

public class BlockSmallStairs extends BlockStairs
{
	private static final double base00 = 0.0D;
	private static final double base03 = 1.0D / 3.0D;
	private static final double base06 = 2.0D / 3.0D;
	private static final double base10 = 1.0D;
	/**
	 *         (NORTH)
	 *            |
	 *            |
	 * (WEST) ----|---->+x (EAST)
	 *            |
	 *            |
	 *            V
	 *            +z (SOUTH)
	 */
	//************************************************************************************************************************
	/**
	 * B: ... T: xxx
	 * B: ... T: xxx
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_TOP_NORTH = new AxisAlignedBB(base00, base06, base00, base10, base10, base03);
	/**
	 * B: ... T: ...
	 * B: ... T: xxx
	 * B: ... T: xxx
	 */
	protected static final AxisAlignedBB AABB_TOP_SOUTH = new AxisAlignedBB(base00, base06, base03, base10, base10, base10);
	/**
	 * B: ... T: xx.
	 * B: ... T: xx.
	 * B: ... T: xx.
	 */
	protected static final AxisAlignedBB AABB_TOP_WEST = new AxisAlignedBB(base00, base06, base00, base06, base10, base10);
	/**
	 * B: ... T: .xx
	 * B: ... T: .xx
	 * B: ... T: .xx
	 */
	protected static final AxisAlignedBB AABB_TOP_EAST = new AxisAlignedBB(base03, base06, base00, base10, base10, base10);
	//*******************************************************************************************************************************
	/**
	 * B: xxx T: ...
	 * B: xxx T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_BOTTOM_NORTH = new AxisAlignedBB(base00, base00, base00, base10, base03, base06);
	/**
	 * B: ... T: ...
	 * B: xxx T: ...
	 * B: xxx T: ...
	 */
	protected static final AxisAlignedBB AABB_BOTTOM_SOUTH = new AxisAlignedBB(base00, base00, base03, base10, base03, base10);
	/**
	 * B: xx. T: ...
	 * B: xx. T: ...
	 * B: xx. T: ...
	 */
	protected static final AxisAlignedBB AABB_BOTTOM_WEST = new AxisAlignedBB(base00, base00, base00, base06, base03, base10);
	/**
	 * B: .xx T: ...
	 * B: .xx T: ...
	 * B: .xx T: ...
	 */
	protected static final AxisAlignedBB AABB_BOTTOM_EAST = new AxisAlignedBB(base03, base00, base00, base10, base03, base10);
	//********************************************************************************************************************************
	/**
	 * B: xxx T: ...
	 * B: ... T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_QTR_NORTH = new AxisAlignedBB(base00, base03, base00, base10, base06, base03);
	/**
	 * B: ... T: ...
	 * B: ... T: ...
	 * B: xxx T: ...
	 */
	protected static final AxisAlignedBB AABB_QTR_SOUTH = new AxisAlignedBB(base00, base03, base06, base10, base06, base10);
	/**
	 * B: x.. T: ...
	 * B: x.. T: ...
	 * B: x.. T: ...
	 */
	protected static final AxisAlignedBB AABB_QTR_WEST = new AxisAlignedBB(base00, base03, base00, base03, base06, base10);
	/**
	 * B: ..x T: ...
	 * B: ..x T: ...
	 * B: ..x T: ...
	 */
	protected static final AxisAlignedBB AABB_QTR_EAST = new AxisAlignedBB(base06, base03, base00, base10, base06, base10);
	//**************************************************************************************************************************************
	/**
	 * B: x.. T: ...
	 * B: ... T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_OCT_NW = new AxisAlignedBB(base00, base03, base00, base03, base06, base03);
	/**
	 * B: ..x T: ...
	 * B: ... T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_OCT_NE = new AxisAlignedBB(base06, base03, base00, base10, base06, base03);
	/**
	 * B: ... T: ...
	 * B: ... T: ...
	 * B: x.. T: ...
	 */
	protected static final AxisAlignedBB AABB_OCT_SW = new AxisAlignedBB(base00, base03, base06, base03, base06, base10);
	/**
	 * B: ... T: ...
	 * B: ... T: ...
	 * B: ..x T: ...
	 */
	protected static final AxisAlignedBB AABB_OCT_SE = new AxisAlignedBB(base06, base03, base06, base10, base06, base10);
	//*************************************************************************************************************************************
	/**
	 * B: ... T: xx.
	 * B: ... T: xx.
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_TOP_OCT_NW = new AxisAlignedBB(base00, base06, base00, base06, base10, base06);
	/**
	 * B: ... T: .xx
	 * B: ... T: .xx
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_TOP_OCT_NE = new AxisAlignedBB(base03, base06, base00, base10, base10, base06);
	/**
	 * B: ... T: ...
	 * B: ... T: xx.
	 * B: ... T: xx.
	 */
	protected static final AxisAlignedBB AABB_TOP_OCT_SW = new AxisAlignedBB(base00, base06, base03, base06, base10, base10);
	/**
	 * B: ... T: ...
	 * B: ... T: .xx
	 * B: ... T: .xx
	 */
	protected static final AxisAlignedBB AABB_TOP_OCT_SE = new AxisAlignedBB(base03, base06, base03, base10, base10, base10);
	//********************************************************************************************************************************
	/**
	 * B: xx. T: ...
	 * B: xx. T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_BOT_OCT_NW = new AxisAlignedBB(base00, base00, base00, base06, base03, base06);
	/**
	 * B: .xx T: ...
	 * B: .xx T: ...
	 * B: ... T: ...
	 */
	protected static final AxisAlignedBB AABB_BOT_OCT_NE = new AxisAlignedBB(base03, base00, base00, base10, base03, base06);
	/**
	 * B: ... T: ...
	 * B: xx. T: ...
	 * B: xx. T: ...
	 */
	protected static final AxisAlignedBB AABB_BOT_OCT_SW = new AxisAlignedBB(base00, base00, base03, base06, base03, base10);
	/**
	 * B: ... T: ...
	 * B: .xx T: ...
	 * B: .xx T: ...
	 */
	protected static final AxisAlignedBB AABB_BOT_OCT_SE = new AxisAlignedBB(base03, base00, base03, base10, base03, base10);
	//********************************************************************************************************************************
	/**
	 * B: xxx M:xxx T: ...
	 * B: xxx M:xxx T: ...
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_SOUTH = new AxisAlignedBB(base00, base00, base03, base10, base06, base10);
	/**
	 * B: ... M:... T: ...
	 * B: xxx M:xxx T: ...
	 * B: xxx M:xxx T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_NORTH = new AxisAlignedBB(base00, base00, base00, base10, base06, base06);
	/**
	 * B: .xx M:.xx T: ...
	 * B: .xx M:.xx T: ...
	 * B: .xx M:.xx T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_WEST = new AxisAlignedBB(base00, base00, base00, base06, base06, base10);
	/**
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_EAST = new AxisAlignedBB(base03, base00, base00, base10, base06, base10);
	//**********************************************************************************************************************************
	/**
	 * B: ... M:xxx T: xxx
	 * B: ... M:xxx T: xxx
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_TOP_SOUTH = new AxisAlignedBB(base00, base03, base03, base10, base10, base10);
	/**
	 * B: ... M:... T: ...
	 * B: ... M:xxx T: xxx
	 * B: ... M:xxx T: xxx
	 */
	protected static final AxisAlignedBB SELECT_TOP_NORTH = new AxisAlignedBB(base00, base03, base00, base10, base10, base06);
	/**
	 * B: ... M:.xx T: .xx
	 * B: ... M:.xx T: .xx
	 * B: ... M:.xx T: .xx
	 */
	protected static final AxisAlignedBB SELECT_TOP_WEST = new AxisAlignedBB(base00, base03, base00, base06, base10, base10);
	/**
	 * B: ... M:xx. T: xx.
	 * B: ... M:xx. T: xx.
	 * B: ... M:xx. T: xx.
	 */
	protected static final AxisAlignedBB SELECT_TOP_EAST = new AxisAlignedBB(base03, base03, base00, base10, base10, base10);
	//*********************************************************************************************************************************
	/**
	 * B: ... M:xx. T: xx.
	 * B: ... M:xx. T: xx.
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_TOP_OUTER_NW = new AxisAlignedBB(base00, base03, base00, base06, base10, base06);
	/**
	 * B: ... M:.xx T: .xx
	 * B: ... M:.xx T: .xx
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_TOP_OUTER_NE = new AxisAlignedBB(base03, base03, base00, base10, base10, base06);
	/**
	 * B: ... M:... T: ...
	 * B: ... M:xx. T: xx.
	 * B: ... M:xx. T: xx.
	 */
	protected static final AxisAlignedBB SELECT_TOP_OUTER_SW = new AxisAlignedBB(base00, base03, base03, base06, base10, base10);
	/**
	 * B: ... M:... T: ...
	 * B: ... M:.xx T: .xx
	 * B: ... M:.xx T: .xx
	 */
	protected static final AxisAlignedBB SELECT_TOP_OUTER_SE = new AxisAlignedBB(base03, base03, base03, base10, base10, base10);
	//********************************************************************************************************************************
	/**
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_OUTER_NW = new AxisAlignedBB(base00, base00, base00, base06, base06, base06);
	/**
	 * B: .xx M:.xx T: ...
	 * B: .xx M:.xx T: ...
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_OUTER_NE = new AxisAlignedBB(base03, base00, base00, base10, base06, base06);
	/**
	 * B: ... M:... T: ...
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_OUTER_SW = new AxisAlignedBB(base00, base00, base03, base06, base06, base10);
	/**
	 * B: ... M:... T: ...
	 * B: .xx M:.xx T: ...
	 * B: .xx M:.xx T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_OUTER_SE = new AxisAlignedBB(base03, base00, base03, base10, base06, base10);
	//*********************************************************************************************************************************
	/**
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_BOT_INNER = new AxisAlignedBB(base00, base00, base00, base10, base06, base10);
	//*********************************************************************************************************************************
	/**
	 * B: xx. M:xx. T: ...
	 * B: xx. M:xx. T: ...
	 * B: ... M:... T: ...
	 */
	protected static final AxisAlignedBB SELECT_TOP_INNER = new AxisAlignedBB(base00, base03, base00, base10, base10, base10);
	//*********************************************************************************************************************************

	public BlockSmallStairs(IBlockState modelState, String name)
	{
		super(modelState);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setUnlocalizedName("small_stairs_" + name);
		this.setLightOpacity(0);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState)
	{
		getCollisionBoxList(this.getActualState(state, world, pos))
			.forEach(aabb -> addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb));
	}

	private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state)
	{
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();

		BlockStairs.EnumShape shape = (BlockStairs.EnumShape) state.getValue(BlockSmallStairs.SHAPE);

		if(shape == BlockStairs.EnumShape.STRAIGHT || shape == BlockStairs.EnumShape.INNER_LEFT || shape == BlockStairs.EnumShape.INNER_RIGHT)
		{
			list.add(getBaseBlock(state));
			list.add(getCollQuarterBlock(state));
		}

		if(shape != BlockStairs.EnumShape.STRAIGHT)
		{
			list.add(getCollEighthBaseBlock(state));
			list.add(getCollEighthBlock(state));
		}

		return list;
	}

	private static AxisAlignedBB getBaseBlock(IBlockState state)
	{
		boolean flag = state.getValue(BlockSmallStairs.HALF) == BlockStairs.EnumHalf.TOP;
		switch((EnumFacing) state.getValue(BlockSmallStairs.FACING))
		{
		case NORTH:
		default:
			return flag ? AABB_TOP_NORTH : AABB_BOTTOM_NORTH;
		case SOUTH:
			return flag ? AABB_TOP_SOUTH : AABB_BOTTOM_SOUTH;
		case WEST:
			return flag ? AABB_TOP_WEST : AABB_BOTTOM_WEST;
		case EAST:
			return flag ? AABB_TOP_EAST : AABB_BOTTOM_EAST;
		}
	}

	private static AxisAlignedBB getCollEighthBaseBlock(IBlockState state)
	{
		EnumFacing facing = (EnumFacing) state.getValue(BlockSmallStairs.FACING);
		EnumFacing facing1;

		switch((BlockStairs.EnumShape) state.getValue(BlockSmallStairs.SHAPE))
		{
		case OUTER_LEFT:
		default:
			facing1 = facing;
			break;
		case OUTER_RIGHT:
			facing1 = facing.rotateY();
			break;
		case INNER_RIGHT:
			facing1 = facing.getOpposite();
			break;
		case INNER_LEFT:
			facing1 = facing.rotateYCCW();
		}

		boolean flag = state.getValue(BlockSmallStairs.HALF) == BlockStairs.EnumHalf.TOP;
		switch(facing1)
		{
		case NORTH:
		default:
			return flag ? AABB_TOP_OCT_NW : AABB_BOT_OCT_NW;
		case SOUTH:
			return flag ? AABB_TOP_OCT_SE : AABB_BOT_OCT_SE;
		case WEST:
			return flag ? AABB_TOP_OCT_SW : AABB_BOT_OCT_SW;
		case EAST:
			return flag ? AABB_TOP_OCT_NE : AABB_BOT_OCT_NE;
		}
	}

	private static AxisAlignedBB getCollQuarterBlock(IBlockState state)
	{
		switch((EnumFacing) state.getValue(BlockSmallStairs.FACING))
		{
		case NORTH:
		default:
			return AABB_QTR_NORTH;
		case SOUTH:
			return AABB_QTR_SOUTH;
		case WEST:
			return AABB_QTR_WEST;
		case EAST:
			return AABB_QTR_EAST;
		}
	}

	private static AxisAlignedBB getCollEighthBlock(IBlockState state)
	{
		EnumFacing facing = (EnumFacing) state.getValue(BlockSmallStairs.FACING);
		EnumFacing facing1;

		switch((BlockStairs.EnumShape) state.getValue(BlockSmallStairs.SHAPE))
		{
		case OUTER_LEFT:
		default:
			facing1 = facing;
			break;
		case OUTER_RIGHT:
			facing1 = facing.rotateY();
			break;
		case INNER_RIGHT:
			facing1 = facing.getOpposite();
			break;
		case INNER_LEFT:
			facing1 = facing.rotateYCCW();
		}

		switch(facing1)
		{
		case NORTH:
		default:
			return AABB_OCT_NW;
		case SOUTH:
			return AABB_OCT_SE;
		case WEST:
			return AABB_OCT_SW;
		case EAST:
			return AABB_OCT_NE;
		}
	}

	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		return getCollisionBoxList(this.getActualState(blockState, world, pos)).stream()
			.map(aabb -> this.rayTrace(pos, start, end, aabb))
			.filter(Objects::nonNull)
			.max((result1, result2) -> (int) (result1.hitVec.squareDistanceTo(end) - result2.hitVec.squareDistanceTo(end)))
			.orElse(null);
	}

	/**
	 * Return an AABB (""""in world coords!""") that should be highlighted when the player is targeting this Block
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		state = this.getActualState(state, world, pos);
		EnumFacing facing = state.getValue(BlockSmallStairs.FACING);
		EnumShape shape = state.getValue(BlockSmallStairs.SHAPE);
		boolean flag = state.getValue(BlockSmallStairs.HALF) == EnumHalf.TOP;

		if(shape == EnumShape.STRAIGHT)
		{
			switch(facing)
			{
			case NORTH:
			default:
				return flag ? SELECT_TOP_NORTH.offset(pos) : SELECT_BOT_NORTH.offset(pos);
			case SOUTH:
				return flag ? SELECT_TOP_SOUTH.offset(pos) : SELECT_BOT_SOUTH.offset(pos);
			case WEST:
				return flag ? SELECT_TOP_WEST.offset(pos) : SELECT_BOT_WEST.offset(pos);
			case EAST:
				return flag ? SELECT_TOP_EAST.offset(pos) : SELECT_BOT_EAST.offset(pos);
			}
		}
		else if(shape == EnumShape.OUTER_LEFT)
		{
			switch(facing)
			{
			case NORTH:
			default:
				return flag ? SELECT_TOP_OUTER_NW.offset(pos) : SELECT_BOT_OUTER_NW.offset(pos);
			case SOUTH:
				return flag ? SELECT_TOP_OUTER_SE.offset(pos) : SELECT_BOT_OUTER_SE.offset(pos);
			case WEST:
				return flag ? SELECT_TOP_OUTER_SW.offset(pos) : SELECT_BOT_OUTER_SW.offset(pos);
			case EAST:
				return flag ? SELECT_TOP_OUTER_NE.offset(pos) : SELECT_BOT_OUTER_NE.offset(pos);
			}
		}
		else if(shape == EnumShape.OUTER_RIGHT)
		{
			switch(facing)
			{
			case NORTH:
			default:
				return flag ? SELECT_TOP_OUTER_NE.offset(pos) : SELECT_BOT_OUTER_NE.offset(pos);
			case SOUTH:
				return flag ? SELECT_TOP_OUTER_SW.offset(pos) : SELECT_BOT_OUTER_SW.offset(pos);
			case WEST:
				return flag ? SELECT_TOP_OUTER_NW.offset(pos) : SELECT_BOT_OUTER_NW.offset(pos);
			case EAST:
				return flag ? SELECT_TOP_OUTER_SE.offset(pos) : SELECT_BOT_OUTER_SE.offset(pos);

			}
		}
		else if(shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT)
		{
			return flag ? SELECT_TOP_INNER.offset(pos) : SELECT_BOT_INNER.offset(pos);
		}
		else
			return Utils.creatAABB(0, 0, 0, 16, 8, 16).offset(pos);
	}
}