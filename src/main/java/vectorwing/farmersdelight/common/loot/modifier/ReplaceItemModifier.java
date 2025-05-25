package vectorwing.farmersdelight.common.loot.modifier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.refabricated.LootModifier;

public class ReplaceItemModifier extends LootModifier
{

	private final Item removedItem;
	private final Item addedItem;
	private final int addedCount;

	/**
	 * This loot modifier removes all instances of the specified item, replacing it by another specified addition.
	 */
	public ReplaceItemModifier(LootCondition[] conditions, Item removedItem, Item addedItem, int addedCount) {
		super(conditions);
		this.removedItem = removedItem;
		this.addedItem = addedItem;
		this.addedCount = addedCount;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
		ItemStack addedStack = new ItemStack(addedItem, addedCount);

		generatedLoot.forEach((item) -> {
			if (item.isOf(removedItem)) {
				generatedLoot.remove(item);
			}
		});

		if (addedStack.getCount() < addedStack.getMaxCount()) {
			generatedLoot.add(addedStack);
		} else {
			int i = addedStack.getCount();

			while (i > 0) {
				ItemStack subStack = addedStack.copy();
				subStack.setCount(Math.min(addedStack.getMaxCount(), i));
				i -= subStack.getCount();
				generatedLoot.add(subStack);
			}
		}

		return generatedLoot;
	}

}
