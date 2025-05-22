package vectorwing.farmersdelight.common.mixin.refabricated;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.event.CommonEvents;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "finishUsingItem", at = @At(value = "TAIL"))
    private void fdrf$onItemUseFinished(World level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        CommonEvents.onItemUseFinished(level, livingEntity, (ItemStack) (Object) this);
    }
}
