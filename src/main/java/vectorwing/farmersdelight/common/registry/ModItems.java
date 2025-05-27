package vectorwing.farmersdelight.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.*;

import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static vectorwing.farmersdelight.FarmersDelight.res;

@SuppressWarnings("unused")
public class ModItems
{
	public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

	public static Supplier<Item> registerWithTab(final String name, final Function<Item.Settings, Item> supplier, Item.Settings settings) {
		Item item = regItem(name, supplier, settings);
		Supplier<Item> block = () -> item;
		CREATIVE_TAB_ITEMS.add(block);
		return block;
	}
	public static Item regItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, res(name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM, itemKey, item);

		return item;
	}

	// Helper methods
	public static Item.Settings basicItem() {
		return new Item.Settings();
	}

	public static Item.Settings foodItem(FoodComponent food) {
		return new Item.Settings().food(food);
	}

	public static Item.Settings bowlFoodItem(FoodComponent food) {
		return new Item.Settings().food(food).recipeRemainder(Items.BOWL).maxCount(16);
	}

	public static Item.Settings drinkItem() {
		return new Item.Settings().recipeRemainder(Items.GLASS_BOTTLE).maxCount(16);
	}

	// Blocks
	public static final Supplier<Item> STOVE = registerWithTab("stove", (s) -> new BlockItem(ModBlocks.STOVE.get(), s), basicItem());
	public static final Supplier<Item> COOKING_POT = registerWithTab("cooking_pot", (s) -> new CookingPotItem(ModBlocks.COOKING_POT.get(), s), basicItem().maxCount(1));
	public static final Supplier<Item> SKILLET = registerWithTab("skillet", (s) -> new SkilletItem(ModBlocks.SKILLET.get(), s), basicItem().maxCount(1).attributeModifiers(SkilletItem.createAttributes(SkilletItem.SKILLET_TIER, 5.0F, -3.1F)));
	public static final Supplier<Item> CUTTING_BOARD = registerWithTab("cutting_board", (s) -> new FuelBlockItem(ModBlocks.CUTTING_BOARD.get(), s, 200), basicItem());

	public static final Supplier<Item> CARROT_CRATE = registerWithTab("carrot_crate", (s) -> new BlockItem(ModBlocks.CARROT_CRATE.get(), s), basicItem());
	public static final Supplier<Item> POTATO_CRATE = registerWithTab("potato_crate", (s) -> new BlockItem(ModBlocks.POTATO_CRATE.get(), s), basicItem());
	public static final Supplier<Item> BEETROOT_CRATE = registerWithTab("beetroot_crate",(s) -> new BlockItem(ModBlocks.BEETROOT_CRATE.get(), s), basicItem());
	public static final Supplier<Item> CABBAGE_CRATE = registerWithTab("cabbage_crate",(s) -> new BlockItem(ModBlocks.CABBAGE_CRATE.get(), s), basicItem());
	public static final Supplier<Item> TOMATO_CRATE = registerWithTab("tomato_crate", (s) -> new BlockItem(ModBlocks.TOMATO_CRATE.get(), s), basicItem());
	public static final Supplier<Item> ONION_CRATE = registerWithTab("onion_crate", (s) -> new BlockItem(ModBlocks.ONION_CRATE.get(), s), basicItem());
	public static final Supplier<Item> RICE_BALE = registerWithTab("rice_bale", (s) -> new BlockItem(ModBlocks.RICE_BALE.get(), s), basicItem());
	public static final Supplier<Item> RICE_BAG = registerWithTab("rice_bag", (s) -> new BlockItem(ModBlocks.RICE_BAG.get(), s), basicItem());
	public static final Supplier<Item> STRAW_BALE = registerWithTab("straw_bale", (s) -> new BlockItem(ModBlocks.STRAW_BALE.get(), s), basicItem());

	public static final Supplier<Item> SAFETY_NET = registerWithTab("safety_net", (s) -> new FuelBlockItem(ModBlocks.SAFETY_NET.get(),s, 200), basicItem());
	public static final Supplier<Item> OAK_CABINET = registerWithTab("oak_cabinet", (s) -> new FuelBlockItem(ModBlocks.OAK_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> SPRUCE_CABINET = registerWithTab("spruce_cabinet", (s) -> new FuelBlockItem(ModBlocks.SPRUCE_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> BIRCH_CABINET = registerWithTab("birch_cabinet", (s) -> new FuelBlockItem(ModBlocks.BIRCH_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> JUNGLE_CABINET = registerWithTab("jungle_cabinet", (s) -> new FuelBlockItem(ModBlocks.JUNGLE_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> ACACIA_CABINET = registerWithTab("acacia_cabinet", (s) -> new FuelBlockItem(ModBlocks.ACACIA_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> DARK_OAK_CABINET = registerWithTab("dark_oak_cabinet", (s) -> new FuelBlockItem(ModBlocks.DARK_OAK_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> MANGROVE_CABINET = registerWithTab("mangrove_cabinet", (s) -> new FuelBlockItem(ModBlocks.MANGROVE_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> CHERRY_CABINET = registerWithTab("cherry_cabinet", (s) -> new FuelBlockItem(ModBlocks.CHERRY_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> BAMBOO_CABINET = registerWithTab("bamboo_cabinet", (s) -> new FuelBlockItem(ModBlocks.BAMBOO_CABINET.get(),s, 300), basicItem());
	public static final Supplier<Item> CRIMSON_CABINET = registerWithTab("crimson_cabinet", (s) -> new BlockItem(ModBlocks.CRIMSON_CABINET.get(), s), basicItem());
	public static final Supplier<Item> WARPED_CABINET = registerWithTab("warped_cabinet", (s) -> new BlockItem(ModBlocks.WARPED_CABINET.get(), s), basicItem());
	public static final Supplier<Item> TATAMI = registerWithTab("tatami", (s) -> new FuelBlockItem(ModBlocks.TATAMI.get(),s, 400), basicItem());
	public static final Supplier<Item> FULL_TATAMI_MAT = registerWithTab("full_tatami_mat", (s) -> new FuelBlockItem(ModBlocks.FULL_TATAMI_MAT.get(),s, 200), basicItem());
	public static final Supplier<Item> HALF_TATAMI_MAT = registerWithTab("half_tatami_mat", (s) -> new FuelBlockItem(ModBlocks.HALF_TATAMI_MAT.get(), s), basicItem());
	public static final Supplier<Item> CANVAS_RUG = registerWithTab("canvas_rug", (s) -> new FuelBlockItem(ModBlocks.CANVAS_RUG.get(),s, 200), basicItem());
	public static final Supplier<Item> ORGANIC_COMPOST = registerWithTab("organic_compost", (s) -> new BlockItem(ModBlocks.ORGANIC_COMPOST.get(), s), basicItem());
	public static final Supplier<Item> RICH_SOIL = registerWithTab("rich_soil", (s) -> new BlockItem(ModBlocks.RICH_SOIL.get(), s), basicItem());
	public static final Supplier<Item> RICH_SOIL_FARMLAND = registerWithTab("rich_soil_farmland", (s) -> new BlockItem(ModBlocks.RICH_SOIL_FARMLAND.get(), s), basicItem());
	public static final Supplier<Item> ROPE = registerWithTab("rope", (s) -> new RopeItem(ModBlocks.ROPE.get(), s), basicItem());

	// Tools
	public static final Supplier<Item> FLINT_KNIFE = registerWithTab("flint_knife", (s) -> new KnifeItem(s, ModMaterials.FLINT), new Item.Settings());
	public static final Supplier<Item> IRON_KNIFE = registerWithTab("iron_knife", (s) -> new KnifeItem(s, ToolMaterial.IRON), new Item.Settings());
	public static final Supplier<Item> DIAMOND_KNIFE = registerWithTab("diamond_knife", (s) -> new KnifeItem(s, ToolMaterial.DIAMOND), new Item.Settings());
	public static final Supplier<Item> NETHERITE_KNIFE = registerWithTab("netherite_knife", (s) -> new KnifeItem(s.fireproof(), ToolMaterial.NETHERITE), new Item.Settings());
	public static final Supplier<Item> GOLDEN_KNIFE = registerWithTab("golden_knife", (s) -> new KnifeItem(s, ToolMaterial.GOLD), new Item.Settings());

	public static final Supplier<Item> STRAW = registerWithTab("straw", FuelItem::new, basicItem());
	public static final Supplier<Item> CANVAS = registerWithTab("canvas", (s) -> new FuelItem(s, 400),basicItem());
	public static final Supplier<Item> TREE_BARK = registerWithTab("tree_bark", (s) -> new FuelItem(s, 200),basicItem());

	// Wild Crops
	public static final Supplier<Item> SANDY_SHRUB = registerWithTab("sandy_shrub", (s) -> new BlockItem(ModBlocks.SANDY_SHRUB.get(), s), basicItem());
	public static final Supplier<Item> WILD_CABBAGES = registerWithTab("wild_cabbages", (s) -> new BlockItem(ModBlocks.WILD_CABBAGES.get(), s), basicItem());
	public static final Supplier<Item> WILD_ONIONS = registerWithTab("wild_onions", (s) -> new BlockItem(ModBlocks.WILD_ONIONS.get(), s), basicItem());
	public static final Supplier<Item> WILD_TOMATOES = registerWithTab("wild_tomatoes", (s) -> new BlockItem(ModBlocks.WILD_TOMATOES.get(), s), basicItem());
	public static final Supplier<Item> WILD_CARROTS = registerWithTab("wild_carrots", (s) -> new BlockItem(ModBlocks.WILD_CARROTS.get(), s), basicItem());
	public static final Supplier<Item> WILD_POTATOES = registerWithTab("wild_potatoes", (s) -> new BlockItem(ModBlocks.WILD_POTATOES.get(), s), basicItem());
	public static final Supplier<Item> WILD_BEETROOTS = registerWithTab("wild_beetroots", (s) -> new BlockItem(ModBlocks.WILD_BEETROOTS.get(), s), basicItem());
	public static final Supplier<Item> WILD_RICE = registerWithTab("wild_rice", (s) -> new TallBlockItem(ModBlocks.WILD_RICE.get(), s), basicItem());

	public static final Supplier<Item> BROWN_MUSHROOM_COLONY = registerWithTab("brown_mushroom_colony", (s) -> new MushroomColonyItem(ModBlocks.BROWN_MUSHROOM_COLONY.get(), s), basicItem());
	public static final Supplier<Item> RED_MUSHROOM_COLONY = registerWithTab("red_mushroom_colony", (s) -> new MushroomColonyItem(ModBlocks.RED_MUSHROOM_COLONY.get(), s), basicItem());

	// Basic Crops
	public static final Supplier<Item> CABBAGE = registerWithTab("cabbage", Item::new, foodItem(FoodValues.CABBAGE));
	public static final Supplier<Item> TOMATO = registerWithTab("tomato", Item::new, foodItem(FoodValues.TOMATO));
	public static final Supplier<Item> ONION = registerWithTab("onion", (s) -> new BlockItem(ModBlocks.ONION_CROP.get(),s), foodItem(FoodValues.ONION));
	public static final Supplier<Item> RICE_PANICLE = registerWithTab("rice_panicle", Item::new, basicItem());
	public static final Supplier<Item> RICE = registerWithTab("rice", (s) -> new RiceItem(ModBlocks.RICE_CROP.get(),s), basicItem());
	public static final Supplier<Item> CABBAGE_SEEDS = registerWithTab("cabbage_seeds", (s) -> new BlockItem(ModBlocks.CABBAGE_CROP.get(),s), basicItem());
	public static final Supplier<Item> TOMATO_SEEDS = registerWithTab("tomato_seeds", (s) -> new BlockItem(ModBlocks.BUDDING_TOMATO_CROP.get(),s), basicItem());
	public static final Supplier<Item> ROTTEN_TOMATO = registerWithTab("rotten_tomato", RottenTomatoItem::new,new Item.Settings().maxCount(16));

	// Foodstuffs
	public static final Supplier<Item> FRIED_EGG = registerWithTab("fried_egg", Item::new, foodItem(FoodValues.FRIED_EGG));
	public static final Supplier<Item> MILK_BOTTLE = registerWithTab("milk_bottle", MilkBottleItem::new,drinkItem());
	public static final Supplier<Item> HOT_COCOA = registerWithTab("hot_cocoa", HotCocoaItem::new,drinkItem());
	public static final Supplier<Item> APPLE_CIDER = registerWithTab("apple_cider", (s) -> new DrinkableItem(s, true, false),drinkItem().food(FoodValues.APPLE_CIDER).component(DataComponentTypes.CONSUMABLE, FoodValues.APPLE_CIDER_CONSUMABLE));
	public static final Supplier<Item> MELON_JUICE = registerWithTab("melon_juice", MelonJuiceItem::new,drinkItem());
	public static final Supplier<Item> TOMATO_SAUCE = registerWithTab("tomato_sauce", ConsumableItem::new,foodItem(FoodValues.TOMATO_SAUCE).recipeRemainder(Items.BOWL));
	public static final Supplier<Item> WHEAT_DOUGH = registerWithTab("wheat_dough", Item::new, foodItem(FoodValues.WHEAT_DOUGH).component(DataComponentTypes.CONSUMABLE, FoodValues.WHEAT_DOUGH_CONSUMABLE));
	public static final Supplier<Item> RAW_PASTA = registerWithTab("raw_pasta", Item::new, foodItem(FoodValues.RAW_PASTA).component(DataComponentTypes.CONSUMABLE, FoodValues.RAW_PASTA_CONSUMABLE));
	public static final Supplier<Item> PUMPKIN_SLICE = registerWithTab("pumpkin_slice", Item::new, foodItem(FoodValues.PUMPKIN_SLICE));
	public static final Supplier<Item> CABBAGE_LEAF = registerWithTab("cabbage_leaf", Item::new, foodItem(FoodValues.CABBAGE_LEAF));
	public static final Supplier<Item> MINCED_BEEF = registerWithTab("minced_beef", Item::new, foodItem(FoodValues.MINCED_BEEF));
	public static final Supplier<Item> BEEF_PATTY = registerWithTab("beef_patty", Item::new, foodItem(FoodValues.BEEF_PATTY));
	public static final Supplier<Item> CHICKEN_CUTS = registerWithTab("chicken_cuts", Item::new, foodItem(FoodValues.CHICKEN_CUTS).component(DataComponentTypes.CONSUMABLE, FoodValues.CHICKEN_CUTS_CONSUMABLE));
	public static final Supplier<Item> COOKED_CHICKEN_CUTS = registerWithTab("cooked_chicken_cuts", Item::new, foodItem(FoodValues.COOKED_CHICKEN_CUTS));
	public static final Supplier<Item> BACON = registerWithTab("bacon", Item::new, foodItem(FoodValues.BACON));
	public static final Supplier<Item> COOKED_BACON = registerWithTab("cooked_bacon", Item::new, foodItem(FoodValues.COOKED_BACON));
	public static final Supplier<Item> COD_SLICE = registerWithTab("cod_slice", Item::new, foodItem(FoodValues.COD_SLICE));
	public static final Supplier<Item> COOKED_COD_SLICE = registerWithTab("cooked_cod_slice", Item::new, foodItem(FoodValues.COOKED_COD_SLICE));
	public static final Supplier<Item> SALMON_SLICE = registerWithTab("salmon_slice", Item::new, foodItem(FoodValues.SALMON_SLICE));
	public static final Supplier<Item> COOKED_SALMON_SLICE = registerWithTab("cooked_salmon_slice", Item::new, foodItem(FoodValues.COOKED_SALMON_SLICE));
	public static final Supplier<Item> MUTTON_CHOPS = registerWithTab("mutton_chops", Item::new, foodItem(FoodValues.MUTTON_CHOPS));
	public static final Supplier<Item> COOKED_MUTTON_CHOPS = registerWithTab("cooked_mutton_chops", Item::new, foodItem(FoodValues.COOKED_MUTTON_CHOPS));
	public static final Supplier<Item> HAM = registerWithTab("ham", Item::new, foodItem(FoodValues.HAM));
	public static final Supplier<Item> SMOKED_HAM = registerWithTab("smoked_ham", Item::new, foodItem(FoodValues.SMOKED_HAM));

	// Sweets
	public static final Supplier<Item> PIE_CRUST = registerWithTab("pie_crust", Item::new, foodItem(FoodValues.PIE_CRUST));
	public static final Supplier<Item> APPLE_PIE = registerWithTab("apple_pie", (s) -> new BlockItem(ModBlocks.APPLE_PIE.get(),s), basicItem());
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE = registerWithTab("sweet_berry_cheesecake", (s) -> new BlockItem(ModBlocks.SWEET_BERRY_CHEESECAKE.get(),s), basicItem());
	public static final Supplier<Item> CHOCOLATE_PIE = registerWithTab("chocolate_pie", (s) -> new BlockItem(ModBlocks.CHOCOLATE_PIE.get(),s), basicItem());
	public static final Supplier<Item> CAKE_SLICE = registerWithTab("cake_slice", Item::new, foodItem(FoodValues.CAKE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.CAKE_SLICE_CONSUMABLE));
	public static final Supplier<Item> APPLE_PIE_SLICE = registerWithTab("apple_pie_slice", Item::new, foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE));
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE_SLICE = registerWithTab("sweet_berry_cheesecake_slice", Item::new, foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE));
	public static final Supplier<Item> CHOCOLATE_PIE_SLICE = registerWithTab("chocolate_pie_slice", Item::new, foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE));
	public static final Supplier<Item> SWEET_BERRY_COOKIE = registerWithTab("sweet_berry_cookie", Item::new, foodItem(FoodValues.COOKIES));
	public static final Supplier<Item> HONEY_COOKIE = registerWithTab("honey_cookie", Item::new, foodItem(FoodValues.COOKIES));
	public static final Supplier<Item> MELON_POPSICLE = registerWithTab("melon_popsicle", PopsicleItem::new,foodItem(FoodValues.POPSICLE));
	public static final Supplier<Item> GLOW_BERRY_CUSTARD = registerWithTab("glow_berry_custard", ConsumableItem::new,foodItem(FoodValues.GLOW_BERRY_CUSTARD).component(DataComponentTypes.CONSUMABLE, FoodValues.GLOW_BERRY_CUSTARD_CONSUMABLE).recipeRemainder(Items.GLASS_BOTTLE).maxCount(16));
	public static final Supplier<Item> FRUIT_SALAD = registerWithTab("fruit_salad", (s) -> new ConsumableItem(s,true),bowlFoodItem(FoodValues.FRUIT_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.FRUIT_SALAD_CONSUMABLE));

	// Basic Meals
	public static final Supplier<Item> MIXED_SALAD = registerWithTab("mixed_salad", (s) -> new ConsumableItem(s,true),bowlFoodItem(FoodValues.MIXED_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.MIXED_SALAD_CONSUMABLE));
	public static final Supplier<Item> NETHER_SALAD = registerWithTab("nether_salad", ConsumableItem::new,bowlFoodItem(FoodValues.NETHER_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.NETHER_SALAD_CONSUMABLE));
	public static final Supplier<Item> BARBECUE_STICK = registerWithTab("barbecue_stick", Item::new, foodItem(FoodValues.BARBECUE_STICK));
	public static final Supplier<Item> EGG_SANDWICH = registerWithTab("egg_sandwich", Item::new, foodItem(FoodValues.EGG_SANDWICH));
	public static final Supplier<Item> CHICKEN_SANDWICH = registerWithTab("chicken_sandwich", Item::new, foodItem(FoodValues.CHICKEN_SANDWICH));
	public static final Supplier<Item> HAMBURGER = registerWithTab("hamburger", Item::new, foodItem(FoodValues.HAMBURGER));
	public static final Supplier<Item> BACON_SANDWICH = registerWithTab("bacon_sandwich", Item::new, foodItem(FoodValues.BACON_SANDWICH));
	public static final Supplier<Item> MUTTON_WRAP = registerWithTab("mutton_wrap", Item::new, foodItem(FoodValues.MUTTON_WRAP));
	public static final Supplier<Item> DUMPLINGS = registerWithTab("dumplings", Item::new, foodItem(FoodValues.DUMPLINGS));
	public static final Supplier<Item> STUFFED_POTATO = registerWithTab("stuffed_potato", Item::new, foodItem(FoodValues.STUFFED_POTATO));
	public static final Supplier<Item> CABBAGE_ROLLS = registerWithTab("cabbage_rolls", Item::new, foodItem(FoodValues.CABBAGE_ROLLS));
	public static final Supplier<Item> SALMON_ROLL = registerWithTab("salmon_roll", Item::new, foodItem(FoodValues.SALMON_ROLL));
	public static final Supplier<Item> COD_ROLL = registerWithTab("cod_roll", Item::new, foodItem(FoodValues.COD_ROLL));
	public static final Supplier<Item> KELP_ROLL = registerWithTab("kelp_roll", Item::new, foodItem(FoodValues.KELP_ROLL));
	public static final Supplier<Item> KELP_ROLL_SLICE = registerWithTab("kelp_roll_slice", Item::new, foodItem(FoodValues.KELP_ROLL_SLICE));

	// Soups and Stews
	public static final Supplier<Item> COOKED_RICE = registerWithTab("cooked_rice", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.COOKED_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.COOKED_RICE_CONSUMABLE));
	public static final Supplier<Item> BONE_BROTH = registerWithTab("bone_broth", (s) -> new DrinkableItem(s, true),bowlFoodItem(FoodValues.BONE_BROTH).component(DataComponentTypes.CONSUMABLE, FoodValues.BONE_BROTH_CONSUMABLE));
	public static final Supplier<Item> BEEF_STEW = registerWithTab("beef_stew", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.BEEF_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.BEEF_STEW_CONSUMABLE));
	public static final Supplier<Item> CHICKEN_SOUP = registerWithTab("chicken_soup", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.CHICKEN_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.CHICKEN_SOUP_CONSUMABLE));
	public static final Supplier<Item> VEGETABLE_SOUP = registerWithTab("vegetable_soup", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.VEGETABLE_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.VEGETABLE_SOUP_CONSUMABLE));
	public static final Supplier<Item> FISH_STEW = registerWithTab("fish_stew", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.FISH_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.FISH_STEW_CONSUMABLE));
	public static final Supplier<Item> FRIED_RICE = registerWithTab("fried_rice", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.FRIED_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.FRIED_RICE_CONSUMABLE));
	public static final Supplier<Item> PUMPKIN_SOUP = registerWithTab("pumpkin_soup", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.PUMPKIN_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.PUMPKIN_SOUP_CONSUMABLE));
	public static final Supplier<Item> BAKED_COD_STEW = registerWithTab("baked_cod_stew", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.BAKED_COD_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.BAKED_COD_STEW_CONSUMABLE));
	public static final Supplier<Item> NOODLE_SOUP = registerWithTab("noodle_soup", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.NOODLE_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.NOODLE_SOUP_CONSUMABLE));

	// Plated Meals
	public static final Supplier<Item> BACON_AND_EGGS = registerWithTab("bacon_and_eggs", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.BACON_AND_EGGS).component(DataComponentTypes.CONSUMABLE, FoodValues.BACON_AND_EGGS_CONSUMABLE));
	public static final Supplier<Item> PASTA_WITH_MEATBALLS = registerWithTab("pasta_with_meatballs", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.PASTA_WITH_MEATBALLS).component(DataComponentTypes.CONSUMABLE, FoodValues.PASTA_WITH_MEATBALLS_CONSUMABLE));
	public static final Supplier<Item> PASTA_WITH_MUTTON_CHOP = registerWithTab("pasta_with_mutton_chop", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.PASTA_WITH_MUTTON_CHOP).component(DataComponentTypes.CONSUMABLE, FoodValues.PASTA_WITH_MUTTON_CHOP_CONSUMABLE));
	public static final Supplier<Item> MUSHROOM_RICE = registerWithTab("mushroom_rice", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.MUSHROOM_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.MUSHROOM_RICE_CONSUMABLE));
	public static final Supplier<Item> ROASTED_MUTTON_CHOPS = registerWithTab("roasted_mutton_chops", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.ROASTED_MUTTON_CHOPS).component(DataComponentTypes.CONSUMABLE, FoodValues.ROASTED_MUTTON_CHOPS_CONSUMABLE));
	public static final Supplier<Item> VEGETABLE_NOODLES = registerWithTab("vegetable_noodles", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.VEGETABLE_NOODLES).component(DataComponentTypes.CONSUMABLE, FoodValues.VEGETABLE_NOODLES_CONSUMABLE));
	public static final Supplier<Item> STEAK_AND_POTATOES = registerWithTab("steak_and_potatoes", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.STEAK_AND_POTATOES).component(DataComponentTypes.CONSUMABLE, FoodValues.STEAK_AND_POTATOES_CONSUMABLE));
	public static final Supplier<Item> RATATOUILLE = registerWithTab("ratatouille", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.RATATOUILLE).component(DataComponentTypes.CONSUMABLE, FoodValues.RATATOUILLE_CONSUMABLE));
	public static final Supplier<Item> SQUID_INK_PASTA = registerWithTab("squid_ink_pasta", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.SQUID_INK_PASTA).component(DataComponentTypes.CONSUMABLE, FoodValues.SQUID_INK_PASTA_CONSUMABLE));
	public static final Supplier<Item> GRILLED_SALMON = registerWithTab("grilled_salmon", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.GRILLED_SALMON).component(DataComponentTypes.CONSUMABLE, FoodValues.GRILLED_SALMON_CONSUMABLE));

	// Feasts
	public static final Supplier<Item> ROAST_CHICKEN_BLOCK = registerWithTab("roast_chicken_block", (s) -> new BlockItem(ModBlocks.ROAST_CHICKEN_BLOCK.get(),s), basicItem().maxCount(1));
	public static final Supplier<Item> ROAST_CHICKEN = registerWithTab("roast_chicken", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.ROAST_CHICKEN).component(DataComponentTypes.CONSUMABLE, FoodValues.ROAST_CHICKEN_CONSUMABLE));

	public static final Supplier<Item> STUFFED_PUMPKIN_BLOCK = registerWithTab("stuffed_pumpkin_block", (s) -> new BlockItem(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(),s), basicItem().maxCount(1));
	public static final Supplier<Item> STUFFED_PUMPKIN = registerWithTab("stuffed_pumpkin", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.STUFFED_PUMPKIN).component(DataComponentTypes.CONSUMABLE, FoodValues.STUFFED_PUMPKIN_CONSUMABLE));

	public static final Supplier<Item> HONEY_GLAZED_HAM_BLOCK = registerWithTab("honey_glazed_ham_block", (s) -> new BlockItem(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(),s), basicItem().maxCount(1));
	public static final Supplier<Item> HONEY_GLAZED_HAM = registerWithTab("honey_glazed_ham", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.HONEY_GLAZED_HAM).component(DataComponentTypes.CONSUMABLE, FoodValues.HONEY_GLAZED_HAM_CONSUMABLE));

	public static final Supplier<Item> SHEPHERDS_PIE_BLOCK = registerWithTab("shepherds_pie_block", (s) -> new BlockItem(ModBlocks.SHEPHERDS_PIE_BLOCK.get(),s), basicItem().maxCount(1));
	public static final Supplier<Item> SHEPHERDS_PIE = registerWithTab("shepherds_pie", (s) -> new ConsumableItem(s, true),bowlFoodItem(FoodValues.SHEPHERDS_PIE).component(DataComponentTypes.CONSUMABLE, FoodValues.SHEPHERDS_PIE_CONSUMABLE));

	public static final Supplier<Item> RICE_ROLL_MEDLEY_BLOCK = registerWithTab("rice_roll_medley_block", (s) -> new BlockItem(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(),s), basicItem().maxCount(1));

	// Pet Foods
	public static final Supplier<Item> DOG_FOOD = registerWithTab("dog_food", DogFoodItem::new,bowlFoodItem(FoodValues.DOG_FOOD));
	public static final Supplier<Item> HORSE_FEED = registerWithTab("horse_feed", HorseFeedItem::new,basicItem().maxCount(16));

	public static void touch() {

	}
}
