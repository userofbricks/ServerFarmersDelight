package vectorwing.farmersdelight.common.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import vectorwing.farmersdelight.FarmersDelight;

@SuppressWarnings("unused")
public class WildCropGeneration
{
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_SANDY_SHRUB = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_sandy_shrub"));

	// Those are unused, but kept for reference just in case
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CABBAGES = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_cabbages"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_ONIONS = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_onions"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_TOMATOES = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_tomatoes"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CARROTS = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_carrots"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_POTATOES = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_potatoes"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_BEETROOTS = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_beetroots"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_RICE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_rice"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_brown_mushroom_colony"));
	public static RegistryKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_red_mushroom_colony"));

	public static RegistryKey<PlacedFeature> PATCH_WILD_CABBAGES = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_cabbages"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_ONIONS = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_onions"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_TOMATOES = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_tomatoes"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_CARROTS = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_carrots"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_POTATOES = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_potatoes"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_BEETROOTS = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_beetroots"));
	public static RegistryKey<PlacedFeature> PATCH_WILD_RICE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_wild_rice"));
	public static RegistryKey<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_brown_mushroom_colony"));
	public static RegistryKey<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES = RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(FarmersDelight.MODID, "patch_red_mushroom_colony"));

	public static void load() {
	}
}
