package vectorwing.farmersdelight.common.utility;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Util for obtaining and formatting ITextComponents for use across the mod.
 */

public class TextUtils
{
	private static final MutableText NO_EFFECTS = Text.translatable("effect.none").formatted(Formatting.GRAY);

	/**
	 * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
	 */
	public static MutableText getTranslation(String key, Object... args) {
		return Text.translatable(FarmersDelight.MODID + "." + key, args);
	}

	/**
	 * An alternate version of PotionUtils.addPotionTooltip, that obtains the item's food-property potion effects instead.
	 */
	public static void addFoodEffectTooltip(ItemStack stack, Consumer<Text> tooltipAdder, float durationFactor, float tickRate) {
		FoodComponent foodStats = stack.get(DataComponentTypes.FOOD);
		if (foodStats == null) {
			return;
		}

		List<FoodComponent.PossibleEffect> effectList = foodStats.effects();
		List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> attributeList = Lists.newArrayList();
		MutableText mutableComponent;

		if (effectList.isEmpty()) {
			tooltipAdder.accept(NO_EFFECTS);
		} else {
			for (FoodComponent.PossibleEffect possibleEffect : effectList) {
				StatusEffectInstance instance = possibleEffect.effect();
				mutableComponent = Text.translatable(instance.getTranslationKey());
				StatusEffect effect = instance.getEffectType().value();
				effect.forEachAttributeModifier(instance.getAmplifier(), (attributeHolder, attributeModifier) -> {
					attributeList.add(new Pair<>(attributeHolder, attributeModifier));
				});

				if (instance.getAmplifier() > 0) {
					mutableComponent = Text.translatable("potion.withAmplifier", mutableComponent, Text.translatable("potion.potency." + instance.getAmplifier()));
				}

				if (instance.getDuration() > 20) {
					mutableComponent = Text.translatable("potion.withDuration", mutableComponent, StatusEffectUtil.getDurationText(instance, durationFactor, tickRate));
				}

				tooltipAdder.accept(mutableComponent.formatted(effect.getCategory().getFormatting()));
			}
		}

		if (!attributeList.isEmpty()) {
			tooltipAdder.accept(ScreenTexts.EMPTY);
			tooltipAdder.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

			for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : attributeList) {
				EntityAttributeModifier attributemodifier = pair.getSecond();
				double amount = attributemodifier.value();
				double formattedAmount;
				if (attributemodifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributemodifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
					formattedAmount = attributemodifier.value();
				} else {
					formattedAmount = attributemodifier.value() * 100.0;
				}

				if (amount > 0.0) {
					tooltipAdder.accept(Text.translatable("attribute.modifier.plus." + attributemodifier.operation().getId(), new Object[]{AttributeModifiersComponent.DECIMAL_FORMAT.format(formattedAmount), Text.translatable(((EntityAttribute) ((RegistryEntry) pair.getFirst()).value()).getTranslationKey())}).formatted(Formatting.BLUE));
				} else if (amount < 0.0) {
					formattedAmount *= -1.0;
					tooltipAdder.accept(Text.translatable("attribute.modifier.take." + attributemodifier.operation().getId(), new Object[]{AttributeModifiersComponent.DECIMAL_FORMAT.format(formattedAmount), Text.translatable(((EntityAttribute) ((RegistryEntry) pair.getFirst()).value()).getTranslationKey())}).formatted(Formatting.RED));
				}
			}
		}
	}
}
