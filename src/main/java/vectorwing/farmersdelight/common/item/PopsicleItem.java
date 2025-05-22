package vectorwing.farmersdelight.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PopsicleItem extends ConsumableItem
{
	public PopsicleItem(net.minecraft.item.Item.Settings properties) {
		super(properties);
	}

	@Override
	public void affectConsumer(ItemStack stack, World level, LivingEntity consumer) {
		consumer.extinguish();
	}
}
