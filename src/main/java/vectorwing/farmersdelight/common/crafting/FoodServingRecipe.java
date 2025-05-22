package vectorwing.farmersdelight.common.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public class FoodServingRecipe extends SpecialCraftingRecipe
{
	public FoodServingRecipe(CraftingRecipeCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World level) {
		ItemStack cookingPotStack = ItemStack.EMPTY;
		ItemStack containerStack = ItemStack.EMPTY;
		ItemStack secondStack = ItemStack.EMPTY;

		for (int index = 0; index < input.size(); ++index) {
			ItemStack selectedStack = input.getStackInSlot(index);
			if (!selectedStack.isEmpty()) {
				if (cookingPotStack.isEmpty()) {
					ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(selectedStack);
					if (!mealStack.isEmpty()) {
						cookingPotStack = selectedStack;
						containerStack = CookingPotBlockEntity.getContainerFromItem(selectedStack);
						continue;
					}
				}
				if (secondStack.isEmpty()) {
					secondStack = selectedStack;
				} else {
					return false;
				}
			}
		}

		return !cookingPotStack.isEmpty() && !secondStack.isEmpty() && secondStack.isOf(containerStack.getItem());
	}

	@Override
	public ItemStack assemble(CraftingRecipeInput input, RegistryWrapper.WrapperLookup access) {
		for (int i = 0; i < input.size(); ++i) {
			ItemStack selectedStack = input.getStackInSlot(i);
			if (!selectedStack.isEmpty() && selectedStack.isOf(ModItems.COOKING_POT.get())) {
				ItemStack resultStack = CookingPotBlockEntity.getMealFromItem(selectedStack).copy();
				resultStack.setCount(1);
				return resultStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput input) {
		DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < remainders.size(); ++i) {
			ItemStack selectedStack = input.getStackInSlot(i);
			if (selectedStack.getRecipeRemainder() != null) {
				remainders.set(i, selectedStack.getRecipeRemainder());
			} else if (selectedStack.isOf(ModItems.COOKING_POT.get())) {
				CookingPotBlockEntity.takeServingFromItem(selectedStack);
				ItemStack newCookingPotStack = selectedStack.copy();
				newCookingPotStack.setCount(1);
				remainders.set(i, newCookingPotStack);
				break;
			}
		}

		return remainders;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FOOD_SERVING.get();
	}
}
