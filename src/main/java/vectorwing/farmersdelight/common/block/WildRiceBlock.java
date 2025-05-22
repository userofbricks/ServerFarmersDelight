package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@SuppressWarnings("deprecation")
public class WildRiceBlock extends TallPlantBlock implements Waterloggable, Fertilizable
{
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public WildRiceBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		FluidState fluid = level.getFluidState(pos);
		BlockPos floorPos = pos.down();
		if (state.get(TallPlantBlock.HALF) == DoubleBlockHalf.LOWER) {
			return super.canPlaceAt(state, level, pos) && this.canPlantOnTop(level.getBlockState(floorPos), level, floorPos) && fluid.isIn(FluidTags.WATER) && fluid.getLevel() == 8;
		}
		return super.canPlaceAt(state, level, pos) && level.getBlockState(pos.down()).getBlock() == ModBlocks.WILD_RICE.get();
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView getter, BlockPos pos) {
		return state.isIn(BlockTags.DIRT) || state.isOf(Blocks.SAND);
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
		return false;
	}

	@Override
	public void onPlaced(World level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		level.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		BlockState currentState = super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
		DoubleBlockHalf half = stateIn.get(HALF);
		if (!currentState.isAir()) {
			level.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(level));
		}
		if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.get(HALF) != half) {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canPlaceAt(level, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockPos pos = context.getBlockPos();
		FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
		return pos.getY() < context.getWorld().getMaxBuildHeight() - 1
				&& fluid.isIn(FluidTags.WATER)
				&& fluid.getLevel() == 8
				&& context.getWorld().getBlockState(pos.up()).isAir()
				? super.getPlacementState(context) : null;
	}

	@Override
	public boolean canPlaceLiquid(@Nullable PlayerEntity player, BlockView level, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER
				? Fluids.WATER.getStill(false)
				: Fluids.EMPTY.getDefaultState();
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World level, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.3F;
	}

	@Override
	public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
		dropStack(level, pos, new ItemStack(this));
	}
}
