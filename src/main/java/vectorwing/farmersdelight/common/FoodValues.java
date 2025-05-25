package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class FoodValues
{
	public static final int BRIEF_DURATION = 600;    // 30 seconds
	public static final int SHORT_DURATION = 1200;    // 1 minute
	public static final int MEDIUM_DURATION = 3600;    // 3 minutes
	public static final int LONG_DURATION = 6000;    // 5 minutes

	public static ApplyEffectsConsumeEffect comfort(int duration) {
		return new ApplyEffectsConsumeEffect(new StatusEffectInstance(ModEffects.COMFORT, duration, 0, false, false));
	}

	public static ApplyEffectsConsumeEffect nourishment(int duration) {
		return new ApplyEffectsConsumeEffect(new StatusEffectInstance(ModEffects.NOURISHMENT, duration, 0, false, false));
	}

	// Raw Crops
	public static final FoodComponent CABBAGE = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.4f).build();
	public static final FoodComponent TOMATO = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.3f).build();
	public static final FoodComponent ONION = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.4f).build();

	// Drinks (mostly for effects)
	public static final FoodComponent APPLE_CIDER = (new FoodComponent.Builder()).alwaysEdible().build();
	public static final ConsumableComponent APPLE_CIDER_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 0))).build();

	// Basic Foods
	public static final FoodComponent FRIED_EGG = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.4f).build();
	public static final FoodComponent TOMATO_SAUCE = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.4f).build();
	public static final FoodComponent WHEAT_DOUGH = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3f).build();
	public static final ConsumableComponent WHEAT_DOUGH_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F)).build();
	public static final FoodComponent RAW_PASTA = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3F).build();
	public static final ConsumableComponent RAW_PASTA_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F)).build();
	public static final FoodComponent PIE_CRUST = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.2f).build();
	public static final FoodComponent PUMPKIN_SLICE = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.3f).build();
	public static final FoodComponent CABBAGE_LEAF = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.4f).build();
	public static final FoodComponent MINCED_BEEF = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3f).build();
	public static final FoodComponent BEEF_PATTY = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.8f).build();
	public static final FoodComponent CHICKEN_CUTS = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.3f).build();
	public static final ConsumableComponent CHICKEN_CUTS_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F)).build();
	public static final FoodComponent COOKED_CHICKEN_CUTS = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.6f).build();
	public static final FoodComponent BACON = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.3f).build();
	public static final FoodComponent COOKED_BACON = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.8f).build();
	public static final FoodComponent COD_SLICE = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.1f).build();
	public static final FoodComponent COOKED_COD_SLICE = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.5f).build();
	public static final FoodComponent SALMON_SLICE = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.1f).build();
	public static final FoodComponent COOKED_SALMON_SLICE = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.8f).build();
	public static final FoodComponent MUTTON_CHOPS = (new FoodComponent.Builder()).nutrition(1).saturationModifier(0.3f).build();
	public static final FoodComponent COOKED_MUTTON_CHOPS = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.8f).build();
	public static final FoodComponent HAM = (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.3f).build();
	public static final FoodComponent SMOKED_HAM = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.8f).build();

	// Sweets
	public static final FoodComponent POPSICLE = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.2f).alwaysEdible().build();
	public static final FoodComponent COOKIES = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.1f).build();
	public static final FoodComponent CAKE_SLICE = (new FoodComponent.Builder()).nutrition(2).saturationModifier(0.1f).build();
	public static final ConsumableComponent CAKE_SLICE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0, false, false))).build();
	public static final FoodComponent PIE_SLICE = (new FoodComponent.Builder()).nutrition(3).saturationModifier(0.3f).build();
	public static final ConsumableComponent PIE_SLICE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0, false, false))).build();
	public static final FoodComponent FRUIT_SALAD = (new FoodComponent.Builder()).nutrition(6).saturationModifier(0.6f).build();
	public static final ConsumableComponent FRUIT_SALAD_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0))).build();
	public static final FoodComponent GLOW_BERRY_CUSTARD = (new FoodComponent.Builder()).nutrition(7).saturationModifier(0.6f).alwaysEdible().build();
	public static final ConsumableComponent GLOW_BERRY_CUSTARD_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0))).build();

	// Handheld Foods
	public static final FoodComponent MIXED_SALAD = (new FoodComponent.Builder()).nutrition(6).saturationModifier(0.6f).build();
	public static final ConsumableComponent MIXED_SALAD_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0))).build();
	public static final FoodComponent NETHER_SALAD = (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.4f).build();
	public static final ConsumableComponent NETHER_SALAD_CONSUMABLE = ConsumableComponent.builder().consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 240, 0), 0.3F)).build();
	public static final FoodComponent BARBECUE_STICK = (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.9f).build();
	public static final FoodComponent EGG_SANDWICH = (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.8f).build();
	public static final FoodComponent CHICKEN_SANDWICH = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent HAMBURGER = (new FoodComponent.Builder()).nutrition(11).saturationModifier(0.8f).build();
	public static final FoodComponent BACON_SANDWICH = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent MUTTON_WRAP = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent DUMPLINGS = (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.8f).build();
	public static final FoodComponent STUFFED_POTATO = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.7f).build();
	public static final FoodComponent CABBAGE_ROLLS = (new FoodComponent.Builder()).nutrition(5).saturationModifier(0.5f).build();
	public static final FoodComponent SALMON_ROLL = (new FoodComponent.Builder()).nutrition(7).saturationModifier(0.6f).build();
	public static final FoodComponent COD_ROLL = (new FoodComponent.Builder()).nutrition(7).saturationModifier(0.6f).build();
	public static final FoodComponent KELP_ROLL = new FoodComponent(12, 12, false);
	public static final FoodComponent KELP_ROLL_SLICE = (new FoodComponent.Builder()).nutrition(6).saturationModifier(0.5f).build();

	// Bowl Foods
	public static final FoodComponent COOKED_RICE = (new FoodComponent.Builder()).nutrition(6).saturationModifier(0.4f).build();
	public static final ConsumableComponent COOKED_RICE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(BRIEF_DURATION)).build();
	public static final FoodComponent BONE_BROTH = (new FoodComponent.Builder()).nutrition(8).saturationModifier(0.7f).build();
	public static final ConsumableComponent BONE_BROTH_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(SHORT_DURATION)).build();
	public static final FoodComponent BEEF_STEW = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent BEEF_STEW_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(MEDIUM_DURATION)).build();
	public static final FoodComponent VEGETABLE_SOUP = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent VEGETABLE_SOUP_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(MEDIUM_DURATION)).build();
	public static final FoodComponent FISH_STEW = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent FISH_STEW_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(MEDIUM_DURATION)).build();
	public static final FoodComponent CHICKEN_SOUP = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent CHICKEN_SOUP_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
	public static final FoodComponent FRIED_RICE = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent FRIED_RICE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
	public static final FoodComponent PUMPKIN_SOUP = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent PUMPKIN_SOUP_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
	public static final FoodComponent BAKED_COD_STEW = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent BAKED_COD_STEW_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
	public static final FoodComponent NOODLE_SOUP = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent NOODLE_SOUP_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();

	// Plated Foods
	public static final FoodComponent BACON_AND_EGGS = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.6f).build();
	public static final ConsumableComponent BACON_AND_EGGS_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(SHORT_DURATION)).build();
	public static final FoodComponent RATATOUILLE = (new FoodComponent.Builder()).nutrition(10).saturationModifier(0.6f).build();
	public static final ConsumableComponent RATATOUILLE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(SHORT_DURATION)).build();
	public static final FoodComponent STEAK_AND_POTATOES = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent STEAK_AND_POTATOES_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(MEDIUM_DURATION)).build();
	public static final FoodComponent PASTA_WITH_MEATBALLS = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent PASTA_WITH_MEATBALLS_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(MEDIUM_DURATION)).build();
	public static final FoodComponent PASTA_WITH_MUTTON_CHOP = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent PASTA_WITH_MUTTON_CHOP_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(MEDIUM_DURATION)).build();
	public static final FoodComponent MUSHROOM_RICE = (new FoodComponent.Builder()).nutrition(12).saturationModifier(0.8f).build();
	public static final ConsumableComponent MUSHROOM_RICE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(MEDIUM_DURATION)).build();
	public static final FoodComponent ROASTED_MUTTON_CHOPS = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent ROASTED_MUTTON_CHOPS_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();
	public static final FoodComponent VEGETABLE_NOODLES = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent VEGETABLE_NOODLES_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();
	public static final FoodComponent SQUID_INK_PASTA = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent SQUID_INK_PASTA_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();
	public static final FoodComponent GRILLED_SALMON = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent GRILLED_SALMON_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(MEDIUM_DURATION)).build();

	// Feast Portions
	public static final FoodComponent ROAST_CHICKEN = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent ROAST_CHICKEN_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();
	public static final FoodComponent STUFFED_PUMPKIN = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent STUFFED_PUMPKIN_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
	public static final FoodComponent HONEY_GLAZED_HAM = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent HONEY_GLAZED_HAM_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();
	public static final FoodComponent SHEPHERDS_PIE = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent SHEPHERDS_PIE_CONSUMABLE = ConsumableComponent.builder().consumeEffect(nourishment(LONG_DURATION)).build();

	public static final FoodComponent DOG_FOOD = (new FoodComponent.Builder()).nutrition(4).saturationModifier(0.2f).build();

	// Vanilla SoupItems
	public static final Map<Item, ConsumableComponent> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, ConsumableComponent>())
			.put(Items.MUSHROOM_STEW, ConsumableComponent.builder()
					.consumeParticles(false)
					.consumeEffect(comfort(MEDIUM_DURATION)).build())
			.put(Items.BEETROOT_SOUP, ConsumableComponent.builder()
					.consumeParticles(false)
					.consumeEffect(comfort(MEDIUM_DURATION)).build())
			.put(Items.RABBIT_STEW, ConsumableComponent.builder()
					.consumeParticles(false)
					.consumeEffect(comfort(LONG_DURATION)).build()).build();

	public static final FoodComponent RABBIT_STEW_BUFF = (new FoodComponent.Builder()).nutrition(14).saturationModifier(0.75f).build();
	public static final ConsumableComponent RABBIT_STEW_BUFF_CONSUMABLE = ConsumableComponent.builder().consumeEffect(comfort(LONG_DURATION)).build();
}
