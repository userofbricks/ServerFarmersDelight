package vectorwing.farmersdelight.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightBasicModelProvider extends FabricModelProvider {
    public FarmersDelightBasicModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.BACON.get());
        itemModelGenerator.register(ModItems.BACON_AND_EGGS.get());
        itemModelGenerator.register(ModItems.BACON_SANDWICH.get());
        itemModelGenerator.register(ModItems.BAKED_COD_STEW.get());
        itemModelGenerator.register(ModItems.BAMBOO_CABINET.get());
        itemModelGenerator.register(ModItems.BARBECUE_STICK.get());
        itemModelGenerator.register(ModItems.BEEF_PATTY.get());
        itemModelGenerator.register(ModItems.BEEF_STEW.get());
        itemModelGenerator.register(ModItems.BEETROOT_CRATE.get());
        itemModelGenerator.register(ModItems.BIRCH_CABINET.get());
        itemModelGenerator.register(ModItems.BONE_BROTH.get());
        itemModelGenerator.register(ModItems.BROWN_MUSHROOM_COLONY.get());
        itemModelGenerator.register(ModItems.CABBAGE.get());
        itemModelGenerator.register(ModItems.CABBAGE_CRATE.get());
        itemModelGenerator.register(ModItems.CABBAGE_LEAF.get());
        itemModelGenerator.register(ModItems.CABBAGE_ROLLS.get());
        itemModelGenerator.register(ModItems.CABBAGE_SEEDS.get());
        itemModelGenerator.register(ModItems.CAKE_SLICE.get());
        itemModelGenerator.register(ModItems.CANVAS_RUG.get());
        itemModelGenerator.register(ModItems.CANVAS.get());
        itemModelGenerator.register(ModItems.CARROT_CRATE.get());
        itemModelGenerator.register(ModItems.CHERRY_CABINET.get());
        itemModelGenerator.register(ModItems.CHICKEN_CUTS.get());
        itemModelGenerator.register(ModItems.CHICKEN_SANDWICH.get());
        itemModelGenerator.register(ModItems.CHICKEN_SOUP.get());
        itemModelGenerator.register(ModItems.CHOCOLATE_PIE.get());
        itemModelGenerator.register(ModItems.CHOCOLATE_PIE_SLICE.get());
        itemModelGenerator.register(ModItems.COD_ROLL.get());
        itemModelGenerator.register(ModItems.COD_SLICE.get());
        itemModelGenerator.register(ModItems.COOKED_BACON.get());
        itemModelGenerator.register(ModItems.COOKED_CHICKEN_CUTS.get());
        itemModelGenerator.register(ModItems.COOKED_COD_SLICE.get());
        itemModelGenerator.register(ModItems.COOKED_MUTTON_CHOPS.get());
        itemModelGenerator.register(ModItems.COOKED_RICE.get());
        itemModelGenerator.register(ModItems.COOKED_SALMON_SLICE.get());
        itemModelGenerator.register(ModItems.COOKING_POT.get());
        itemModelGenerator.register(ModItems.CRIMSON_CABINET.get());
        itemModelGenerator.register(ModItems.CUTTING_BOARD.get());
        itemModelGenerator.register(ModItems.DARK_OAK_CABINET.get());
        itemModelGenerator.register(ModItems.DIAMOND_KNIFE.get());
        itemModelGenerator.register(ModItems.DOG_FOOD.get());
        itemModelGenerator.register(ModItems.DUMPLINGS.get());
        itemModelGenerator.register(ModItems.EGG_SANDWICH.get());
        itemModelGenerator.register(ModItems.FISH_STEW.get());
        itemModelGenerator.register(ModItems.FLINT_KNIFE.get());
        itemModelGenerator.register(ModItems.FRIED_EGG.get());
        itemModelGenerator.register(ModItems.FRIED_RICE.get());
        itemModelGenerator.register(ModItems.FRUIT_SALAD.get());
        itemModelGenerator.register(ModItems.FULL_TATAMI_MAT.get());
        itemModelGenerator.register(ModItems.GLOW_BERRY_CUSTARD.get());
        itemModelGenerator.register(ModItems.GOLDEN_KNIFE.get());
        itemModelGenerator.register(ModItems.GRILLED_SALMON.get());
        itemModelGenerator.register(ModItems.HALF_TATAMI_MAT.get());
        itemModelGenerator.register(ModItems.HAM.get());
        itemModelGenerator.register(ModItems.HAMBURGER.get());
        itemModelGenerator.register(ModItems.HONEY_COOKIE.get());
        itemModelGenerator.register(ModItems.HONEY_GLAZED_HAM.get());
        itemModelGenerator.register(ModItems.HONEY_GLAZED_HAM_BLOCK.get());
        itemModelGenerator.register(ModItems.HORSE_FEED.get());
        itemModelGenerator.register(ModItems.HOT_COCOA.get());
        itemModelGenerator.register(ModItems.IRON_KNIFE.get());
        itemModelGenerator.register(ModItems.JUNGLE_CABINET.get());
        itemModelGenerator.register(ModItems.KELP_ROLL.get());
        itemModelGenerator.register(ModItems.KELP_ROLL_SLICE.get());
        itemModelGenerator.register(ModItems.MANGROVE_CABINET.get());
        itemModelGenerator.register(ModItems.MELON_JUICE.get());
        itemModelGenerator.register(ModItems.MELON_POPSICLE.get());
        itemModelGenerator.register(ModItems.MILK_BOTTLE.get());
        itemModelGenerator.register(ModItems.MINCED_BEEF.get());
        itemModelGenerator.register(ModItems.MIXED_SALAD.get());
        itemModelGenerator.register(ModItems.MUSHROOM_RICE.get());
        itemModelGenerator.register(ModItems.MUTTON_CHOPS.get());
        itemModelGenerator.register(ModItems.MUTTON_WRAP.get());
        itemModelGenerator.register(ModItems.NETHER_SALAD.get());
        itemModelGenerator.register(ModItems.NETHERITE_KNIFE.get());
        itemModelGenerator.register(ModItems.NOODLE_SOUP.get());
        itemModelGenerator.register(ModItems.OAK_CABINET.get());
        itemModelGenerator.register(ModItems.ONION.get());
        itemModelGenerator.register(ModItems.ONION_CRATE.get());
        itemModelGenerator.register(ModItems.ORGANIC_COMPOST.get());
        itemModelGenerator.register(ModItems.PASTA_WITH_MEATBALLS.get());
        itemModelGenerator.register(ModItems.PASTA_WITH_MUTTON_CHOP.get());
        itemModelGenerator.register(ModItems.PIE_CRUST.get());
        itemModelGenerator.register(ModItems.POTATO_CRATE.get());
        itemModelGenerator.register(ModItems.PUMPKIN_SLICE.get());
        itemModelGenerator.register(ModItems.PUMPKIN_SOUP.get());
        itemModelGenerator.register(ModItems.RATATOUILLE.get());
        itemModelGenerator.register(ModItems.RAW_PASTA.get());
        itemModelGenerator.register(ModItems.RED_MUSHROOM_COLONY.get());
        itemModelGenerator.register(ModItems.RICE.get());
        itemModelGenerator.register(ModItems.RICE_BAG.get());
        itemModelGenerator.register(ModItems.RICE_BALE.get());
        itemModelGenerator.register(ModItems.RICE_PANICLE.get());
        itemModelGenerator.register(ModItems.RICE_ROLL_MEDLEY_BLOCK.get());
        itemModelGenerator.register(ModItems.RICH_SOIL.get());
        itemModelGenerator.register(ModItems.RICH_SOIL_FARMLAND.get());
        itemModelGenerator.register(ModItems.ROAST_CHICKEN.get());
        itemModelGenerator.register(ModItems.ROAST_CHICKEN_BLOCK.get());
        itemModelGenerator.register(ModItems.ROASTED_MUTTON_CHOPS.get());
        itemModelGenerator.register(ModItems.ROPE.get());
        itemModelGenerator.register(ModItems.ROTTEN_TOMATO.get());
        itemModelGenerator.register(ModItems.SAFETY_NET.get());
        itemModelGenerator.register(ModItems.SALMON_ROLL.get());
        itemModelGenerator.register(ModItems.SALMON_SLICE.get());
        itemModelGenerator.register(ModItems.SANDY_SHRUB.get());
        itemModelGenerator.register(ModItems.SHEPHERDS_PIE.get());
        itemModelGenerator.register(ModItems.SHEPHERDS_PIE_BLOCK.get());
        itemModelGenerator.register(ModItems.SMOKED_HAM.get());
        itemModelGenerator.register(ModItems.SPRUCE_CABINET.get());
        itemModelGenerator.register(ModItems.SQUID_INK_PASTA.get());
        itemModelGenerator.register(ModItems.STEAK_AND_POTATOES.get());
        itemModelGenerator.register(ModItems.STOVE.get());
        itemModelGenerator.register(ModItems.STRAW.get());
        itemModelGenerator.register(ModItems.STRAW_BALE.get());
        itemModelGenerator.register(ModItems.STUFFED_POTATO.get());
        itemModelGenerator.register(ModItems.STUFFED_PUMPKIN.get());
        itemModelGenerator.register(ModItems.STUFFED_PUMPKIN_BLOCK.get());
        itemModelGenerator.register(ModItems.SWEET_BERRY_CHEESECAKE.get());
        itemModelGenerator.register(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get());
        itemModelGenerator.register(ModItems.SWEET_BERRY_COOKIE.get());
        itemModelGenerator.register(ModItems.TATAMI.get());
        itemModelGenerator.register(ModItems.TOMATO.get());
        itemModelGenerator.register(ModItems.TOMATO_CRATE.get());
        itemModelGenerator.register(ModItems.TOMATO_SAUCE.get());
        itemModelGenerator.register(ModItems.TOMATO_SEEDS.get());
        itemModelGenerator.register(ModItems.TREE_BARK.get());
        itemModelGenerator.register(ModItems.VEGETABLE_NOODLES.get());
        itemModelGenerator.register(ModItems.VEGETABLE_SOUP.get());
        itemModelGenerator.register(ModItems.WARPED_CABINET.get());
        itemModelGenerator.register(ModItems.WHEAT_DOUGH.get());
        itemModelGenerator.register(ModItems.WILD_BEETROOTS.get());
        itemModelGenerator.register(ModItems.WILD_CABBAGES.get());
        itemModelGenerator.register(ModItems.WILD_CARROTS.get());
        itemModelGenerator.register(ModItems.WILD_ONIONS.get());
        itemModelGenerator.register(ModItems.WILD_POTATOES.get());
        itemModelGenerator.register(ModItems.WILD_RICE.get());
        itemModelGenerator.register(ModItems.WILD_TOMATOES.get());
    }

    @Override
    public String getName() {
        return "FabricDocsReference Model Provider";
    }
}
