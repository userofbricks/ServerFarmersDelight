package vectorwing.farmersdelight.common.event;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

public class CommonEvents {
    //called by mixin

    public static void onItemUseFinished(World level, LivingEntity livingEntity, ItemStack stack) {
        handleVanillaSoupEffects(level, livingEntity, stack);
    }

    public static void handleVanillaSoupEffects(World level, LivingEntity livingEntity, ItemStack stack) {
        Item food = stack.getItem();

        if (Configuration.RABBIT_STEW_BUFF.get() && food.equals(Items.RABBIT_STEW)) {
            livingEntity.addStatusEffect(new StatusEffectInstance(MobEffects.JUMP, 200, 1));
        }

        if (Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
            FoodComponent soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

            if (soupEffects != null) {
                for (FoodComponent.PossibleEffect effect : soupEffects.effects()) {
                    livingEntity.addStatusEffect(effect.effect());
                }
            }
        }
    }

}
