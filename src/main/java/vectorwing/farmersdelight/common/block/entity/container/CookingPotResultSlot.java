package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemHandlerSlot;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

public class CookingPotResultSlot extends ItemHandlerSlot
{
	public final CookingPotBlockEntity tileEntity;
	private final PlayerEntity player;
	private int removeCount;

	public CookingPotResultSlot(PlayerEntity player, CookingPotBlockEntity tile, ItemStackHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.tileEntity = tile;
		this.player = player;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}

	@Override
	@NotNull
	public ItemStack takeStack(int amount) {
		if (this.hasStack()) {
			this.removeCount += Math.min(amount, this.getStack().getCount());
		}

		return super.takeStack(amount);
	}

	@Override
	public void onTakeItem(PlayerEntity thePlayer, ItemStack stack) {
		this.onCrafted(stack);
		super.onTakeItem(thePlayer, stack);
	}

	@Override
	protected void onCrafted(ItemStack stack, int amount) {
		this.removeCount += amount;
		this.onCrafted(stack);
	}

	@Override
	protected void onCrafted(ItemStack stack) {
		stack.onCraftByPlayer(this.player.getWorld(), this.player, this.removeCount);

		if (!this.player.getWorld().isClient) {
			tileEntity.unlockLastRecipe(this.player, tileEntity.getDroppableInventory());
		}

		this.removeCount = 0;
	}
}
