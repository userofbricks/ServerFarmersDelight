package vectorwing.farmersdelight.common.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.tag.ModTags;

@Mixin(CampfireBlock.class)
public abstract class CampfireBaleMixin
{
	@Inject(at = @At("HEAD"), method = "isSmokeSource", cancellable = true)
	public void isFDSmokeSource(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.isIn(ModTags.CAMPFIRE_SIGNAL_SMOKE)) {
			cir.setReturnValue(true);
		}
	}
}