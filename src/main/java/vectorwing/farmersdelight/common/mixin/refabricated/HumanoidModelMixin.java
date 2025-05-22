package vectorwing.farmersdelight.common.mixin.refabricated;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

// TODO: Handle differently if the Skillet changes get merged.
@Mixin(BipedEntityModel.class)
public class HumanoidModelMixin {
    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getUsedItemHand()Lnet/minecraft/world/InteractionHand;"))
    private <T extends LivingEntity> void farmersdelightrefabricated$setupSkilletThirdPersonAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ItemStack stack = entity.getActiveItem();
        if (stack.getItem() instanceof SkilletItem && stack.contains(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
            long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
            float partialTicks = MinecraftClient.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
            float animation = ((entity.getWorld().getTime() - time) + partialTicks) / SkilletItem.FLIP_TIME;
            animation = MathHelper.clamp(animation, 0, 1);

            if (entity.getMainArm() == Arm.LEFT) {
                leftArm.pitch = (-MathHelper.sin(animation * MathHelper.TAU) * 15 - 20) * (float) (Math.PI / 180.0);
            } else {
                rightArm.pitch = (-MathHelper.sin(animation * MathHelper.TAU) * 15 - 20) * (float) (Math.PI / 180.0);
            }
        }
    }
}
