package vectorwing.farmersdelight.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import java.util.List;
import java.util.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class SmokerCookFunction extends ConditionalLootFunction
{
	public static final Identifier ID = Identifier.of(FarmersDelight.MODID, "smoker_cook");
	public static final MapCodec<SmokerCookFunction> CODEC = RecordCodecBuilder.mapCodec(
			p_298131_ -> addConditionsField(p_298131_).apply(p_298131_, SmokerCookFunction::new)
	);

	protected SmokerCookFunction(List<LootCondition> conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected ItemStack process(ItemStack stack, LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		} else {
			Optional<RecipeEntry<SmokingRecipe>> recipe = context.getWorld().getRecipeManager().getAllOfType(RecipeType.SMOKING).stream()
					.filter(r -> r.value().getIngredientPlacement().getIngredients().get(0).test(stack)).findFirst();
			if (recipe.isPresent()) {
				ItemStack result = recipe.get().value().result().copy();
				result.setCount(result.getCount() * stack.getCount());
				return result;
			} else {
				return stack;
			}
		}
	}

	@Override
	public LootFunctionType<SmokerCookFunction> getType() {
		return ModLootFunctions.SMOKER_COOK.get();
	}
}
