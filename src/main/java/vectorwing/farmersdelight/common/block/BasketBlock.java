package vectorwing.farmersdelight.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

@SuppressWarnings("deprecation")
public class BasketBlock extends BlockWithEntity implements Waterloggable
{
	public static final MapCodec<BasketBlock> CODEC = createCodec(BasketBlock::new);

	public static final EnumProperty<Direction> FACING = Properties.FACING;
	public static final BooleanProperty ENABLED = Properties.ENABLED;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public static final VoxelShape OUT_SHAPE = VoxelShapes.fullCube();
	public static final VoxelShape RENDER_SHAPE = Block.createCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	@SuppressWarnings("UnstableApiUsage")
	public static final ImmutableMap<Direction, VoxelShape> COLLISION_SHAPE_FACING =
			Maps.immutableEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
					.put(Direction.DOWN, makeHollowCubeShape(Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.UP, makeHollowCubeShape(Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)))
					.put(Direction.NORTH, makeHollowCubeShape(Block.createCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.SOUTH, makeHollowCubeShape(Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D)))
					.put(Direction.WEST, makeHollowCubeShape(Block.createCuboidShape(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.EAST, makeHollowCubeShape(Block.createCuboidShape(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D)))
					.build());

	private static VoxelShape makeHollowCubeShape(VoxelShape cutout) {
		return VoxelShapes.combine(OUT_SHAPE, cutout, BooleanBiFunction.ONLY_FIRST).simplify();
	}

	public BasketBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE_FACING.get(state.get(FACING));
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockView level, BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED, WATERLOGGED);
	}

	@Override
	public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!level.isClient) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof BasketBlockEntity) {
				player.openHandledScreen((BasketBlockEntity) tileEntity);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof Inventory) {
				ItemScatterer.spawn(level, pos, (Inventory) tileEntity);
				level.updateComparators(pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (state.get(WATERLOGGED)) {
			level.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(level));
		}

		return super.getStateForNeighborUpdate(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	// --- HOPPER STUFF ---

	@Override
	public void neighborChanged(BlockState state, World level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean isPowered = !level.isReceivingRedstonePower(pos);
		if (isPowered != state.get(ENABLED)) {
			level.setBlockState(pos, state.with(ENABLED, isPowered), 4);
		}
	}

	// --- BARREL STUFF ---

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World level, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(level.getBlockEntity(pos));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
		return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite()).setValue(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView level, BlockPos pos) {
		return OUT_SHAPE;
	}

	@Override
	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BasketBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClient ? null : BlockWithEntity.validateTicker(blockEntityType, ModBlockEntityTypes.BASKET.get(), BasketBlockEntity::pushItemsTick);
	}
}
