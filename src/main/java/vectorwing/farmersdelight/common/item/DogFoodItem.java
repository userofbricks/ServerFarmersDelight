package vectorwing.farmersdelight.common.item;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.entity.animal.Wolf;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class DogFoodItem extends ConsumableItem
{
	public static final List<StatusEffectInstance> EFFECTS = Lists.newArrayList(
			new StatusEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0),
			new StatusEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 0),
			new StatusEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0));

	public DogFoodItem(net.minecraft.item.Item.Settings properties) {
		super(properties);
	}

	public static void init(){
		UseEntityCallback.EVENT.register(DogFoodEvent::onDogFoodApplied);
	}

	public static class DogFoodEvent
	{
		public static ActionResult onDogFoodApplied(PlayerEntity player, World level, Hand hand, Entity target,
											@Nullable EntityHitResult entityHitResult) {
			if (player.isSpectator())
				return ActionResult.PASS;

			ItemStack itemStack = player.getStackInHand(hand);

			if (target instanceof LivingEntity entity && target.getType().isIn(ModTags.DOG_FOOD_USERS)) {
				boolean isTameable = entity instanceof TameableEntity;

				if (entity.isAlive() && (!isTameable || ((TameableEntity) entity).isTamed()) && itemStack.getItem().equals(ModItems.DOG_FOOD.get())) {
					entity.setHealth(entity.getMaxHealth());
					for (StatusEffectInstance effect : EFFECTS) {
						entity.addStatusEffect(new StatusEffectInstance(effect));
					}
					entity.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for (int i = 0; i < 5; ++i) {
						double xSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						double ySpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						double zSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						entity.getWorld().addParticleClient(ModParticleTypes.STAR.get(), entity.getParticleX(1.0D), entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), xSpeed, ySpeed, zSpeed);
					}

					if (itemStack.getRecipeRemainder() != ItemStack.EMPTY && !player.isCreative()) {
						player.giveItemStack(itemStack.getRecipeRemainder());
						itemStack.decrement(1);
					}

					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType isAdvanced) {
		if (!Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			return;
		}

		MutableText textWhenFeeding = TextUtils.getTranslation("tooltip.dog_food.when_feeding");
		tooltip.add(textWhenFeeding.formatted(Formatting.GRAY));

		for (StatusEffectInstance effectInstance : EFFECTS) {
			MutableText effectDescription = Text.literal(" ");
			MutableText effectName = Text.translatable(effectInstance.getTranslationKey());
			effectDescription.append(effectName);
			StatusEffect effect = effectInstance.getEffectType().value();

			if (effectInstance.getAmplifier() > 0) {
				effectDescription.append(" ").append(Text.translatable("potion.potency." + effectInstance.getAmplifier()));
			}

			if (effectInstance.getDuration() > 20) {
				effectDescription.append(" (").append(StatusEffectUtil.getDurationText(effectInstance, 1.0F, context.getUpdateTickRate())).append(")");
			}

			tooltip.add(effectDescription.formatted(effect.getCategory().getFormatting()));
		}
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof Wolf wolf) {
			if (wolf.isAlive() && wolf.isTame()) {
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
