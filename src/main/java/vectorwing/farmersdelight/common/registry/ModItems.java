package vectorwing.farmersdelight.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.*;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static vectorwing.farmersdelight.refabricated.RegUtils.regItem;

@SuppressWarnings("unused")
public class ModItems
{
	public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

	public static Supplier<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
		Supplier<Item> block = regItem(name, supplier);
		CREATIVE_TAB_ITEMS.add(block);
		return block;
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
	public static final Supplier<Item> STOVE = registerWithTab("stove", () -> new BlockItem(ModBlocks.STOVE.get(), basicItem()));
	public static final Supplier<Item> COOKING_POT = registerWithTab("cooking_pot", () -> new CookingPotItem(ModBlocks.COOKING_POT.get(), basicItem().maxCount(1)));
	public static final Supplier<Item> SKILLET = registerWithTab("skillet", () -> new SkilletItem(ModBlocks.SKILLET.get(), basicItem().maxCount(1).attributeModifiers(SkilletItem.createAttributes(SkilletItem.SKILLET_TIER, 5.0F, -3.1F))));
	public static final Supplier<Item> CUTTING_BOARD = registerWithTab("cutting_board", () -> new FuelBlockItem(ModBlocks.CUTTING_BOARD.get(), basicItem(), 200));
	public static final Supplier<Item> BASKET = registerWithTab("basket", () -> new FuelBlockItem(ModBlocks.BASKET.get(), basicItem(), 300));

	public static final Supplier<Item> CARROT_CRATE = registerWithTab("carrot_crate", () -> new BlockItem(ModBlocks.CARROT_CRATE.get(), basicItem()));
	public static final Supplier<Item> POTATO_CRATE = registerWithTab("potato_crate", () -> new BlockItem(ModBlocks.POTATO_CRATE.get(), basicItem()));
	public static final Supplier<Item> BEETROOT_CRATE = registerWithTab("beetroot_crate",() -> new BlockItem(ModBlocks.BEETROOT_CRATE.get(), basicItem()));
	public static final Supplier<Item> CABBAGE_CRATE = registerWithTab("cabbage_crate",() -> new BlockItem(ModBlocks.CABBAGE_CRATE.get(), basicItem()));
	public static final Supplier<Item> TOMATO_CRATE = registerWithTab("tomato_crate", () -> new BlockItem(ModBlocks.TOMATO_CRATE.get(), basicItem()));
	public static final Supplier<Item> ONION_CRATE = registerWithTab("onion_crate", () -> new BlockItem(ModBlocks.ONION_CRATE.get(), basicItem()));
	public static final Supplier<Item> RICE_BALE = registerWithTab("rice_bale", () -> new BlockItem(ModBlocks.RICE_BALE.get(), basicItem()));
	public static final Supplier<Item> RICE_BAG = registerWithTab("rice_bag", () -> new BlockItem(ModBlocks.RICE_BAG.get(), basicItem()));
	public static final Supplier<Item> STRAW_BALE = registerWithTab("straw_bale", () -> new BlockItem(ModBlocks.STRAW_BALE.get(), basicItem()));

