package vectorwing.farmersdelight.common.item.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class BackstabbingEnchantment
{
	/**
	 * Determines whether the attacker is facing a 90-100 degree cone behind the target's looking direction.
	 */
	public static boolean isLookingBehindTarget(LivingEntity target, Vec3d attackerLocation) {
		if (attackerLocation != null) {
			Vec3d lookingVector = target.getRotationVec(1.0F);
			Vec3d attackAngleVector = attackerLocation.subtract(target.getPos()).normalize();
			attackAngleVector = new Vec3d(attackAngleVector.x, 0.0D, attackAngleVector.z);
			return attackAngleVector.dotProduct(lookingVector) < -0.5D;
		}
		return false;
	}

	public static float getBackstabbingDamagePerLevel(float amount, int level) {
		float multiplier = ((level * 0.2F) + 1.2F);
		return amount * multiplier;
	}

	public static class BackstabbingEvent
	{
		/*
		 * Moved impl to LivingEntityMixin because PortingLib does not support
		 * stacking values within their LivingHurtEvent equivalent.
		 */
		@SuppressWarnings("unused")
		public static float onKnifeBackstab(LivingEntity entity, DamageSource source, float amount) {
			Entity attacker = source.getAttacker();
			if (attacker instanceof LivingEntity living && isLookingBehindTarget(entity, source.getPosition())) {
				World level = attacker.getWorld();
				if (level instanceof ServerWorld serverLevel) {
					ItemStack weapon = living.getWeaponStack(); // since you play a sound on success, we record the original to do a change check later
					MutableFloat dmg = new MutableFloat(amount);
					EnchantmentHelper.forEachEnchantment(weapon, (enchantment, powerLevel) -> {
						enchantment.value().modifyValue(ModDataComponents.BACKSTABBING.get(), serverLevel, powerLevel, weapon, attacker, source, dmg);
					});

					if (amount != dmg.getValue()) {
						amount = dmg.getValue();
						serverLevel.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			return amount;
		}
	}
}
