package ryoryo.smallstairs.block;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ryoryo.polishedlib.util.Utils;
import ryoryo.polishedlib.util.enums.EnumAxis;
import ryoryo.polishedlib.util.handlers.ModelHandler;
import ryoryo.polishedlib.util.handlers.RegistryHandler;
import ryoryo.polishedlib.util.interfaces.IGetItemBlock;
import ryoryo.polishedlib.util.interfaces.IModelRegister;

public class BlockSmallStairs extends BlockStairs implements IModelRegister, IGetItemBlock {

	private static final double base00 = 0.0D;
	private static final double base03 = 1.0D / 3.0D;
	private static final double base06 = 2.0D / 3.0D;
	private static final double base10 = 1.0D;
	/**
	 *         (NORTH)
	 *            |
	 *            |
	 * (WEST) ----|----> +x (EAST)
	 *            |
	 *            |
	 *            V
	 *           +z
	 *         (SOUTH)
	 */
	// ********************************************************************************************************************************
	/**
	 * B: xxx M: ... T: ...
	 * B: xxx M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_STRAIGHT_STEP1 = new AxisAlignedBB(base00, base00, base00, base10, base03, base06);
	// ********************************************************************************************************************************
	/**
	 * B: ... M: xxx T: ...
	 * B: ... M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_STRAIGHT_STEP2 = new AxisAlignedBB(base00, base03, base00, base10, base06, base03);
	// ********************************************************************************************************************************
	/**
	 * B: xx. M: ... T: ...
	 * B: xx. M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_OUTER_STEP1 = new AxisAlignedBB(base00, base00, base00, base06, base03, base06);
	// ********************************************************************************************************************************
	/**
	 * B: ... M: x.. T: ...
	 * B: ... M: ... T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB COLLISION_OUTER_STEP2 = new AxisAlignedBB(base00, base03, base00, base03, base06, base03);
	// ********************************************************************************************************************************
	/**
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB SELECT_STRAIGHT = new AxisAlignedBB(base00, base00, base00, base10, base06, base06);
	// ********************************************************************************************************************************
	/**
	 * B: xx. M: xx. T: ...
	 * B: xx. M: xx. T: ...
	 * B: ... M: ... T: ...
	 */
	protected static final AxisAlignedBB SELECT_OUTER = new AxisAlignedBB(base00, base00, base00, base06, base06, base06);
	// ********************************************************************************************************************************
	/**
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 * B: xxx M: xxx T: ...
	 */
	protected static final AxisAlignedBB SELECT_INNER = new AxisAlignedBB(base00, base00, base00, base10, base06, base10);
	// ********************************************************************************************************************************

