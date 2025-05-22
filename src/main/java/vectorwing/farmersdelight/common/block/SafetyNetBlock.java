package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

;

@SuppressWarnings("deprecation")
public class SafetyNetBlock extends Block implements Waterloggable
{
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 7.0D, 0.0D, 16.0D, 9.0D, 16.0D);

	public SafetyNetBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
		return this.getDefaultState().with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			level.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(level));
		}

		return super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void fallOn(World level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.bypassesLandingEffects()) {
			super.onLandedUpon(level, state, pos, entityIn, fallDistance);
		} else {
			entityIn.handleFallDamage(fallDistance, 0.0F, level.getDamageSources().fall());
		}
	}

	@Override
	public void updateEntityAfterFallOn(BlockView level, Entity entityIn) {
		if (entityIn.bypassesLandingEffects()) {
			super.updateEntityAfterFallOn(level, entityIn);
		} else {
			this.bounceEntity(entityIn);
		}
	}

	private void bounceEntity(Entity entityIn) {
		Vec3d vec3d = entityIn.getVelocity();
		if (vec3d.y < 0.0D) {
			double entityWeightOffset = entityIn instanceof LivingEntity ? 0.6D : 0.8D;
			entityIn.setVelocity(vec3d.x, -vec3d.y * entityWeightOffset, vec3d.z);
		}
	}
}
