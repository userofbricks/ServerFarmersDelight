package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.common.loot.function.CopySkilletFunction;
import vectorwing.farmersdelight.common.loot.function.SmokerCookFunction;

import java.util.function.Supplier;
import net.minecraft.loot.function.LootFunctionType;

import static vectorwing.farmersdelight.refabricated.RegUtils.regLootFunc;

public class ModLootFunctions
{
	public static final Supplier<LootFunctionType<CopySkilletFunction>> COPY_SKILLET = regLootFunc("copy_skillet", () -> new LootFunctionType<>(CopySkilletFunction.CODEC));
	public static final Supplier<LootFunctionType<SmokerCookFunction>> SMOKER_COOK = regLootFunc("smoker_cook", () -> new LootFunctionType<>(SmokerCookFunction.CODEC));

	public static void touch() {

	}
}
