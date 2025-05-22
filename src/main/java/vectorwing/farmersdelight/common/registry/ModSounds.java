package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import static vectorwing.farmersdelight.refabricated.RegUtils.regSound;

public class ModSounds
{
	// Stove
	public static final Supplier<SoundEvent> BLOCK_STOVE_CRACKLE = regSound("block.stove.crackle",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.stove.crackle")));

	// Cooking Pot
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL = regSound("block.cooking_pot.boil",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.cooking_pot.boil")));
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = regSound("block.cooking_pot.boil_soup",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.cooking_pot.boil_soup")));

	// Cutting Board
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = regSound("block.cutting_board.knife",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.cutting_board.knife")));

	// Cabinet
	public static final Supplier<SoundEvent> BLOCK_CABINET_OPEN = regSound("block.cabinet.open",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.cabinet.open")));
	public static final Supplier<SoundEvent> BLOCK_CABINET_CLOSE = regSound("block.cabinet.close",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.cabinet.close")));

	// Skillet
	public static final Supplier<SoundEvent> BLOCK_SKILLET_SIZZLE = regSound("block.skillet.sizzle",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.skillet.sizzle")));
	public static final Supplier<SoundEvent> BLOCK_SKILLET_ADD_FOOD = regSound("block.skillet.add_food",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.skillet.add_food")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_STRONG = regSound("item.skillet.attack.strong",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "item.skillet.attack.strong")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_WEAK = regSound("item.skillet.attack.weak",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "item.skillet.attack.weak")));

	// Tomato Bush
	public static final Supplier<SoundEvent> ITEM_TOMATO_PICK_FROM_BUSH = regSound("block.tomato_bush.pick_tomatoes",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "block.tomato_bush.pick_tomatoes")));

	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_THROW = regSound("entity.rotten_tomato.throw",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "entity.rotten_tomato.throw")));
	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_HIT = regSound("entity.rotten_tomato.hit",
			() -> SoundEvent.of(Identifier.of(FarmersDelight.MODID, "entity.rotten_tomato.hit")));

	public static void touch() {

	}
}
