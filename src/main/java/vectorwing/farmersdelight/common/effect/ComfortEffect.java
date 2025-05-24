package vectorwing.farmersdelight.common.effect;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

@SuppressWarnings("unused")
public class ComfortEffect extends StatusEffect
{
	/**
	 * This effect extends the player's natural regeneration, regardless of how hungry they are.
	 * Comfort does not care for amplifiers; it will always heal at the same slow pace.
	 * If the player has saturation to spend, or has the Regeneration effect, Comfort does nothing.
	 */
	public ComfortEffect() {
		super(StatusEffectCategory.BENEFICIAL, 14545909);
	}

	@Override
	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		if (entity.hasStatusEffect(StatusEffects.REGENERATION)) {
			return true;
		}
		if (entity instanceof PlayerEntity player) {
			if (player.getHungerManager().getSaturationLevel() > 0.0) {
				return true;
			}
		}
		if (entity.getHealth() < entity.getMaxHealth()) {
			entity.heal(1.0F);
		}
		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return duration % 80 == 0;
	}
}
