package vectorwing.farmersdelight.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import org.jetbrains.annotations.Nullable;

;

public class FuelItem extends Item
{
	public final int burnTime;

	public FuelItem(net.minecraft.item.Item.Settings properties) {
		this(properties, 100);
	}

	public FuelItem(net.minecraft.item.Item.Settings properties, int burnTime) {
		super(properties);
		this.burnTime = burnTime;
	}

	public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
		return this.burnTime;
	}
}
