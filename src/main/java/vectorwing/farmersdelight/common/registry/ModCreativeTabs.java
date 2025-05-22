package vectorwing.farmersdelight.common.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

import static vectorwing.farmersdelight.refabricated.RegUtils.regTab;

public class ModCreativeTabs {
    public static final Supplier<ItemGroup> TAB_FARMERS_DELIGHT = regTab(FarmersDelight.MODID,
            () -> FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.farmersdelight"))
                    .icon(() -> new ItemStack(ModBlocks.STOVE.get()))
                    .entries((parameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.add(item.get())))
                    .build());

    public static void touch() {

    }
}
