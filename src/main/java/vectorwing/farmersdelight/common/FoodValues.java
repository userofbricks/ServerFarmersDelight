package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
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

	public static StatusEffectInstance comfort(int duration) {
		return new StatusEffectInstance(ModEffects.COMFORT, duration, 0, false, false);
	}

	public static StatusEffectInstance nourishment(int duration) {
		return new StatusEffectInstance(ModEffects.NOURISHMENT, duration, 0, false, false);
	}

	// Raw Crops
	public static final FoodComponent CABBAGE = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.4f).build();
	public static final FoodComponent TOMATO = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.3f).build();
	public static final FoodComponent ONION = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.4f).build();

	// Drinks (mostly for effects)
	public static final FoodComponent APPLE_CIDER = (new FoodComponent.Builder())
			.alwaysEdible().effect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 0), 1.0F).build();

	// Basic Foods
	public static final FoodComponent FRIED_EGG = (new FoodComponent.Builder())
			.nutrition(4).saturationModifier(0.4f).build();
	public static final FoodComponent TOMATO_SAUCE = (new FoodComponent.Builder())
			.nutrition(4).saturationModifier(0.4f).build();
	public static final FoodComponent WHEAT_DOUGH = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.3f).effect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F).build();
	public static final FoodComponent RAW_PASTA = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.3F).effect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F).build();
	public static final FoodComponent PIE_CRUST = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.2f).build();
	public static final FoodComponent PUMPKIN_SLICE = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.3f).build();
	public static final FoodComponent CABBAGE_LEAF = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.4f).fast().build();
	public static final FoodComponent MINCED_BEEF = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.3f).fast().build();
	public static final FoodComponent BEEF_PATTY = (new FoodComponent.Builder())
			.nutrition(4).saturationModifier(0.8f).fast().build();
	public static final FoodComponent CHICKEN_CUTS = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.3f).effect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F).fast().build();
	public static final FoodComponent COOKED_CHICKEN_CUTS = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.6f).fast().build();
	public static final FoodComponent BACON = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.3f).fast().build();
	public static final FoodComponent COOKED_BACON = (new FoodComponent.Builder())
			.nutrition(4).saturationModifier(0.8f).fast().build();
	public static final FoodComponent COD_SLICE = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.1f).fast().build();
	public static final FoodComponent COOKED_COD_SLICE = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.5f).fast().build();
	public static final FoodComponent SALMON_SLICE = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.1f).fast().build();
	public static final FoodComponent COOKED_SALMON_SLICE = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.8f).fast().build();
	public static final FoodComponent MUTTON_CHOPS = (new FoodComponent.Builder())
			.nutrition(1).saturationModifier(0.3f).fast().build();
	public static final FoodComponent COOKED_MUTTON_CHOPS = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.8f).fast().build();
	public static final FoodComponent HAM = (new FoodComponent.Builder())
			.nutrition(5).saturationModifier(0.3f).build();
	public static final FoodComponent SMOKED_HAM = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.8f).build();

	// Sweets
	public static final FoodComponent POPSICLE = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.2f).fast().alwaysEdible().build();
	public static final FoodComponent COOKIES = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.1f).fast().build();
	public static final FoodComponent CAKE_SLICE = (new FoodComponent.Builder())
			.nutrition(2).saturationModifier(0.1f).fast()
			.effect(new StatusEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F).build();
	public static final FoodComponent PIE_SLICE = (new FoodComponent.Builder())
			.nutrition(3).saturationModifier(0.3f).fast()
			.effect(new StatusEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F).build();
	public static final FoodComponent FRUIT_SALAD = (new FoodComponent.Builder())
			.nutrition(6).saturationModifier(0.6f)
			.effect( new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodComponent GLOW_BERRY_CUSTARD = (new FoodComponent.Builder())
			.nutrition(7).saturationModifier(0.6f).alwaysEdible()
			.effect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 0), 1.0F).build();

	// Handheld Foods
	public static final FoodComponent MIXED_SALAD = (new FoodComponent.Builder())
			.nutrition(6).saturationModifier(0.6f)
			.effect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodComponent NETHER_SALAD = (new FoodComponent.Builder())
			.nutrition(5).saturationModifier(0.4f)
			.effect(new StatusEffectInstance(MobEffects.CONFUSION, 240, 0), 0.3F).build();
	public static final FoodComponent BARBECUE_STICK = (new FoodComponent.Builder())
			.nutrition(8).saturationModifier(0.9f).build();
	public static final FoodComponent EGG_SANDWICH = (new FoodComponent.Builder())
			.nutrition(8).saturationModifier(0.8f).build();
	public static final FoodComponent CHICKEN_SANDWICH = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent HAMBURGER = (new FoodComponent.Builder())
			.nutrition(11).saturationModifier(0.8f).build();
	public static final FoodComponent BACON_SANDWICH = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent MUTTON_WRAP = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodComponent DUMPLINGS = (new FoodComponent.Builder())
			.nutrition(8).saturationModifier(0.8f).build();
	public static final FoodComponent STUFFED_POTATO = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.7f).build();
	public static final FoodComponent CABBAGE_ROLLS = (new FoodComponent.Builder())
			.nutrition(5).saturationModifier(0.5f).build();
	public static final FoodComponent SALMON_ROLL = (new FoodComponent.Builder())
			.nutrition(7).saturationModifier(0.6f).build();
	public static final FoodComponent COD_ROLL = (new FoodComponent.Builder())
			.nutrition(7).saturationModifier(0.6f).build();
	public static final FoodComponent KELP_ROLL = new FoodComponent(12, 12, false, 2.4f, Optional.empty(), List.of());
	public static final FoodComponent KELP_ROLL_SLICE = (new FoodComponent.Builder())
			.nutrition(6).saturationModifier(0.5f).fast().build();

	// Bowl Foods
	public static final FoodComponent COOKED_RICE = (new FoodComponent.Builder())
			.nutrition(6).saturationModifier(0.4f)
			.effect(comfort(BRIEF_DURATION), 1.0F).build();
	public static final FoodComponent BONE_BROTH = (new FoodComponent.Builder())
			.nutrition(8).saturationModifier(0.7f)
			.effect(comfort(SHORT_DURATION), 1.0F).build();
	public static final FoodComponent BEEF_STEW = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(comfort(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent VEGETABLE_SOUP = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(comfort(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent FISH_STEW = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(comfort(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent CHICKEN_SOUP = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();
	public static final FoodComponent FRIED_RICE = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();
	public static final FoodComponent PUMPKIN_SOUP = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();
	public static final FoodComponent BAKED_COD_STEW = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();
	public static final FoodComponent NOODLE_SOUP = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();

	// Plated Foods
	public static final FoodComponent BACON_AND_EGGS = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.6f)
			.effect(nourishment(SHORT_DURATION), 1.0F).build();
	public static final FoodComponent RATATOUILLE = (new FoodComponent.Builder())
			.nutrition(10).saturationModifier(0.6f)
			.effect(nourishment(SHORT_DURATION), 1.0F).build();
	public static final FoodComponent STEAK_AND_POTATOES = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(nourishment(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent PASTA_WITH_MEATBALLS = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(nourishment(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent PASTA_WITH_MUTTON_CHOP = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(nourishment(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent MUSHROOM_RICE = (new FoodComponent.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.effect(nourishment(MEDIUM_DURATION), 1.0F).build();
	public static final FoodComponent ROASTED_MUTTON_CHOPS = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();
	public static final FoodComponent VEGETABLE_NOODLES = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();
	public static final FoodComponent SQUID_INK_PASTA = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();
	public static final FoodComponent GRILLED_SALMON = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(MEDIUM_DURATION), 1.0F).build();

	// Feast Portions
	public static final FoodComponent ROAST_CHICKEN = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();
	public static final FoodComponent STUFFED_PUMPKIN = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(comfort(LONG_DURATION), 1.0F).build();
	public static final FoodComponent HONEY_GLAZED_HAM = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();
	public static final FoodComponent SHEPHERDS_PIE = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.effect(nourishment(LONG_DURATION), 1.0F).build();

	public static final FoodComponent DOG_FOOD = (new FoodComponent.Builder())
			.nutrition(4).saturationModifier(0.2f).build();

	// Vanilla SoupItems
	public static final Map<Item, FoodComponent> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, FoodComponent>())
			.put(Items.MUSHROOM_STEW, (new FoodComponent.Builder())
					.effect(comfort(MEDIUM_DURATION), 1.0F).build())
			.put(Items.BEETROOT_SOUP, (new FoodComponent.Builder())
					.effect(comfort(MEDIUM_DURATION), 1.0F).build())
			.put(Items.RABBIT_STEW, (new FoodComponent.Builder())
					.effect(comfort(LONG_DURATION), 1.0F).build())
			.build();

	public static final FoodComponent RABBIT_STEW_BUFF = (new FoodComponent.Builder())
			.nutrition(14).saturationModifier(0.75f).effect(comfort(LONG_DURATION), 1.0F).build();
}
