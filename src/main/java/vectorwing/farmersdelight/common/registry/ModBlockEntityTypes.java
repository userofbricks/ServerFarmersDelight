package vectorwing.farmersdelight.common.registry;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Util;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.*;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.block.entity.BlockEntityType;

import static vectorwing.farmersdelight.refabricated.RegUtils.regBlockEntity;

public class ModBlockEntityTypes
{
	public static final BlockEntityType<StoveBlockEntity> STOVE = regBlockEntity("stove", StoveBlockEntity::new, ModBlocks.STOVE.get());
	public static final BlockEntityType<CookingPotBlockEntity> COOKING_POT = regBlockEntity("cooking_pot", CookingPotBlockEntity::new, ModBlocks.COOKING_POT.get());
	public static final BlockEntityType<CuttingBoardBlockEntity> CUTTING_BOARD = regBlockEntity("cutting_board", CuttingBoardBlockEntity::new, ModBlocks.CUTTING_BOARD.get());
	public static final BlockEntityType<SkilletBlockEntity> SKILLET = regBlockEntity("skillet", SkilletBlockEntity::new, ModBlocks.SKILLET.get());
	public static final BlockEntityType<CabinetBlockEntity> CABINET = regBlockEntity("cabinet", CabinetBlockEntity::new,
							ModBlocks.OAK_CABINET.get(),
							ModBlocks.BIRCH_CABINET.get(),
							ModBlocks.SPRUCE_CABINET.get(),
							ModBlocks.JUNGLE_CABINET.get(),
							ModBlocks.ACACIA_CABINET.get(),
							ModBlocks.DARK_OAK_CABINET.get(),
							ModBlocks.MANGROVE_CABINET.get(),
							ModBlocks.BAMBOO_CABINET.get(),
							ModBlocks.CHERRY_CABINET.get(),
							ModBlocks.CRIMSON_CABINET.get(),
							ModBlocks.WARPED_CABINET.get()
	);

	public static void touch() {

	}
}
