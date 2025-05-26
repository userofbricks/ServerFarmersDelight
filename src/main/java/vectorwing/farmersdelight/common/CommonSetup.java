package vectorwing.farmersdelight.common;

import net.minecraft.entity.passive.VillagerEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

import net.minecraft.block.DispenserBlock;

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
		VillagerEntity.ITEM_FOOD_VALUES.put(ModItems.CABBAGE.get(), 1);
		VillagerEntity.ITEM_FOOD_VALUES.put(ModItems.TOMATO.get(), 1);
		VillagerEntity.ITEM_FOOD_VALUES.put(ModItems.ONION.get(), 1);
		VillagerEntity.ITEM_FOOD_VALUES.put(ModItems.RICE.get(), 1);
	}
}
