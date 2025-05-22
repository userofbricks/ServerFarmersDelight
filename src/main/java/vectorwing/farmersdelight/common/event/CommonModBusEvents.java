package vectorwing.farmersdelight.common.event;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

public class CommonModBusEvents
{
	public static void init() {
		DefaultItemComponentEvents.MODIFY.register(CommonModBusEvents::onModifyDefaultComponents);
	}

	public static void onModifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context) {
		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
			Configuration.SOUP_ITEM_LIST.get().forEach((key) -> {
				Item item = Registries.ITEM.getEntry(Identifier.of(key));
				context.modify(item, (builder) -> builder.add(DataComponentTypes.MAX_STACK_SIZE, 16));
			});
		}
		if (Configuration.RABBIT_STEW_BUFF.get()) {
			context.modify(Items.RABBIT_STEW, (builder) -> builder.add(DataComponentTypes.FOOD, FoodValues.RABBIT_STEW_BUFF));
		}
	}
}
