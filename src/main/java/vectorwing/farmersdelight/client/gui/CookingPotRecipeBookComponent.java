package vectorwing.farmersdelight.client.gui;

import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CookingPotRecipeBookComponent extends RecipeBookWidget
{
	protected static final ButtonTextures RECIPE_BOOK_BUTTONS = new ButtonTextures(
			Identifier.of(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
			Identifier.of(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
			Identifier.of(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
			Identifier.of(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted"));

	@Override
	protected void setBookButtonTexture() {
		this.toggleCraftableButton.setTextures(RECIPE_BOOK_BUTTONS);
	}

	public void hide() {
		this.setOpen(false);
	}

	@Override
	@NotNull
	protected Text getToggleCraftableButtonText() {
		return TextUtils.getTranslation("container.recipe_book.cookable");
	}

	@Override
	public void setupGhostRecipe(RecipeEntry<?> recipe, List<Slot> slots) {
		ItemStack resultStack = recipe.value().getResultItem(this.client.world.getRegistryManager());
		this.ghostRecipe.setRecipe(recipe);
		if (slots.get(6).getStack().isEmpty()) {
			this.ghostRecipe.addIngredient(Ingredient.ofItem(resultStack), (slots.get(6)).x, (slots.get(6)).y);
		}

		if (recipe.value() instanceof CookingPotRecipe cookingRecipe) {
			ItemStack containerStack = cookingRecipe.getOutputContainer();
			if (!containerStack.isEmpty()) {
				this.ghostRecipe.addIngredient(Ingredient.ofItem(containerStack), (slots.get(7)).x, (slots.get(7)).y);
			}
		}

		this.placeRecipe(this.craftingScreenHandler.getGridWidth(), this.craftingScreenHandler.getGridHeight(), this.craftingScreenHandler.getResultSlotIndex(), recipe, recipe.value().getIngredients().iterator(), 0);
	}
}
