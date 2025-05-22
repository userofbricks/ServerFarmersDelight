package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.block.*;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class RichSoilFarmlandBlock extends FarmlandBlock
{
	public RichSoilFarmlandBlock(Settings properties) {
		super(properties);
	}

	private static boolean hasWater(WorldView level, BlockPos pos) {
		for (BlockPos nearbyPos : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (level.getFluidState(nearbyPos).isIn(FluidTags.WATER)) {
				return true;
			}
		}
		// There is no FarmlandWaterManager alternative on Fabric.
		// return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
		return false;
	}

	public static void turnToRichSoil(BlockState state, World level, BlockPos pos) {
		level.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, ModBlocks.RICH_SOIL.get().getDefaultState(), level, pos));
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		BlockState aboveState = level.getBlockState(pos.up());
		return super.canPlaceAt(state, level, pos) || aboveState.getBlock().equals(Blocks.MELON) || aboveState.getBlock().equals(Blocks.PUMPKIN);
	}

	public boolean isFertile(BlockState state, BlockView world, BlockPos pos) {
		if (state.isOf(ModBlocks.RICH_SOIL_FARMLAND.get()))
			return state.get(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
		if (!state.canPlaceAt(level, pos)) {
			turnToRichSoil(state, level, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		int moisture = state.get(MOISTURE);
		if (!hasWater(level, pos) && !level.hasRain(pos.up())) {
			if (moisture > 0) {
				level.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
			}
		} else if (moisture < 7) {
			level.setBlockState(pos, state.with(MOISTURE, 7), 2);
		} else if (moisture == 7) {
			if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
				return;
			}

			BlockPos abovePos = pos.up();
			BlockState aboveState = level.getBlockState(abovePos);
			Block aboveBlock = aboveState.getBlock();

			if (aboveState.isIn(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			if (aboveBlock instanceof Fertilizable growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
				if (growable.isFertilizable(level, abovePos, aboveState)) {
					growable.grow(level, level.random, abovePos, aboveState);
					level.syncWorldEvent(1505, abovePos, 15);
				}
			}
		}
	}

	/*
	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, BlockState plantState) {
//		PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
//		return plantType == PlantType.CROP || plantType == PlantType.PLAINS;

		// TODO: Revisit this method to filter out plants correctly. Also, there's a chance Rich Soil Farmland won't need it anymore.
		if (plantState.getBlock() instanceof CropBlock) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}
	 */

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return !this.getDefaultState().canPlaceAt(context.getWorld(), context.getBlockPos()) ? ModBlocks.RICH_SOIL.get().getDefaultState() : super.getPlacementState(context);
	}

	@Override
	public void fallOn(World level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		// Rich Soil is immune to trampling
	}
}
