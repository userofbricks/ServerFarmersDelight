package vectorwing.farmersdelight.common.block;

import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class RopeBlock extends PaneBlock
{
	public static final BooleanProperty TIED_TO_BELL = BooleanProperty.of("tied_to_bell");
	protected static final VoxelShape LOWER_SUPPORT_AABB = Block.createCuboidShape(7, 0, 7, 9, 1, 9);

	public RopeBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.stateManager.getDefaultState()
				.with(HorizontalConnectingBlock.NORTH, false)
				.with(HorizontalConnectingBlock.SOUTH, false)
				.with(HorizontalConnectingBlock.EAST, false)
				.with(HorizontalConnectingBlock.WEST, false)
				.with(TIED_TO_BELL, false)
				.with(HorizontalConnectingBlock.WATERLOGGED, false)
		);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return true;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockView world = context.getWorld();
		BlockPos posAbove = context.getBlockPos().up();
		BlockState state = super.getPlacementState(context);
		return state != null ? state.with(TIED_TO_BELL, world.getBlockState(posAbove).getBlock() == Blocks.BELL) : null;
	}

	@Override
	public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (player.shouldCancelInteraction()) {
			if (player.getAbilities().allowModifyWorld && (player.getAbilities().creativeMode || player.getInventory().insertStack(new ItemStack(this.asItem())))) {
				BlockPos.Mutable reelingPos = pos.mutableCopy().move(Direction.DOWN);
				int minBuildHeight = level.getBottomY();

				while (reelingPos.getY() >= minBuildHeight) {
					BlockState blockStateBelow = level.getBlockState(reelingPos);
					if (blockStateBelow.isOf(this)) {
						reelingPos.move(Direction.DOWN);
					} else {
						reelingPos.move(Direction.UP);
						level.breakBlock(reelingPos, false, player);
						return ActionResult.SUCCESS;
					}
				}
			}
		} else {
			BlockPos.Mutable bellRingingPos = pos.mutableCopy().move(Direction.UP);

			for (int i = 0; i < 24; i++) {
				BlockState blockStateAbove = level.getBlockState(bellRingingPos);
				Block blockAbove = blockStateAbove.getBlock();
				if (blockAbove == Blocks.BELL) {
					((BellBlock) blockAbove).ring(level, bellRingingPos, blockStateAbove.get(BellBlock.FACING).rotateYClockwise());
					return ActionResult.SUCCESS;
				} else if (blockAbove == ModBlocks.ROPE.get()) {
					bellRingingPos.move(Direction.UP);
				} else {
					return ActionResult.PASS;
				}
			}
		}

		return ActionResult.PASS;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getSidesShape(BlockState pState, BlockView pReader, BlockPos pPos) {
		return LOWER_SUPPORT_AABB;
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
		return useContext.getStack().getItem() == this.asItem();
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(HorizontalConnectingBlock.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		boolean tiedToBell= world.getBlockState(pos.up()).getBlock() == Blocks.BELL;

		return direction.getAxis().isHorizontal()
				? state.with(TIED_TO_BELL, tiedToBell).with(HorizontalConnectingBlock.FACING_PROPERTIES.get(direction), this.connectsTo(neighborState, neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())))
				: super.getStateForNeighborUpdate(state.with(TIED_TO_BELL, tiedToBell), world, tickView, pos, direction, neighborPos, neighborState, random);
	}


	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalConnectingBlock.NORTH, HorizontalConnectingBlock.EAST, HorizontalConnectingBlock.WEST, HorizontalConnectingBlock.SOUTH, HorizontalConnectingBlock.WATERLOGGED, TIED_TO_BELL);
	}
}