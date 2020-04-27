package ryoryo.smallstairs.block;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

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
import ryoryo.polishedlib.util.enums.EnumAxis;
import ryoryo.smallstairs.SmallStairs;

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
	//********************************************************************************************************************************
	/**
	 * B: xxx M: ... T: ...
	 * B: xxx M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_STRAIGHT_STEP1 = new AxisAlignedBB(base00, base00, base00, base10, base03, base06);
	//********************************************************************************************************************************
	/**
	 * B: ... M: xxx T: ...
	 * B: ... M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_STRAIGHT_STEP2 = new AxisAlignedBB(base00, base03, base00, base10, base06, base03);
	//********************************************************************************************************************************
	/**
	 * B: xx. M: ... T: ...
	 * B: xx. M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_OUTER_STEP1 = new AxisAlignedBB(base00, base00, base00, base06, base03, base06);
	//********************************************************************************************************************************
	/**
	 * B: ... M: x.. T: ...
	 * B: ... M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_OUTER_STEP2 = new AxisAlignedBB(base00, base03, base00, base03, base06, base03);
	//********************************************************************************************************************************
	/**
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB SELECT_STRAIGHT = new AxisAlignedBB(base00, base00, base00, base10, base06, base06);
	//********************************************************************************************************************************
	/**
	 * B: xx. M: xx. T: ...
	 * B: xx. M: xx. T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB SELECT_OUTER = new AxisAlignedBB(base00, base00, base00, base06, base06, base06);
	//********************************************************************************************************************************
	/**
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 */
	protected static final AxisAlignedBB SELECT_INNER = new AxisAlignedBB(base00, base00, base00, base10, base06, base10);
	//********************************************************************************************************************************

	private static final Table<EnumFacing, EnumShape, EnumMap<EnumHalf, List<AxisAlignedBB>>> COLLISION_AABBS = HashBasedTable.create();
	private static final Table<EnumFacing, EnumShape, EnumMap<EnumHalf, AxisAlignedBB>> SELECTION_AABBS = HashBasedTable.create();

	static
	{
		long startTime = System.nanoTime();

		Stream.of(EnumFacing.values())
		.filter(f -> (f != EnumFacing.UP && f != EnumFacing.DOWN))
		.forEach(facing ->
		{
			for(EnumShape shape : EnumShape.values())
			{
				EnumMap<EnumHalf, List<AxisAlignedBB>> halfMap = new EnumMap<EnumHalf, List<AxisAlignedBB>>(EnumHalf.class);

				for(EnumHalf half : EnumHalf.values())
				{
					List<AxisAlignedBB> bounds = Lists.newArrayList();

					if(shape == EnumShape.STRAIGHT || shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT)
					{
						bounds.add(getStraightStep1(facing, half));
						bounds.add(getStraightStep2(facing));
					}

					if(shape == EnumShape.INNER_LEFT || shape == EnumShape.INNER_RIGHT)
					{
						bounds.add(getOuterStep1(facing, shape, half));
						bounds.add(getInnerStep2(facing, shape));
					}

					if(shape == EnumShape.OUTER_LEFT || shape == EnumShape.OUTER_RIGHT)
					{
						bounds.add(getOuterStep1(facing, shape, half));
						bounds.add(getOuterStep2(facing, shape));
					}

					halfMap.put(half, bounds);
				}

				COLLISION_AABBS.put(facing, shape, halfMap);
			}
		});

		Stream.of(EnumFacing.values())
		.filter(f -> (f != EnumFacing.UP && f != EnumFacing.DOWN))
		.forEach(facing ->
		{
			for(EnumShape shape : EnumShape.values())
			{
				EnumMap<EnumHalf, AxisAlignedBB> halfMap = new EnumMap<EnumHalf, AxisAlignedBB>(EnumHalf.class);

				for(EnumHalf half : EnumHalf.values())
				{
					EnumAxis axis = half == EnumHalf.TOP ? EnumAxis.Y : EnumAxis.NONE;
					AxisAlignedBB outerBase = shape == EnumShape.OUTER_LEFT ? SELECT_OUTER : Utils.flipAABB(SELECT_OUTER, EnumAxis.X);

					switch(shape)
					{
						default:
						case STRAIGHT:
							halfMap.put(half, Utils.rotateAABB(Utils.flipAABB(SELECT_STRAIGHT, axis),
									Utils.getRotationFromNorth(facing)));
							break;
						case OUTER_LEFT:
						case OUTER_RIGHT:
							halfMap.put(half, Utils.rotateAABB(Utils.flipAABB(outerBase, axis),
									Utils.getRotationFromNorth(facing)));
							break;
						case INNER_LEFT:
						case INNER_RIGHT:
							halfMap.put(half, Utils.flipAABB(SELECT_INNER, axis));
							break;
					}
				}

				SELECTION_AABBS.put(facing, shape, halfMap);
			}
		});

		long endTime = System.nanoTime();
		SmallStairs.LOGGER.info("Initializing collision boxes took " + (endTime-startTime)/1000000F + " ms");
	}

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

	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
	{
		return getCollisionBoxList(this.getActualState(state, world, pos)).stream()
			.map(aabb -> this.rayTrace(pos, start, end, aabb))
			.filter(Objects::nonNull)
			.max((result1, result2) -> (int) (result1.hitVec.squareDistanceTo(end) - result2.hitVec.squareDistanceTo(end)))
			.orElse(null);
	}

	/**
	 * to return an AABB "in world coords", use AxisAlignedBB.offset(pos)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos)
	{
		return getSelectionBox(this.getActualState(state, world, pos)).offset(pos);
	}

	private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state)
	{
		List<AxisAlignedBB> list = COLLISION_AABBS.get(state.getValue(FACING), state.getValue(SHAPE)).get(state.getValue(HALF));
		return list != null ? list : Lists.newArrayList(FULL_BLOCK_AABB);
	}

	private static AxisAlignedBB getSelectionBox(IBlockState state)
	{
		AxisAlignedBB aabb = SELECTION_AABBS.get(state.getValue(FACING), state.getValue(SHAPE)).get(state.getValue(HALF));
		return aabb != null ? aabb : FULL_BLOCK_AABB;
	}

	/**
	 * use for EnumShape.STRAIGHT, EnumShape.INNER_LEFT and EnumShape.INNER_RIGHT
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getStraightStep1(EnumFacing facing, EnumHalf half)
	{
		return Utils.rotateAABB(Utils.flipYAABB(COLLISION_STRAIGHT_STEP1, half == EnumHalf.TOP),
				Utils.getRotationFromNorth(facing));
	}

	/**
	 * use for EnumShape.STRAIGHT, EnumShape.INNER_LEFT and EnumShape.INNER_RIGHT
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getStraightStep2(EnumFacing facing)
	{
		return Utils.rotateAABB(COLLISION_STRAIGHT_STEP2, Utils.getRotationFromNorth(facing));
	}

	/**
	 * use for EnumShape.INNER_LEFT and EnumShape.INNER_RIGHT
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getInnerStep2(EnumFacing facing, EnumShape shape)
	{
		return Utils.rotateAABB(COLLISION_STRAIGHT_STEP2,
				Utils.getRotationFromNorth(getRotateFacing(facing, shape)));
	}

	/**
	 * use for EnumShape.OUTER_LEFT, EnumShape.OUTER_RIGHT, EnumShape.INNER_LEFT and EnumShape.INNER_RIGHT
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getOuterStep1(EnumFacing facing, EnumShape shape, EnumHalf half)
	{
		return Utils.rotateAABB(Utils.flipYAABB(COLLISION_OUTER_STEP1, half == EnumHalf.TOP),
				Utils.getRotationFromNorth(getRotateFacing(facing, shape)));
	}

	/**
	 * use for EnumShape.OUTER_LEFT and EnumShape.OUTER_RIGHT
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getOuterStep2(EnumFacing facing, EnumShape shape)
	{
		return Utils.rotateAABB(COLLISION_OUTER_STEP2,
				Utils.getRotationFromNorth(getRotateFacing(facing, shape)));
	}

	private static EnumFacing getRotateFacing(EnumFacing facing, EnumShape shape)
	{
		switch(shape)
		{
			default:
			case OUTER_LEFT:
				return facing;
			case OUTER_RIGHT:
				return facing.rotateY();
			case INNER_RIGHT:
				return facing.rotateY();
			case INNER_LEFT:
				return facing.rotateYCCW();
		}
	}
}