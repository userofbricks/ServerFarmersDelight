package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.common.block.entity.*;

import java.util.function.Supplier;
import net.minecraft.block.entity.BlockEntityType;

import static vectorwing.farmersdelight.refabricated.RegUtils.regBlockEntity;

public class ModBlockEntityTypes
{
	public static final Supplier<BlockEntityType<StoveBlockEntity>> STOVE = regBlockEntity("stove",
			() -> BlockEntityType.Builder.of(StoveBlockEntity::new, ModBlocks.STOVE.get()).build());
	public static final Supplier<BlockEntityType<CookingPotBlockEntity>> COOKING_POT = regBlockEntity("cooking_pot",
			() -> BlockEntityType.Builder.of(CookingPotBlockEntity::new, ModBlocks.COOKING_POT.get()).build());
	public static final Supplier<BlockEntityType<BasketBlockEntity>> BASKET = regBlockEntity("basket",
			() -> BlockEntityType.Builder.of(BasketBlockEntity::new, ModBlocks.BASKET.get()).build());
	public static final Supplier<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD = regBlockEntity("cutting_board",
			() -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new, ModBlocks.CUTTING_BOARD.get()).build());
	public static final Supplier<BlockEntityType<SkilletBlockEntity>> SKILLET = regBlockEntity("skillet",
			() -> BlockEntityType.Builder.of(SkilletBlockEntity::new, ModBlocks.SKILLET.get()).build());
	public static final Supplier<BlockEntityType<CabinetBlockEntity>> CABINET = regBlockEntity("cabinet",
			() -> BlockEntityType.Builder.of(CabinetBlockEntity::new,
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
							ModBlocks.WARPED_CABINET.get())
					.build());

	public static void touch() {

	}
}
