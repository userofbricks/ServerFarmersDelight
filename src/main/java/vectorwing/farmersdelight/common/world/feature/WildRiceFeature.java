package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import vectorwing.farmersdelight.common.block.WildRiceBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class WildRiceFeature extends Feature<RandomPatchFeatureConfig>
{
	public WildRiceFeature(Codec<RandomPatchFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean generate(FeatureContext<RandomPatchFeatureConfig> context) {
		StructureWorldAccess level = context.getWorld();
		BlockPos origin = context.getOrigin();
		RandomPatchFeatureConfig config = context.getConfig();
		Random rand = context.getRandom();

		BlockPos blockpos = level.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, origin);

		int i = 0;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (int j = 0; j < config.tries(); ++j) {
			blockpos$mutable.set(blockpos).move(
					rand.nextInt(config.xzSpread() + 1) - rand.nextInt(config.xzSpread() + 1),
					rand.nextInt(config.ySpread() + 1) - rand.nextInt(config.ySpread() + 1),
					rand.nextInt(config.xzSpread() + 1) - rand.nextInt(config.xzSpread() + 1));

			if (level.getBlockState(blockpos$mutable).getBlock() == Blocks.WATER && level.getBlockState(blockpos$mutable.up()).getBlock() == Blocks.AIR) {
				BlockState bottomRiceState = ModBlocks.WILD_RICE.get().getDefaultState().with(WildRiceBlock.HALF, DoubleBlockHalf.LOWER);
				if (bottomRiceState.canPlaceAt(level, blockpos$mutable)) {
					TallPlantBlock.placeAt(level, bottomRiceState, blockpos$mutable, 2);
					++i;
				}
			}
		}

		return i > 0;
	}
}
