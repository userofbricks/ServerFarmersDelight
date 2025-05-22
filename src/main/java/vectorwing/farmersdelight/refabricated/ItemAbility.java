package vectorwing.farmersdelight.refabricated;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ItemAbility implements StringIdentifiable {
    //just contains stuff fd has. Could have been hardcoded, Hoping that keeping like this will make it easier to merge
    SWORD_DIG, SHOVEL_DIG, PICKAXE_DIG,
    SHEARS_CARVE, SHEARS_DIG,
    AXE_DIG, AXE_STRIP; //just add needed ones, same names as neo so we can keep the recipe as is

    @Override
    public String asString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static final Codec<ItemAbility> CODEC = StringIdentifiable.createCodec(ItemAbility::values);

    public boolean canPerformAction(@NotNull ItemStack stack) {
        //item ability -> tag

        return switch (this) {
            case SHEARS_CARVE, SHEARS_DIG -> stack.isIn(ConventionalItemTags.SHEAR_TOOLS);
            case SWORD_DIG -> stack.isIn(ItemTags.SWORDS);
            case SHOVEL_DIG -> stack.isIn(ItemTags.SHOVELS);
            case PICKAXE_DIG -> stack.isIn(ItemTags.PICKAXES);
            case AXE_DIG, AXE_STRIP -> stack.isIn(ItemTags.AXES);
        };
    }
}
