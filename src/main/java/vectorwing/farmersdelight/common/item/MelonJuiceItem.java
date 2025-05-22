package vectorwing.farmersdelight.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MelonJuiceItem extends DrinkableItem
{
	public MelonJuiceItem(net.minecraft.item.Item.Settings properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, World level, LivingEntity consumer) {
		consumer.heal(2.0F);
	}
}
