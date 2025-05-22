package vectorwing.farmersdelight.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public class NourishmentEffect extends StatusEffect
{
	/**
	 * This effect prevents hunger loss by constantly decreasing the exhaustion level.
	 * If the player can spend saturation to heal damage, the effect halts to let them do so, until they can't any more.
	 * This means players can grow hungry by healing damage, but no further than 1.5 points, allowing them to eat more and keep healing.
	 */
	public NourishmentEffect() {
		super(StatusEffectCategory.BENEFICIAL, 15971072);
	}

	public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
		if (!entity.getEntityWorld().isClient && entity instanceof PlayerEntity player) {
			HungerManager foodData = player.getHungerManager();
			boolean isPlayerHealingWithHunger =
					world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
							&& player.canFoodHeal()
							&& foodData.getFoodLevel() >= 18;
			if (!isPlayerHealingWithHunger) {
				float exhaustion = foodData.getSaturationLevel();
				float reduction = Math.min(exhaustion, 4.0F);
				if (exhaustion > 0.0F) {
					player.addExhaustion(-reduction);
				}
			}
		}

		return true;
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}
