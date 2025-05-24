package vectorwing.farmersdelight.refabricated.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.RecipeInput;

/**
 * Refabricated: Wrapper for ItemStackHandler.
 */
public class RecipeWrapper implements RecipeInput {
    private final ItemHandler handler;
    private final RecipeFinder matcher = new RecipeFinder();
    private final int ingredientAmount;

    public RecipeWrapper(ItemHandler handler) {
        this.handler = handler;
        int ingredientAmount = 0;
        for (int value : handler.getInputSlotIndexes()) {
            ItemStack itemstack = handler.getStackInSlot(value);
            if (!itemstack.isEmpty()) {
                ++ingredientAmount;
                this.matcher.addInput(itemstack, 1);
            }
        }
        this.ingredientAmount = ingredientAmount;
    }

    public RecipeFinder getRecipeMatcher() {
        return this.matcher;
    }

    public int ingredientAmount() {
        return ingredientAmount;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public int size() {
        return handler.getSlotCount();
    }
}
