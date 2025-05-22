package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.refabricated.inventory.ItemHandlerSlot;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

public class CookingPotMealSlot extends ItemHandlerSlot
{
	public CookingPotMealSlot(ItemStackHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerIn) {
		return false;
	}
}
