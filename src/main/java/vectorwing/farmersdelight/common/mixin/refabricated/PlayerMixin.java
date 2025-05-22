package vectorwing.farmersdelight.common.mixin.refabricated;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.item.SkilletItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public abstract float getAttackStrengthScale(float adjustTicks);

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void captureAttackStrengthScale(Entity target, CallbackInfo ci) {
        SkilletItem.SkilletEvents.attackPower = this.getAttackStrengthScale(0.0F);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void handleSkilletAttackSound(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        SkilletItem.SkilletEvents.playSkilletAttackSound(this, source);
    }
}
