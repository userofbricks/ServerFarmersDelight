package vectorwing.farmersdelight.common.utility;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.refabricated.inventory.ItemHandler;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

/**
 * Util for handling ItemStacks and inventories containing them.
 */
public class ItemUtils
{
	public static void dropItems(World level, BlockPos pos, ItemStackHandler inventory) {
		for (int slot = 0; slot < inventory.getSlotCount(); slot++)
			ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(),
					inventory.removeItem(slot));
	}

	public static boolean isInventoryEmpty(ItemHandler inventory) {
		for (int i = 0; i < inventory.getSlotCount(); ++i) {
			if (!inventory.getStackInSlot(i).isEmpty())
				return false;
		}
		return true;
	}

	public static void spawnItemEntity(World level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
		ItemEntity entity = new ItemEntity(level, x, y, z, stack);
		entity.setVelocity(xMotion, yMotion, zMotion);
		level.spawnEntity(entity);
	}
}
