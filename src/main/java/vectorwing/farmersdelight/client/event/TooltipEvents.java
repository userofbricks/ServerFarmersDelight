package vectorwing.farmersdelight.client.event;

import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TooltipEvents
{
	public static void addTooltipToVanillaSoups(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
		if (!Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			return;
		}

		Item food = stack.getItem();
		FoodComponent soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

		if (soupEffects != null) {
			for (FoodComponent.PossibleEffect effect : soupEffects.effects()) {
				StatusEffectInstance effectInstance = effect.effect();
				MutableText effectText = Text.translatable(effectInstance.getTranslationKey());
				PlayerEntity player = MinecraftClient.getInstance().player;
				if (effectInstance.getDuration() > 20) {
					effectText = Text.translatable("potion.withDuration", effectText, StatusEffectUtil.getDurationText(effectInstance, 1, player == null ? 20 : player.getWorld().getTickManager().getTickRate()));
				}
				lines.add(effectText.formatted(effectInstance.getEffectType().value().getCategory().getFormatting()));
			}
		}
	}
}
