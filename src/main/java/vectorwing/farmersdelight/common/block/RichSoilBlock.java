package vectorwing.farmersdelight.common.block;

import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.HoeItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class RichSoilBlock extends Block
{
	public RichSoilBlock(Settings properties) {
		super(properties);
	}

	public static void init() {
		TillableBlockRegistry.register(ModBlocks.RICH_SOIL.get(), HoeItem::canTillFarmland, HoeItem.createTillAction(ModBlocks.RICH_SOIL_FARMLAND.get().getDefaultState()));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random rand) {
		if (!level.isClient) {
			BlockPos abovePos = pos.up();
			BlockState aboveState = level.getBlockState(abovePos);
			Block aboveBlock = aboveState.getBlock();

			// Do nothing if the plant is unaffected by rich soil
			if (aboveState.isIn(ModTags.UNAFFECTED_BY_RICH_SOIL)) {
				return;
			}

			// Convert mushrooms to colonies if it's dark enough
			if (aboveBlock == Blocks.BROWN_MUSHROOM) {
				level.setBlockState(pos.up(), ModBlocks.BROWN_MUSHROOM_COLONY.get().getDefaultState());
				return;
			}
			if (aboveBlock == Blocks.RED_MUSHROOM) {
				level.setBlockState(pos.up(), ModBlocks.RED_MUSHROOM_COLONY.get().getDefaultState());
				return;
			}

			if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
				return;
			}

			// If all else fails, and it's a plant, give it a growth boost now and then!
			if (aboveBlock instanceof Fertilizable growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
				if (growable.isFertilizable(level, pos.up(), aboveState)) {
					growable.grow(level, level.random, pos.up(), aboveState);
					level.syncWorldEvent(1505, pos.up(), 0);
				}
			}
		}
	}
}
