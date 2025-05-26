package vectorwing.farmersdelight.common.event;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CommonModBusEvents
{
	public static void init() {
		DefaultItemComponentEvents.MODIFY.register(CommonModBusEvents::onModifyDefaultComponents);
	}

	public static void onModifyDefaultComponents(DefaultItemComponentEvents.ModifyContext context) {
		context.modify(item -> item.getRegistryEntry().isIn(ModTags.MAX_STACK_SIZE_16), (builder, item) -> builder.add(DataComponentTypes.MAX_STACK_SIZE, 16));
		context.modify(Items.RABBIT_STEW, (builder) -> builder.add(DataComponentTypes.FOOD, FoodValues.RABBIT_STEW_BUFF).add(DataComponentTypes.CONSUMABLE, FoodValues.RABBIT_STEW_BUFF_CONSUMABLE));
	}
}
