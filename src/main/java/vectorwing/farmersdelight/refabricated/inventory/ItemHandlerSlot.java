package vectorwing.farmersdelight.refabricated.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ItemHandlerSlot extends Slot {
    private static final Inventory EMPTY_INVENTORY = new SimpleInventory(0);
    private final ItemHandler itemHandler;

    public ItemHandlerSlot(ItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(EMPTY_INVENTORY, index, xPosition, yPosition);
        this.itemHandler = inventoryIn;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return !stack.isEmpty() && itemHandler.isItemValid(getIndex(), stack);
    }

    @Override
    public ItemStack getStack() {
        return itemHandler.getStackInSlot(getIndex());
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        itemHandler.setStackInSlot(getIndex(), stack);
        markDirty();
    }

    @Override
    public ItemStack takeStack(int amount) {
        ItemStack stack = itemHandler.removeItem(getIndex(), amount);
        markDirty();
        return stack;
    }

    @Override
    public int getMaxItemCount() {
        return itemHandler.getSlotLimit(getIndex());
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public void markDirty() {
        itemHandler.commitModifiedStacks();
    }
}
