package vectorwing.farmersdelight.common.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(BlockRenderManager.class)
public abstract class HideBlockBreakProgressMixin
{
	@Inject(method = "renderBreakingTexture", at = @At("HEAD"), cancellable = true)
	private void hideBlockDamage(BlockState state, BlockPos pos, BlockRenderView level, MatrixStack poseStack, VertexConsumer consumer, CallbackInfo ci) {
		if (state.getBlock() == ModBlocks.CANVAS_RUG.get()) {
			ci.cancel();
		}
	}
}
