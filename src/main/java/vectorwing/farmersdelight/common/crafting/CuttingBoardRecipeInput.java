package vectorwing.farmersdelight.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record CuttingBoardRecipeInput(ItemStack item, ItemStack tool) implements RecipeInput {
	@Override
	public ItemStack getStackInSlot(int index) {
		return switch (index) {
			case 0 -> this.item;
			case 1 -> this.tool;
			default -> throw new IllegalArgumentException("Recipe does not contain slot " + index);
		};
	}

	@Override
	public int size() {
		return 2;
	}
}