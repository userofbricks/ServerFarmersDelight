package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.tag.ModTags;

/**
 * Blocks that can be heated by the block below them.
 * This checks for the HEAT_SOURCES and HEAT_CONDUCTORS tag to determine heat state.
 * If the heat source has a LIT state, it must be "true" in order to give heat.
 */
public interface HeatableBlockEntity
{
	/**
	 * Checks for heat sources below the block. If it can, it also checks for conducted heat.
	 */
	default boolean isHeated(World level, BlockPos pos) {
		BlockState stateBelow = level.getBlockState(pos.down());

		if (stateBelow.isIn(ModTags.HEAT_SOURCES)) {
			if (stateBelow.contains(Properties.LIT))
				return stateBelow.get(Properties.LIT);
			return true;
		}

		if (!this.requiresDirectHeat() && stateBelow.isIn(ModTags.HEAT_CONDUCTORS)) {
			BlockState stateFurtherBelow = level.getBlockState(pos.down(2));
			if (stateFurtherBelow.isIn(ModTags.HEAT_SOURCES)) {
				if (stateFurtherBelow.contains(Properties.LIT))
					return stateFurtherBelow.get(Properties.LIT);
				return true;
			}
		}

		return false;
	}

	/**
	 * Determines if this block can only be heated directly, excluding conductors.
	 */
	default boolean requiresDirectHeat() {
		return false;
	}
}
