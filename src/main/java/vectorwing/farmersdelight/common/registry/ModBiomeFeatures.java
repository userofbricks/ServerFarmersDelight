package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.feature.WildCropFeature;
import vectorwing.farmersdelight.common.world.feature.WildRiceFeature;

import java.util.function.Supplier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;

import static vectorwing.farmersdelight.refabricated.RegUtils.regFeature;

public class ModBiomeFeatures {
    public static final Supplier<Feature<RandomPatchFeatureConfig>> WILD_RICE = regFeature("wild_rice", () -> new WildRiceFeature(RandomPatchFeatureConfig.CODEC));
    public static final Supplier<Feature<WildCropConfiguration>> WILD_CROP = regFeature("wild_crop", () -> new WildCropFeature(WildCropConfiguration.CODEC));

    public static void touch() {
    }
}
