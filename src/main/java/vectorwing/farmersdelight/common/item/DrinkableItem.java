package vectorwing.farmersdelight.common.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.World;
import net.minecraft.world.item.UseAnim;

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
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
		return 32;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		if (heldStack.contains(DataComponentTypes.FOOD)) {
			if (player.canConsume(heldStack.get(DataComponentTypes.FOOD).canAlwaysEat())) {
				player.setCurrentHand(hand);
				return InteractionResultHolder.consume(heldStack);
			} else {
				return InteractionResultHolder.fail(heldStack);
			}
		}
		return ItemUsage.consumeHeldItem(level, player, hand);
	}
}
