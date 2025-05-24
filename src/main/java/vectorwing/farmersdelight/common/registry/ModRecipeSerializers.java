package vectorwing.farmersdelight.common.registry;

import net.minecraft.recipe.RecipeSerializer;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import java.util.function.Supplier;

import static vectorwing.farmersdelight.refabricated.RegUtils.regRecipeSerializer;

public class ModRecipeSerializers {
    public static final Supplier<RecipeSerializer<CookingPotRecipe>> COOKING = regRecipeSerializer("cooking", CookingPotRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<CuttingBoardRecipe>> CUTTING = regRecipeSerializer("cutting", CuttingBoardRecipe.Serializer::new);

	public static void touch() {

	}
}
