package vectorwing.farmersdelight.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BuddingTomatoBlock extends BuddingBushBlock implements Fertilizable
{
	public BuddingTomatoBlock(Settings properties) {
		super(properties);
	}

	@Override
	public boolean canPlantOnTop(BlockState pState, BlockView pLevel, BlockPos pPos) {
		return pState.isOf(ModBlocks.RICH_SOIL_FARMLAND.get()) || pState.isOf(Blocks.FARMLAND);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (state.get(BuddingBushBlock.AGE) == 4) {
			level.setBlockState(currentPos, ModBlocks.TOMATO_CROP.get().getDefaultState(), 3);
		}
		return super.getStateForNeighborUpdate(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canGrowPastMaxAge() {
		return true;
	}

	@Override
	public void growPastMaxAge(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		level.setBlockState(pos, ModBlocks.TOMATO_CROP.get().getDefaultState());
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World level, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(World level) {
		return MathHelper.nextInt(level.random, 1, 4);
	}

	@Override
	public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
		int maxAge = getMaxAge();
		int ageGrowth = Math.min(getAge(state) + getBonemealAgeIncrease(level), 7);
		if (ageGrowth <= maxAge) {
			level.setBlockState(pos, state.with(AGE, ageGrowth));
		} else {
			int remainingGrowth = ageGrowth - maxAge - 1;
			level.setBlockState(pos, ModBlocks.TOMATO_CROP.get().getDefaultState().with(TomatoVineBlock.VINE_AGE, remainingGrowth));
		}
	}
}
