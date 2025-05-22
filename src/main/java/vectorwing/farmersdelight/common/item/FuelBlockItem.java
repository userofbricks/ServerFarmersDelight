package vectorwing.farmersdelight.common.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import org.jetbrains.annotations.Nullable;

;

public class FuelBlockItem extends BlockItem
{
	public final int burnTime;

	public FuelBlockItem(Block block, net.minecraft.item.Item.Settings properties) {
		this(block, properties, 100);
	}

	public FuelBlockItem(Block block, net.minecraft.item.Item.Settings properties, int burnTime) {
		super(block, properties);
		this.burnTime = burnTime;
		FuelRegistry.INSTANCE.add(this, this.burnTime);
	}

	public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
		return this.burnTime;
	}
}
