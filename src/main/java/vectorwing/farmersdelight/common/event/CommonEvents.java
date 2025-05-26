package vectorwing.farmersdelight.common.event;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.FoodValues;

public class CommonEvents {
    //called by mixin

    public static void onItemUseFinished(World level, LivingEntity livingEntity, ItemStack stack) {
        handleVanillaSoupEffects(level, livingEntity, stack);
    }

    public static void handleVanillaSoupEffects(World level, LivingEntity livingEntity, ItemStack stack) {
        Item food = stack.getItem();

        if (food.equals(Items.RABBIT_STEW)) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 200, 1));
        }

        ConsumableComponent soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);
        if (soupEffects != null) {
            soupEffects.consume(livingEntity, stack, Hand.MAIN_HAND);
        }
    }

}
