package vectorwing.farmersdelight.refabricated;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.context.ContextParameter;
import java.util.Set;
import java.util.function.Supplier;

public record CanItemPerformAbilityCondition(ItemAbility ability) implements LootCondition {
    public static final MapCodec<CanItemPerformAbilityCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ItemAbility.CODEC.fieldOf("ability").forGetter(CanItemPerformAbilityCondition::ability)
    ).apply(inst, CanItemPerformAbilityCondition::new));
    public static final Supplier<LootConditionType> TYPE = RegUtils.regLootCond("can_item_perform_ability", () -> new LootConditionType(CODEC));

    public static void init() {

    }

    @Override
    public boolean test(LootContext context) {
        ItemStack stack = context.get(LootContextParameters.TOOL);
        return ability.canPerformAction(stack);
    }

    @Override
    public Set<ContextParameter<?>> getAllowedParameters() {
        return Set.of(LootContextParameters.TOOL);
    }

    @Override
    public LootConditionType getType() {
        return TYPE.get();
    }
}
