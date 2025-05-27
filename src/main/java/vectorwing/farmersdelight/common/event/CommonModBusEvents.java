package vectorwing.farmersdelight.common.event;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import vectorwing.farmersdelight.common.FoodValues;

import java.util.List;

public class CommonModBusEvents
{
	public static void init() {
		DefaultItemComponentEvents.MODIFY.register(CommonModBusEvents::onModifyDefaultComponents);
	}

	public static void onModifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context) {
		for (Item itemTest: List.of(Items.RABBIT_STEW, Items.MUSHROOM_STEW, Items.BEETROOT_SOUP, Items.POTION, Items.LINGERING_POTION, Items.SPLASH_POTION)) {
			context.modify(item -> item == itemTest, (builder, item) -> builder.add(DataComponentTypes.MAX_STACK_SIZE, 16));
		}
		context.modify(Items.RABBIT_STEW, (builder) -> builder.add(DataComponentTypes.FOOD, FoodValues.RABBIT_STEW_BUFF).add(DataComponentTypes.CONSUMABLE, FoodValues.RABBIT_STEW_BUFF_CONSUMABLE));
	}
}
