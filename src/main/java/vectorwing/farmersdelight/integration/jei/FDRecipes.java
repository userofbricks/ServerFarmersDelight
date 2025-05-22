package vectorwing.farmersdelight.integration.jei;

import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;

public class FDRecipes
{
	private final ServerRecipeManager recipeManager;

	public FDRecipes() {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		ClientWorld level = minecraft.world;

		if (level != null) {
			this.recipeManager = level.getRecipeManager();
		} else {
			throw new NullPointerException("minecraft world must not be null.");
		}
	}

	public List<CookingPotRecipe> getCookingPotRecipes() {
		return recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get()).stream().map(RecipeEntry::value).toList();
	}

	public List<CuttingBoardRecipe> getCuttingBoardRecipes() {
		return recipeManager.getAllRecipesFor(ModRecipeTypes.CUTTING.get()).stream().map(RecipeEntry::value).toList();
	}
}
