package vectorwing.farmersdelight.refabricated.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.input.RecipeInput;

/**
 * Refabricated: Wrapper for ItemStackHandler.
 */
public class RecipeWrapper implements RecipeInput {

    private final ItemHandler handler;
    private final RecipeMatcher stackedContents;
    private final int ingredientAmount;

    public RecipeWrapper(ItemHandler handler) {
        this.handler = handler;
        this.stackedContents = new RecipeMatcher();
        int ingredientAmount = 0;
        for (int value : handler.getInputSlotIndexes()) {
            ItemStack itemstack = handler.getStackInSlot(value);
            if (!itemstack.isEmpty()) {
                ++ingredientAmount;
                stackedContents.accountStack(itemstack, 1);
            }
        }
        this.ingredientAmount = ingredientAmount;
    }

    public RecipeMatcher stackedContents() {
        return stackedContents;
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
