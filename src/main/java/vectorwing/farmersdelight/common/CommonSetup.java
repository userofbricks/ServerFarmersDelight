package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Set;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Item;

public class CommonSetup
{
	public static void init() {
		registerDispenserBehaviors();
		registerItemSetAdditions();
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerProjectileBehavior(ModItems.ROTTEN_TOMATO.get());
	}

	public static void registerItemSetAdditions() {
		Set<Item> newWantedItems = Sets.newHashSet(
				ModItems.CABBAGE.get(),
				ModItems.TOMATO.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get(),
				ModItems.CABBAGE_SEEDS.get(),
				ModItems.TOMATO_SEEDS.get(),
				ModItems.RICE_PANICLE.get());
		newWantedItems.addAll(Villager.WANTED_ITEMS);
		Villager.WANTED_ITEMS = ImmutableSet.copyOf(newWantedItems);
	}
}
