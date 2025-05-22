package vectorwing.farmersdelight.common.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

import java.util.function.Supplier;

import static vectorwing.farmersdelight.refabricated.RegUtils.regMenu;

public class ModMenuTypes {
    public static final Supplier<ScreenHandlerType<CookingPotMenu>> COOKING_POT = regMenu("cooking_pot", () -> new ExtendedScreenHandlerType<>(CookingPotMenu::new, BlockPos.PACKET_CODEC));

    public static void touch() {

    }
}