	public BlockSmallStairs(IBlockState modelState, String name) {
		super(modelState);
		String newName = "small_stairs_" + name;
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setUnlocalizedName(newName);
		this.setRegistryName(newName);
		this.setLightOpacity(0);

		RegistryHandler.register(this);
		RegistryHandler.register(createItemBlock().setRegistryName(newName));
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState) {
		getCollisionBoxList(this.getActualState(state, world, pos))
				.forEach(aabb -> addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb));
	}

	@Override
	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
		return getCollisionBoxList(this.getActualState(state, world, pos)).stream()
				.map(aabb -> this.rayTrace(pos, start, end, aabb))
				.filter(Objects::nonNull)
				.max((result1, result2) -> MathHelper.floor(result1.hitVec.squareDistanceTo(end) - result2.hitVec.squareDistanceTo(end)))
				.orElse(null);
	}

	/**
	 * to return an AABB "in world coords", use AxisAlignedBB.offset(pos)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
		return getSelectionBox(this.getActualState(state, world, pos)).offset(pos);
	}

	private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
		List<AxisAlignedBB> list = Lists.newArrayList();

		BlockStairs.EnumShape shape = state.getValue(SHAPE);

		/**
		 * B: xxx M: xxx T: ...
		 * B: xxx M: ... T: ...
		 * B: ... M: ... T: ...
		 */
		if (shape == BlockStairs.EnumShape.STRAIGHT || shape == BlockStairs.EnumShape.INNER_LEFT || shape == BlockStairs.EnumShape.INNER_RIGHT) {
			list.add(getStraightStep1(state));
			list.add(getStraightStep2(state));
		}

		/**
		 * B: ... M: x.. T: ...
		 * B: xx. M: x.. T: ...
		 * B: xx. M: x.. T: ...
		 */
		if (shape == BlockStairs.EnumShape.INNER_LEFT || shape == BlockStairs.EnumShape.INNER_RIGHT) {
			list.add(getOuterStep1(state));
			list.add(getInnerStep2(state));
		}

		/**
		 * B: xx. M: x.. T: ...
		 * B: xx. M: ... T: ...
		 * B: ... M: ... T: ...
		 */
		if (shape == BlockStairs.EnumShape.OUTER_LEFT || shape == BlockStairs.EnumShape.OUTER_RIGHT) {
			list.add(getOuterStep1(state));
			list.add(getOuterStep2(state));
		}

		return list;
	}

	private static AxisAlignedBB getSelectionBox(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);
		EnumShape shape = state.getValue(SHAPE);
		EnumAxis axis = state.getValue(HALF) == EnumHalf.TOP ? EnumAxis.Y : EnumAxis.NONE;
		AxisAlignedBB outerBase = shape == EnumShape.OUTER_LEFT ? SELECT_OUTER : Utils.flipAABB(SELECT_OUTER, EnumAxis.X);

		switch (shape) {
			default:
			case STRAIGHT:
				return Utils.rotateAABB(Utils.flipAABB(SELECT_STRAIGHT, axis),
						Utils.getRotationFromNorth(facing));
			case OUTER_LEFT:
			case OUTER_RIGHT:
				return Utils.rotateAABB(Utils.flipAABB(outerBase, axis),
						Utils.getRotationFromNorth(facing));
			case INNER_LEFT:
			case INNER_RIGHT:
				return Utils.flipAABB(SELECT_INNER, axis);
		}
	}

	/**
	 * use for EnumShape.STRAIGHT, EnumShape.INNER_LEFT and
	 * EnumShape.INNER_RIGHT
	 *
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getStraightStep1(IBlockState state) {
		return Utils.rotateAABB(Utils.flipYAABB(COLLISION_STRAIGHT_STEP1, state.getValue(HALF) == BlockStairs.EnumHalf.TOP),
				Utils.getRotationFromNorth(state.getValue(FACING)));
	}

	/**
	 * use for EnumShape.STRAIGHT, EnumShape.INNER_LEFT and
	 * EnumShape.INNER_RIGHT
	 *
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getStraightStep2(IBlockState state) {
		return Utils.rotateAABB(COLLISION_STRAIGHT_STEP2, Utils.getRotationFromNorth(state.getValue(FACING)));
	}

	/**
	 * use for EnumShape.INNER_LEFT and EnumShape.INNER_RIGHT
	 *
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getInnerStep2(IBlockState state) {
		return Utils.rotateAABB(COLLISION_STRAIGHT_STEP2,
				Utils.getRotationFromNorth(getRotateFacing(state.getValue(FACING), state.getValue(SHAPE))));
	}

	/**
	 * use for EnumShape.OUTER_LEFT, EnumShape.OUTER_RIGHT, EnumShape.INNER_LEFT
	 * and EnumShape.INNER_RIGHT
	 *
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getOuterStep1(IBlockState state) {
		return Utils.rotateAABB(Utils.flipYAABB(COLLISION_OUTER_STEP1, state.getValue(HALF) == BlockStairs.EnumHalf.TOP),
				Utils.getRotationFromNorth(getRotateFacing(state.getValue(FACING), state.getValue(SHAPE))));
	}

	/**
	 * use for EnumShape.OUTER_LEFT and EnumShape.OUTER_RIGHT
	 *
	 * @param state
	 * @return
	 */
	private static AxisAlignedBB getOuterStep2(IBlockState state) {
		return Utils.rotateAABB(COLLISION_OUTER_STEP2,
				Utils.getRotationFromNorth(getRotateFacing(state.getValue(FACING), state.getValue(SHAPE))));
	}

	private static EnumFacing getRotateFacing(EnumFacing facing, EnumShape shape) {
		switch (shape) {
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

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelHandler.registerBlockModel(this);
	}
}