package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;

public class WildCropFeature extends Feature<WildCropConfiguration>
{
	public WildCropFeature(Codec<WildCropConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<WildCropConfiguration> context) {
		WildCropConfiguration config = context.getConfig();
		BlockPos origin = context.getOrigin();
		StructureWorldAccess level = context.getWorld();
		Random random = context.getRandom();

		int i = 0;
		int tries = config.tries();
		int xzSpread = config.xzSpread() + 1;
		int ySpread = config.ySpread() + 1;

		BlockPos.Mutable mutablePos = new BlockPos.Mutable();

		RegistryEntry<PlacedFeature> floorFeature = config.floorFeature();
		if (floorFeature != null) {
			for (int j = 0; j < tries; ++j) {
				mutablePos.set(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
				if (config.floorFeature().value().generateUnregistered(level, context.getGenerator(), random, mutablePos)) {
					++i;
				}
			}
		}

		for (int k = 0; k < tries; ++k) {
			int shorterXZ = xzSpread - 2;
			mutablePos.set(origin, random.nextInt(shorterXZ) - random.nextInt(shorterXZ), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(shorterXZ) - random.nextInt(shorterXZ));
			if (config.primaryFeature().value().generateUnregistered(level, context.getGenerator(), random, mutablePos)) {
				++i;
			}
		}

		for (int l = 0; l < tries; ++l) {
			mutablePos.set(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
			if (config.secondaryFeature().value().generateUnregistered(level, context.getGenerator(), random, mutablePos)) {
				++i;
			}
		}

		return i > 0;
	}
}
