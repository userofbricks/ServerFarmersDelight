package vectorwing.farmersdelight.common.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Predicate;

public class ModBiomeModifiers {

    private static final RegistryKey<PlacedFeature> BROWN_COLONY = modFeature("patch_brown_mushroom_colony");
    private static final RegistryKey<PlacedFeature> RED_COLONY = modFeature("patch_red_mushroom_colony");
    private static final RegistryKey<PlacedFeature> WILD_CABBAGE = modFeature("patch_wild_cabbages");
    private static final RegistryKey<PlacedFeature> WILD_BEETROOT = modFeature("patch_wild_beetroots");
    private static final RegistryKey<PlacedFeature> WILD_CARROTS = modFeature("patch_wild_carrots");
    private static final RegistryKey<PlacedFeature> WILD_ONIONS = modFeature("patch_wild_onions");
    private static final RegistryKey<PlacedFeature> WILD_TOMATOES = modFeature("patch_wild_tomatoes");
    private static final RegistryKey<PlacedFeature> WILD_POTATOES = modFeature("patch_wild_potatoes");
    private static final RegistryKey<PlacedFeature> WILD_RICE = modFeature("patch_wild_rice");

    @NotNull
    private static RegistryKey<PlacedFeature> modFeature(String red_colony) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, FarmersDelight.res(red_colony));
    }

    public static void init() {
        BiomeModifications.addFeature(new FDBiomeSelector(ModTags.HAS_BROWN_MUSHROOM_COLONY),
                GenerationStep.Feature.VEGETAL_DECORATION, BROWN_COLONY);
        BiomeModifications.addFeature(new FDBiomeSelector(ModTags.HAS_RED_MUSHROOM_COLONY),
                GenerationStep.Feature.VEGETAL_DECORATION, RED_COLONY);
        BiomeModifications.addFeature(new FDBiomeSelector(ModTags.HAS_WILD_CABBAGE),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_CABBAGE);

        BiomeModifications.addFeature(new FDBiomeSelector(ModTags.HAS_WILD_BEETROOTS),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_BEETROOT);

        BiomeModifications.addFeature(new FDBiomeSelector(0.4f, 0.9f,
                        ModTags.WILD_CARROTS_WHITELIST, ModTags.WILD_CARROTS_BLACKLIST),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_CARROTS);

        BiomeModifications.addFeature(new FDBiomeSelector(0.4f, 0.9f,
                        ModTags.WILD_ONIONS_WHITELIST, ModTags.WILD_ONIONS_BLACKLIST),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_ONIONS);

        BiomeModifications.addFeature(new FDBiomeSelector(0.1f, 0.3f,
                        ModTags.WILD_POTATOES_WHITELIST, ModTags.WILD_POTATOES_BLACKLIST),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_POTATOES);

        BiomeModifications.addFeature(new FDBiomeSelector(-4, 4,
                        ModTags.WILD_RICE_WHITELIST, ModTags.WILD_RICE_BLACKLIST),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_RICE);

        BiomeModifications.addFeature(new FDBiomeSelector(-4f, 4f,
                        ModTags.WILD_TOMATOES_WHITELIST, ModTags.WILD_TOMATOES_BLACKLIST),
                GenerationStep.Feature.VEGETAL_DECORATION, WILD_TOMATOES);
    }

    //TODO: use humidity too? for rice
    public record FDBiomeSelector(float minTemperature, float maxTemperature, TagKey<Biome> allowed,
                                  @Nullable TagKey<Biome> denied) implements Predicate<BiomeSelectionContext> {

        public FDBiomeSelector(TagKey<Biome> tagKey) {
            this(-4f, 4f, tagKey, null);
        }

        @Override
        public boolean test(BiomeSelectionContext context) {
            RegistryEntry<Biome> biome = context.getBiomeRegistryEntry();
            float temp = biome.value().getTemperature();
            if (denied != null && biome.isIn(denied)) return false;
            return biome.isIn(allowed) && temp >= minTemperature && temp <= maxTemperature;
        }
    }
}