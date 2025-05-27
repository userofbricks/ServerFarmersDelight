package vectorwing.farmersdelight.refabricated;

import net.minecraft.block.Blocks;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

public class TagUtils {
    // Vanilla loads tags after Loot Tables are loaded, so we need to do something about that.
    private static Collection<RegistryEntry<Block>> earlyDropsCakeTag;
    private static Collection<RegistryEntry<EntityType<?>>> earlyDropsLeatherTag;

    // This exists so we don't modify literally every loot table in the game just to add loot to a few
    public static boolean isCandleDropsCakeSliceTag(RegistryEntry<Block> block, RegistryWrapper<Block> lookup) {
        if (earlyDropsCakeTag == null) {
            earlyDropsCakeTag = List.of(Blocks.CANDLE_CAKE.getRegistryEntry(),
                    Blocks.WHITE_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.ORANGE_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.MAGENTA_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.LIGHT_BLUE_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.YELLOW_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.LIME_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.PINK_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.GRAY_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.LIGHT_GRAY_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.CYAN_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.PURPLE_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.BLUE_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.BROWN_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.GREEN_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.RED_CANDLE_CAKE.getRegistryEntry(),
                    Blocks.BLACK_CANDLE_CAKE.getRegistryEntry());
            if (earlyDropsCakeTag == null)
                earlyDropsCakeTag = List.of();
        }

        return earlyDropsCakeTag.contains(block);
    }

    // This exists so we don't modify literally every loot table in the game just to add loot to a few
    public static boolean isDropsLeatherTag(RegistryEntry<EntityType<?>> entityType, RegistryWrapper<EntityType<?>> lookup) {
        if (earlyDropsLeatherTag == null) {
            earlyDropsLeatherTag = List.of(EntityType.COW.getRegistryEntry(),
                    EntityType.DONKEY.getRegistryEntry(),
                    EntityType.HORSE.getRegistryEntry(),
                    EntityType.LLAMA.getRegistryEntry(),
                    EntityType.MOOSHROOM.getRegistryEntry(),
                    EntityType.TRADER_LLAMA.getRegistryEntry());
            if (earlyDropsLeatherTag == null)
                earlyDropsLeatherTag = List.of();
        }

        return earlyDropsLeatherTag.contains(entityType);
    }
}