	public static final Supplier<Item> SAFETY_NET = registerWithTab("safety_net", () -> new FuelBlockItem(ModBlocks.SAFETY_NET.get(), basicItem(), 200));
	public static final Supplier<Item> OAK_CABINET = registerWithTab("oak_cabinet", () -> new FuelBlockItem(ModBlocks.OAK_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> SPRUCE_CABINET = registerWithTab("spruce_cabinet", () -> new FuelBlockItem(ModBlocks.SPRUCE_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> BIRCH_CABINET = registerWithTab("birch_cabinet", () -> new FuelBlockItem(ModBlocks.BIRCH_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> JUNGLE_CABINET = registerWithTab("jungle_cabinet", () -> new FuelBlockItem(ModBlocks.JUNGLE_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> ACACIA_CABINET = registerWithTab("acacia_cabinet", () -> new FuelBlockItem(ModBlocks.ACACIA_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> DARK_OAK_CABINET = registerWithTab("dark_oak_cabinet", () -> new FuelBlockItem(ModBlocks.DARK_OAK_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> MANGROVE_CABINET = registerWithTab("mangrove_cabinet", () -> new FuelBlockItem(ModBlocks.MANGROVE_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> CHERRY_CABINET = registerWithTab("cherry_cabinet", () -> new FuelBlockItem(ModBlocks.CHERRY_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> BAMBOO_CABINET = registerWithTab("bamboo_cabinet", () -> new FuelBlockItem(ModBlocks.BAMBOO_CABINET.get(), basicItem(), 300));
	public static final Supplier<Item> CRIMSON_CABINET = registerWithTab("crimson_cabinet", () -> new BlockItem(ModBlocks.CRIMSON_CABINET.get(), basicItem()));
	public static final Supplier<Item> WARPED_CABINET = registerWithTab("warped_cabinet", () -> new BlockItem(ModBlocks.WARPED_CABINET.get(), basicItem()));
	public static final Supplier<Item> TATAMI = registerWithTab("tatami", () -> new FuelBlockItem(ModBlocks.TATAMI.get(), basicItem(), 400));
	public static final Supplier<Item> FULL_TATAMI_MAT = registerWithTab("full_tatami_mat", () -> new FuelBlockItem(ModBlocks.FULL_TATAMI_MAT.get(), basicItem(), 200));
	public static final Supplier<Item> HALF_TATAMI_MAT = registerWithTab("half_tatami_mat", () -> new FuelBlockItem(ModBlocks.HALF_TATAMI_MAT.get(), basicItem()));
	public static final Supplier<Item> CANVAS_RUG = registerWithTab("canvas_rug", () -> new FuelBlockItem(ModBlocks.CANVAS_RUG.get(), basicItem(), 200));
	public static final Supplier<Item> ORGANIC_COMPOST = registerWithTab("organic_compost", () -> new BlockItem(ModBlocks.ORGANIC_COMPOST.get(), basicItem()));
	public static final Supplier<Item> RICH_SOIL = registerWithTab("rich_soil", () -> new BlockItem(ModBlocks.RICH_SOIL.get(), basicItem()));
	public static final Supplier<Item> RICH_SOIL_FARMLAND = registerWithTab("rich_soil_farmland", () -> new BlockItem(ModBlocks.RICH_SOIL_FARMLAND.get(), basicItem()));
	public static final Supplier<Item> ROPE = registerWithTab("rope", () -> new RopeItem(ModBlocks.ROPE.get(), basicItem()));

	// Tools
	public static final Supplier<Item> FLINT_KNIFE = registerWithTab("flint_knife", () -> new KnifeItem(new Item.Settings(), ModMaterials.FLINT));
	public static final Supplier<Item> IRON_KNIFE = registerWithTab("iron_knife", () -> new KnifeItem(new Item.Settings(), ToolMaterial.IRON));
	public static final Supplier<Item> DIAMOND_KNIFE = registerWithTab("diamond_knife", () -> new KnifeItem(new Item.Settings(), ToolMaterial.DIAMOND));
	public static final Supplier<Item> NETHERITE_KNIFE = registerWithTab("netherite_knife", () -> new KnifeItem(new Item.Settings().fireproof(), ToolMaterial.NETHERITE));
	public static final Supplier<Item> GOLDEN_KNIFE = registerWithTab("golden_knife", () -> new KnifeItem(new Item.Settings(), ToolMaterial.GOLD));

	public static final Supplier<Item> STRAW = registerWithTab("straw", () -> new FuelItem(basicItem()));
	public static final Supplier<Item> CANVAS = registerWithTab("canvas", () -> new FuelItem(basicItem(), 400));
	public static final Supplier<Item> TREE_BARK = registerWithTab("tree_bark", () -> new FuelItem(basicItem(), 200));

	// Wild Crops
	public static final Supplier<Item> SANDY_SHRUB = registerWithTab("sandy_shrub", () -> new BlockItem(ModBlocks.SANDY_SHRUB.get(), basicItem()));
	public static final Supplier<Item> WILD_CABBAGES = registerWithTab("wild_cabbages", () -> new BlockItem(ModBlocks.WILD_CABBAGES.get(), basicItem()));
	public static final Supplier<Item> WILD_ONIONS = registerWithTab("wild_onions", () -> new BlockItem(ModBlocks.WILD_ONIONS.get(), basicItem()));
	public static final Supplier<Item> WILD_TOMATOES = registerWithTab("wild_tomatoes", () -> new BlockItem(ModBlocks.WILD_TOMATOES.get(), basicItem()));
	public static final Supplier<Item> WILD_CARROTS = registerWithTab("wild_carrots", () -> new BlockItem(ModBlocks.WILD_CARROTS.get(), basicItem()));
	public static final Supplier<Item> WILD_POTATOES = registerWithTab("wild_potatoes", () -> new BlockItem(ModBlocks.WILD_POTATOES.get(), basicItem()));
	public static final Supplier<Item> WILD_BEETROOTS = registerWithTab("wild_beetroots", () -> new BlockItem(ModBlocks.WILD_BEETROOTS.get(), basicItem()));
	public static final Supplier<Item> WILD_RICE = registerWithTab("wild_rice", () -> new TallBlockItem(ModBlocks.WILD_RICE.get(), basicItem()));

	public static final Supplier<Item> BROWN_MUSHROOM_COLONY = registerWithTab("brown_mushroom_colony", () -> new MushroomColonyItem(ModBlocks.BROWN_MUSHROOM_COLONY.get(), basicItem()));
	public static final Supplier<Item> RED_MUSHROOM_COLONY = registerWithTab("red_mushroom_colony", () -> new MushroomColonyItem(ModBlocks.RED_MUSHROOM_COLONY.get(), basicItem()));

	// Basic Crops
	public static final Supplier<Item> CABBAGE = registerWithTab("cabbage", () -> new Item(foodItem(FoodValues.CABBAGE)));
	public static final Supplier<Item> TOMATO = registerWithTab("tomato", () -> new Item(foodItem(FoodValues.TOMATO)));
	public static final Supplier<Item> ONION = registerWithTab("onion", () -> new BlockItem(ModBlocks.ONION_CROP.get(), foodItem(FoodValues.ONION)));
	public static final Supplier<Item> RICE_PANICLE = registerWithTab("rice_panicle", () -> new Item(basicItem()));
	public static final Supplier<Item> RICE = registerWithTab("rice", () -> new RiceItem(ModBlocks.RICE_CROP.get(), basicItem()));
	public static final Supplier<Item> CABBAGE_SEEDS = registerWithTab("cabbage_seeds", () -> new BlockItem(ModBlocks.CABBAGE_CROP.get(), basicItem()));
	public static final Supplier<Item> TOMATO_SEEDS = registerWithTab("tomato_seeds", () -> new BlockItem(ModBlocks.BUDDING_TOMATO_CROP.get(), basicItem()));
	public static final Supplier<Item> ROTTEN_TOMATO = registerWithTab("rotten_tomato", () -> new RottenTomatoItem(new Item.Settings().maxCount(16)));

	// Foodstuffs
	public static final Supplier<Item> FRIED_EGG = registerWithTab("fried_egg", () -> new Item(foodItem(FoodValues.FRIED_EGG)));
	public static final Supplier<Item> MILK_BOTTLE = registerWithTab("milk_bottle", () -> new MilkBottleItem(drinkItem()));
	public static final Supplier<Item> HOT_COCOA = registerWithTab("hot_cocoa", () -> new HotCocoaItem(drinkItem()));
	public static final Supplier<Item> APPLE_CIDER = registerWithTab("apple_cider", () -> new DrinkableItem(drinkItem().food(FoodValues.APPLE_CIDER).component(DataComponentTypes.CONSUMABLE, FoodValues.APPLE_CIDER_CONSUMABLE), true, false));
	public static final Supplier<Item> MELON_JUICE = registerWithTab("melon_juice", () -> new MelonJuiceItem(drinkItem()));
	public static final Supplier<Item> TOMATO_SAUCE = registerWithTab("tomato_sauce", () -> new ConsumableItem(foodItem(FoodValues.TOMATO_SAUCE).recipeRemainder(Items.BOWL)));
	public static final Supplier<Item> WHEAT_DOUGH = registerWithTab("wheat_dough", () -> new Item(foodItem(FoodValues.WHEAT_DOUGH).component(DataComponentTypes.CONSUMABLE, FoodValues.WHEAT_DOUGH_CONSUMABLE)));
	public static final Supplier<Item> RAW_PASTA = registerWithTab("raw_pasta", () -> new Item(foodItem(FoodValues.RAW_PASTA).component(DataComponentTypes.CONSUMABLE, FoodValues.RAW_PASTA_CONSUMABLE)));
	public static final Supplier<Item> PUMPKIN_SLICE = registerWithTab("pumpkin_slice", () -> new Item(foodItem(FoodValues.PUMPKIN_SLICE)));
	public static final Supplier<Item> CABBAGE_LEAF = registerWithTab("cabbage_leaf", () -> new Item(foodItem(FoodValues.CABBAGE_LEAF)));
	public static final Supplier<Item> MINCED_BEEF = registerWithTab("minced_beef", () -> new Item(foodItem(FoodValues.MINCED_BEEF)));
	public static final Supplier<Item> BEEF_PATTY = registerWithTab("beef_patty", () -> new Item(foodItem(FoodValues.BEEF_PATTY)));
	public static final Supplier<Item> CHICKEN_CUTS = registerWithTab("chicken_cuts", () -> new Item(foodItem(FoodValues.CHICKEN_CUTS).component(DataComponentTypes.CONSUMABLE, FoodValues.CHICKEN_CUTS_CONSUMABLE)));
	public static final Supplier<Item> COOKED_CHICKEN_CUTS = registerWithTab("cooked_chicken_cuts", () -> new Item(foodItem(FoodValues.COOKED_CHICKEN_CUTS)));
	public static final Supplier<Item> BACON = registerWithTab("bacon", () -> new Item(foodItem(FoodValues.BACON)));
	public static final Supplier<Item> COOKED_BACON = registerWithTab("cooked_bacon", () -> new Item(foodItem(FoodValues.COOKED_BACON)));
	public static final Supplier<Item> COD_SLICE = registerWithTab("cod_slice", () -> new Item(foodItem(FoodValues.COD_SLICE)));
	public static final Supplier<Item> COOKED_COD_SLICE = registerWithTab("cooked_cod_slice", () -> new Item(foodItem(FoodValues.COOKED_COD_SLICE)));
	public static final Supplier<Item> SALMON_SLICE = registerWithTab("salmon_slice", () -> new Item(foodItem(FoodValues.SALMON_SLICE)));
	public static final Supplier<Item> COOKED_SALMON_SLICE = registerWithTab("cooked_salmon_slice", () -> new Item(foodItem(FoodValues.COOKED_SALMON_SLICE)));
	public static final Supplier<Item> MUTTON_CHOPS = registerWithTab("mutton_chops", () -> new Item(foodItem(FoodValues.MUTTON_CHOPS)));
	public static final Supplier<Item> COOKED_MUTTON_CHOPS = registerWithTab("cooked_mutton_chops", () -> new Item(foodItem(FoodValues.COOKED_MUTTON_CHOPS)));
	public static final Supplier<Item> HAM = registerWithTab("ham", () -> new Item(foodItem(FoodValues.HAM)));
	public static final Supplier<Item> SMOKED_HAM = registerWithTab("smoked_ham", () -> new Item(foodItem(FoodValues.SMOKED_HAM)));

	// Sweets
	public static final Supplier<Item> PIE_CRUST = registerWithTab("pie_crust", () -> new Item(foodItem(FoodValues.PIE_CRUST)));
	public static final Supplier<Item> APPLE_PIE = registerWithTab("apple_pie", () -> new BlockItem(ModBlocks.APPLE_PIE.get(), basicItem()));
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE = registerWithTab("sweet_berry_cheesecake", () -> new BlockItem(ModBlocks.SWEET_BERRY_CHEESECAKE.get(), basicItem()));
	public static final Supplier<Item> CHOCOLATE_PIE = registerWithTab("chocolate_pie", () -> new BlockItem(ModBlocks.CHOCOLATE_PIE.get(), basicItem()));
	public static final Supplier<Item> CAKE_SLICE = registerWithTab("cake_slice", () -> new Item(foodItem(FoodValues.CAKE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.CAKE_SLICE_CONSUMABLE)));
	public static final Supplier<Item> APPLE_PIE_SLICE = registerWithTab("apple_pie_slice", () -> new Item(foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE)));
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE_SLICE = registerWithTab("sweet_berry_cheesecake_slice", () -> new Item(foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE)));
	public static final Supplier<Item> CHOCOLATE_PIE_SLICE = registerWithTab("chocolate_pie_slice", () -> new Item(foodItem(FoodValues.PIE_SLICE).component(DataComponentTypes.CONSUMABLE, FoodValues.PIE_SLICE_CONSUMABLE)));
	public static final Supplier<Item> SWEET_BERRY_COOKIE = registerWithTab("sweet_berry_cookie", () -> new Item(foodItem(FoodValues.COOKIES)));
	public static final Supplier<Item> HONEY_COOKIE = registerWithTab("honey_cookie", () -> new Item(foodItem(FoodValues.COOKIES)));
	public static final Supplier<Item> MELON_POPSICLE = registerWithTab("melon_popsicle", () -> new PopsicleItem(foodItem(FoodValues.POPSICLE)));
	public static final Supplier<Item> GLOW_BERRY_CUSTARD = registerWithTab("glow_berry_custard", () -> new ConsumableItem(foodItem(FoodValues.GLOW_BERRY_CUSTARD).component(DataComponentTypes.CONSUMABLE, FoodValues.GLOW_BERRY_CUSTARD_CONSUMABLE).recipeRemainder(Items.GLASS_BOTTLE).maxCount(16)));
	public static final Supplier<Item> FRUIT_SALAD = registerWithTab("fruit_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.FRUIT_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.FRUIT_SALAD_CONSUMABLE), true));

	// Basic Meals
	public static final Supplier<Item> MIXED_SALAD = registerWithTab("mixed_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.MIXED_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.MIXED_SALAD_CONSUMABLE), true));
	public static final Supplier<Item> NETHER_SALAD = registerWithTab("nether_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.NETHER_SALAD).component(DataComponentTypes.CONSUMABLE, FoodValues.NETHER_SALAD_CONSUMABLE)));
	public static final Supplier<Item> BARBECUE_STICK = registerWithTab("barbecue_stick", () -> new Item(foodItem(FoodValues.BARBECUE_STICK)));
	public static final Supplier<Item> EGG_SANDWICH = registerWithTab("egg_sandwich", () -> new Item(foodItem(FoodValues.EGG_SANDWICH)));
	public static final Supplier<Item> CHICKEN_SANDWICH = registerWithTab("chicken_sandwich", () -> new Item(foodItem(FoodValues.CHICKEN_SANDWICH)));
	public static final Supplier<Item> HAMBURGER = registerWithTab("hamburger", () -> new Item(foodItem(FoodValues.HAMBURGER)));
	public static final Supplier<Item> BACON_SANDWICH = registerWithTab("bacon_sandwich", () -> new Item(foodItem(FoodValues.BACON_SANDWICH)));
	public static final Supplier<Item> MUTTON_WRAP = registerWithTab("mutton_wrap", () -> new Item(foodItem(FoodValues.MUTTON_WRAP)));
	public static final Supplier<Item> DUMPLINGS = registerWithTab("dumplings", () -> new Item(foodItem(FoodValues.DUMPLINGS)));
	public static final Supplier<Item> STUFFED_POTATO = registerWithTab("stuffed_potato", () -> new Item(foodItem(FoodValues.STUFFED_POTATO)));
	public static final Supplier<Item> CABBAGE_ROLLS = registerWithTab("cabbage_rolls", () -> new Item(foodItem(FoodValues.CABBAGE_ROLLS)));
	public static final Supplier<Item> SALMON_ROLL = registerWithTab("salmon_roll", () -> new Item(foodItem(FoodValues.SALMON_ROLL)));
	public static final Supplier<Item> COD_ROLL = registerWithTab("cod_roll", () -> new Item(foodItem(FoodValues.COD_ROLL)));
	public static final Supplier<Item> KELP_ROLL = registerWithTab("kelp_roll", () -> new Item(foodItem(FoodValues.KELP_ROLL)));
	public static final Supplier<Item> KELP_ROLL_SLICE = registerWithTab("kelp_roll_slice", () -> new Item(foodItem(FoodValues.KELP_ROLL_SLICE)));

	// Soups and Stews
	public static final Supplier<Item> COOKED_RICE = registerWithTab("cooked_rice", () -> new ConsumableItem(bowlFoodItem(FoodValues.COOKED_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.COOKED_RICE_CONSUMABLE), true));
	public static final Supplier<Item> BONE_BROTH = registerWithTab("bone_broth", () -> new DrinkableItem(bowlFoodItem(FoodValues.BONE_BROTH).component(DataComponentTypes.CONSUMABLE, FoodValues.BONE_BROTH_CONSUMABLE), true));
	public static final Supplier<Item> BEEF_STEW = registerWithTab("beef_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.BEEF_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.BEEF_STEW_CONSUMABLE), true));
	public static final Supplier<Item> CHICKEN_SOUP = registerWithTab("chicken_soup", () -> new ConsumableItem(bowlFoodItem(FoodValues.CHICKEN_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.CHICKEN_SOUP_CONSUMABLE), true));
	public static final Supplier<Item> VEGETABLE_SOUP = registerWithTab("vegetable_soup", () -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.VEGETABLE_SOUP_CONSUMABLE), true));
	public static final Supplier<Item> FISH_STEW = registerWithTab("fish_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.FISH_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.FISH_STEW_CONSUMABLE), true));
	public static final Supplier<Item> FRIED_RICE = registerWithTab("fried_rice", () -> new ConsumableItem(bowlFoodItem(FoodValues.FRIED_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.FRIED_RICE_CONSUMABLE), true));
	public static final Supplier<Item> PUMPKIN_SOUP = registerWithTab("pumpkin_soup", () -> new ConsumableItem(bowlFoodItem(FoodValues.PUMPKIN_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.PUMPKIN_SOUP_CONSUMABLE), true));
	public static final Supplier<Item> BAKED_COD_STEW = registerWithTab("baked_cod_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.BAKED_COD_STEW).component(DataComponentTypes.CONSUMABLE, FoodValues.BAKED_COD_STEW_CONSUMABLE), true));
	public static final Supplier<Item> NOODLE_SOUP = registerWithTab("noodle_soup", () -> new ConsumableItem(bowlFoodItem(FoodValues.NOODLE_SOUP).component(DataComponentTypes.CONSUMABLE, FoodValues.NOODLE_SOUP_CONSUMABLE), true));

	// Plated Meals
	public static final Supplier<Item> BACON_AND_EGGS = registerWithTab("bacon_and_eggs", () -> new ConsumableItem(bowlFoodItem(FoodValues.BACON_AND_EGGS).component(DataComponentTypes.CONSUMABLE, FoodValues.BACON_AND_EGGS_CONSUMABLE), true));
	public static final Supplier<Item> PASTA_WITH_MEATBALLS = registerWithTab("pasta_with_meatballs", () -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MEATBALLS).component(DataComponentTypes.CONSUMABLE, FoodValues.PASTA_WITH_MEATBALLS_CONSUMABLE), true));
	public static final Supplier<Item> PASTA_WITH_MUTTON_CHOP = registerWithTab("pasta_with_mutton_chop", () -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MUTTON_CHOP).component(DataComponentTypes.CONSUMABLE, FoodValues.PASTA_WITH_MUTTON_CHOP_CONSUMABLE), true));
	public static final Supplier<Item> MUSHROOM_RICE = registerWithTab("mushroom_rice", () -> new ConsumableItem(bowlFoodItem(FoodValues.MUSHROOM_RICE).component(DataComponentTypes.CONSUMABLE, FoodValues.MUSHROOM_RICE_CONSUMABLE), true));
	public static final Supplier<Item> ROASTED_MUTTON_CHOPS = registerWithTab("roasted_mutton_chops", () -> new ConsumableItem(bowlFoodItem(FoodValues.ROASTED_MUTTON_CHOPS).component(DataComponentTypes.CONSUMABLE, FoodValues.ROASTED_MUTTON_CHOPS_CONSUMABLE), true));
	public static final Supplier<Item> VEGETABLE_NOODLES = registerWithTab("vegetable_noodles", () -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_NOODLES).component(DataComponentTypes.CONSUMABLE, FoodValues.VEGETABLE_NOODLES_CONSUMABLE), true));
	public static final Supplier<Item> STEAK_AND_POTATOES = registerWithTab("steak_and_potatoes", () -> new ConsumableItem(bowlFoodItem(FoodValues.STEAK_AND_POTATOES).component(DataComponentTypes.CONSUMABLE, FoodValues.STEAK_AND_POTATOES_CONSUMABLE), true));
	public static final Supplier<Item> RATATOUILLE = registerWithTab("ratatouille", () -> new ConsumableItem(bowlFoodItem(FoodValues.RATATOUILLE).component(DataComponentTypes.CONSUMABLE, FoodValues.RATATOUILLE_CONSUMABLE), true));
	public static final Supplier<Item> SQUID_INK_PASTA = registerWithTab("squid_ink_pasta", () -> new ConsumableItem(bowlFoodItem(FoodValues.SQUID_INK_PASTA).component(DataComponentTypes.CONSUMABLE, FoodValues.SQUID_INK_PASTA_CONSUMABLE), true));
	public static final Supplier<Item> GRILLED_SALMON = registerWithTab("grilled_salmon", () -> new ConsumableItem(bowlFoodItem(FoodValues.GRILLED_SALMON).component(DataComponentTypes.CONSUMABLE, FoodValues.GRILLED_SALMON_CONSUMABLE), true));

	// Feasts
	public static final Supplier<Item> ROAST_CHICKEN_BLOCK = registerWithTab("roast_chicken_block", () -> new BlockItem(ModBlocks.ROAST_CHICKEN_BLOCK.get(), basicItem().maxCount(1)));
	public static final Supplier<Item> ROAST_CHICKEN = registerWithTab("roast_chicken", () -> new ConsumableItem(bowlFoodItem(FoodValues.ROAST_CHICKEN).component(DataComponentTypes.CONSUMABLE, FoodValues.ROAST_CHICKEN_CONSUMABLE), true));

	public static final Supplier<Item> STUFFED_PUMPKIN_BLOCK = registerWithTab("stuffed_pumpkin_block", () -> new BlockItem(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), basicItem().maxCount(1)));
	public static final Supplier<Item> STUFFED_PUMPKIN = registerWithTab("stuffed_pumpkin", () -> new ConsumableItem(bowlFoodItem(FoodValues.STUFFED_PUMPKIN).component(DataComponentTypes.CONSUMABLE, FoodValues.STUFFED_PUMPKIN_CONSUMABLE), true));

	public static final Supplier<Item> HONEY_GLAZED_HAM_BLOCK = registerWithTab("honey_glazed_ham_block", () -> new BlockItem(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), basicItem().maxCount(1)));
	public static final Supplier<Item> HONEY_GLAZED_HAM = registerWithTab("honey_glazed_ham", () -> new ConsumableItem(bowlFoodItem(FoodValues.HONEY_GLAZED_HAM).component(DataComponentTypes.CONSUMABLE, FoodValues.HONEY_GLAZED_HAM_CONSUMABLE), true));

	public static final Supplier<Item> SHEPHERDS_PIE_BLOCK = registerWithTab("shepherds_pie_block", () -> new BlockItem(ModBlocks.SHEPHERDS_PIE_BLOCK.get(), basicItem().maxCount(1)));
	public static final Supplier<Item> SHEPHERDS_PIE = registerWithTab("shepherds_pie", () -> new ConsumableItem(bowlFoodItem(FoodValues.SHEPHERDS_PIE).component(DataComponentTypes.CONSUMABLE, FoodValues.SHEPHERDS_PIE_CONSUMABLE), true));

	public static final Supplier<Item> RICE_ROLL_MEDLEY_BLOCK = registerWithTab("rice_roll_medley_block", () -> new BlockItem(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(), basicItem().maxCount(1)));

	// Pet Foods
	public static final Supplier<Item> DOG_FOOD = registerWithTab("dog_food", () -> new DogFoodItem(bowlFoodItem(FoodValues.DOG_FOOD)));
	public static final Supplier<Item> HORSE_FEED = registerWithTab("horse_feed", () -> new HorseFeedItem(basicItem().maxCount(16)));

	public static void touch() {

	}
}
