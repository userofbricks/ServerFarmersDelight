package vectorwing.farmersdelight.common.item;

import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.math.MathHelper;

public class CookingPotItem extends BlockItem
{
	private static final int BAR_COLOR = MathHelper.hsvToRgb(0.4F, 0.4F, 1.0F);

	public CookingPotItem(Block block, net.minecraft.item.Item.Settings properties) {
		super(block, properties);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getServingCount(stack) > 0;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + 12 * getServingCount(stack) / 64, 13);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(stack);
		return Optional.of(new CookingPotTooltip.CookingPotTooltipComponent(mealStack));
	}

	private static int getServingCount(ItemStack stack) {
		ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(stack);
		return mealStack.getCount();
	}
}
