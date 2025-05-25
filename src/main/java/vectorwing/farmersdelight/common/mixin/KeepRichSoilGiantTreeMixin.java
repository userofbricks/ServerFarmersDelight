package vectorwing.farmersdelight.common.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(Feature.class)
public class KeepRichSoilGiantTreeMixin
{
	/**
	 * Due to how Trees generate, this mixin is needed to prevent Rich Soil from becoming Podzol under a Giant Spruce Tree growth.
	 */
	@Inject(at = @At(value = "HEAD"), method = "isSoil(Lnet/minecraft/world/TestableWorld;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	private static void keepRichSoil(TestableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (world.testBlockState(pos, state -> state.isOf(ModBlocks.RICH_SOIL.get()))) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
