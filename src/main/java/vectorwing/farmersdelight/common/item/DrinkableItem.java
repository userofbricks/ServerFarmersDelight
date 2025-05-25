package vectorwing.farmersdelight.common.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DrinkableItem extends ConsumableItem
{
	public DrinkableItem(net.minecraft.item.Item.Settings properties) {
		super(properties);
	}

	public DrinkableItem(net.minecraft.item.Item.Settings properties, boolean hasFoodEffectTooltip) {
		super(properties, hasFoodEffectTooltip);
	}

	public DrinkableItem(net.minecraft.item.Item.Settings properties, boolean hasPotionEffectTooltip, boolean hasCustomTooltip) {
		super(properties, hasPotionEffectTooltip, hasCustomTooltip);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
		return 32;
	}

	@Override
	public ActionResult use(World level, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (heldStack.contains(DataComponentTypes.FOOD)) {
			if (player.canConsume(heldStack.get(DataComponentTypes.FOOD).canAlwaysEat())) {
				player.setCurrentHand(hand);
				return ActionResult.CONSUME;
			} else {
				return ActionResult.FAIL;
			}
		}
		return ItemUsage.consumeHeldItem(level, player, hand);
	}
}
