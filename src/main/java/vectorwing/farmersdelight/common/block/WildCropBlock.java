package vectorwing.farmersdelight.common.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WildCropBlock extends FlowerBlock implements Fertilizable
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

	public WildCropBlock(RegistryEntry<StatusEffect> suspiciousStewEffect, int effectDuration, Settings properties) {
		super(suspiciousStewEffect, effectDuration, properties);
		FlammableBlockRegistry.getDefaultInstance().add(this, this.getFlammability(null, null, null, null), this.getFireSpreadSpeed(null, null, null, null));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return state.isIn(BlockTags.DIRT) || state.isIn(BlockTags.SAND);
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
		return false;
	}

	public int getFireSpreadSpeed(BlockState state, BlockView world, BlockPos pos, Direction face) {
		return 60;
	}

	public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World level, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.8F;
	}

	@Override
	public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
		int wildCropLimit = 10;

		for (BlockPos nearbyPos : BlockPos.iterate(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
			if (level.getBlockState(nearbyPos).isOf(this)) {
				--wildCropLimit;
				if (wildCropLimit <= 0) {
					return;
				}
			}
		}

		BlockPos randomPos = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

		for (int k = 0; k < 4; ++k) {
			if (level.isAir(randomPos) && state.canPlaceAt(level, randomPos)) {
				pos = randomPos;
			}

			randomPos = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
		}

		if (level.isAir(randomPos) && state.canPlaceAt(level, randomPos)) {
			level.setBlockState(randomPos, state, 2);
		}
	}
}
