package vectorwing.farmersdelight.common.mixin.refabricated;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.item.enchantment.BackstabbingEnchantment;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, World level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private float handleBackstabbingDamage(float original, DamageSource source) {
        if (original > 0) {
            SkilletItem.SkilletEvents.playSkilletAttackSound((LivingEntity)(Object)this, source);
            // You'd be multiplying with 0 if you were to do this with any value <= 0.
            return BackstabbingEnchantment.BackstabbingEvent.onKnifeBackstab((LivingEntity)(Object)this, source, original);
        }
        return original;
    }

    @ModifyExpressionValue(method = "onClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean onlyAllowTomatoClimbingWhilstRopelogged(boolean original, @Local BlockPos pos, @Local BlockState state) {
        return original && (!(state.getBlock() instanceof TomatoVineBlock tomato) || tomato.isLadder(state, this.getWorld(), pos, (LivingEntity)(Object)this));
    }

    @ModifyVariable(method = "knockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double handleKnifeKnockback(double strength) {
        return KnifeItem.KnifeEvents.onKnifeKnockback(strength, (LivingEntity)(Object)this);
    }

}