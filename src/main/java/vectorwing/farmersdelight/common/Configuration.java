package vectorwing.farmersdelight.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.refabricated.mlconfigs.ConfigBuilder;
import vectorwing.farmersdelight.refabricated.mlconfigs.ConfigType;
import vectorwing.farmersdelight.refabricated.mlconfigs.ModConfigHolder;

import java.util.function.Supplier;

public class Configuration {
    public static ModConfigHolder COMMON_CONFIG;

    // COMMON
    public static final String CATEGORY_WORLD = "world";
    public static Supplier<Boolean> GENERATE_FD_CHEST_LOOT;
    public static Supplier<Boolean> GENERATE_VILLAGE_COMPOST_HEAPS;
    public static Supplier<Boolean> GENERATE_VILLAGE_FARM_FD_CROPS;
    public static Supplier<Integer> CHANCE_WILD_CABBAGES;
    public static Supplier<Integer> CHANCE_WILD_BEETROOTS;
    public static Supplier<Integer> CHANCE_WILD_POTATOES;
    public static Supplier<Integer> CHANCE_WILD_ONIONS;
    public static Supplier<Integer> CHANCE_WILD_CARROTS;
    public static Supplier<Integer> CHANCE_WILD_TOMATOES;
    public static Supplier<Integer> CHANCE_WILD_RICE;
    public static Supplier<Boolean> GENERATE_BROWN_MUSHROOM_COLONIES;
    public static Supplier<Integer> CHANCE_BROWN_MUSHROOM_COLONIES;
    public static Supplier<Boolean> GENERATE_RED_MUSHROOM_COLONIES;
    public static Supplier<Integer> CHANCE_RED_MUSHROOM_COLONIES;

    static {
        ConfigBuilder builder = ConfigBuilder.create(FarmersDelight.MODID, ConfigType.COMMON);

        builder.comment("World generation").push(CATEGORY_WORLD);
        GENERATE_FD_CHEST_LOOT = builder.comment("Should this mod add some of its items (ropes, seeds, knives, meals etc.) as extra chest loot across Minecraft?")
                .define("generateFDChestLoot", true);
        GENERATE_VILLAGE_COMPOST_HEAPS = builder.comment("Should FD generate Compost Heaps across all village biomes?")
                .define("genVillageCompostHeaps", true);
        GENERATE_VILLAGE_FARM_FD_CROPS = builder.comment("Should FD crops show up planted randomly in various village farms?")
                .define("genFDCropsOnVillageFarms", true);

        builder.comment("Wild Cabbage generation").push("wild_cabbages");
        CHANCE_WILD_CABBAGES = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 30, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Sea Beet generation").push("wild_beetroots");
        CHANCE_WILD_BEETROOTS = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 30, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Wild Potato generation").push("wild_potatoes");
        CHANCE_WILD_POTATOES = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 100, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Wild Carrot generation").push("wild_carrots");
        CHANCE_WILD_CARROTS = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 120, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Wild Onion generation").push("wild_onions");
        CHANCE_WILD_ONIONS = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 120, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Tomato Vines generation").push("wild_tomatoes");
        CHANCE_WILD_TOMATOES = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 100, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Wild Rice generation").push("wild_rice");
        CHANCE_WILD_RICE = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 20, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Brown Mushroom Colony generation").push("brown_mushroom_colonies");
        GENERATE_BROWN_MUSHROOM_COLONIES = builder.comment("Generate brown mushroom colonies on mushroom fields")
                .define("genBrownMushroomColony", true);
        CHANCE_BROWN_MUSHROOM_COLONIES = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 15, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Red Mushroom Colony generation").push("red_mushroom_colonies");
        GENERATE_RED_MUSHROOM_COLONIES = builder.comment("Generate red mushroom colonies on mushroom fields")
                .define("genRedMushroomColony", true);
        CHANCE_RED_MUSHROOM_COLONIES = builder.comment("Chance of generating clusters. Smaller value = more frequent.")
                .define("chance", 15, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.pop();

        COMMON_CONFIG = builder.build();
        COMMON_CONFIG.forceLoad(); //need for data component event that's fired very early
    }

    public static void touch() {

    }
}
