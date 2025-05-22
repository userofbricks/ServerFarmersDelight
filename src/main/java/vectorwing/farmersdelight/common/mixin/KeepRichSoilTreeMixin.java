package vectorwing.farmersdelight.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.function.BiConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.trunk.TrunkPlacer;

@Mixin(TrunkPlacer.class)
public class KeepRichSoilTreeMixin
{
	/**
	 * Due to how Trees generate, this mixin is needed to prevent Rich Soil from becoming Podzol under a Giant Spruce Tree growth.
	 */
	@Inject(at = @At(value = "HEAD"), method = "setDirtAt", cancellable = true)
	private static void cancelSetDirtIfRichSoil(TestableWorld level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, BlockPos pos, TreeFeatureConfig config, CallbackInfo ci) {
		if (level.testBlockState(pos, state -> state.isOf(ModBlocks.RICH_SOIL.get()))) {
			ci.cancel();
		}
	}
}
