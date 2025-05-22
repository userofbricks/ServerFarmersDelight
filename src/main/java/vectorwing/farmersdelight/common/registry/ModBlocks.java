package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.common.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;

import static vectorwing.farmersdelight.refabricated.RegUtils.regBlock;

public class ModBlocks
{
	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.get(Properties.LIT) ? lightValue : 0;
	}

	// Workstations
	public static final Supplier<Block> STOVE = regBlock("stove",
			() -> new StoveBlock(Block.Properties.copy(Blocks.BRICKS).luminance(litBlockEmission(13))));
	public static final Supplier<Block> COOKING_POT = regBlock("cooking_pot",
			() -> new CookingPotBlock(Block.Properties.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN)));
	public static final Supplier<Block> SKILLET = regBlock("skillet",
			() -> new SkilletBlock(Block.Properties.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN)));
	public static final Supplier<Block> BASKET = regBlock("basket",
			() -> new BasketBlock(Block.Properties.create().strength(1.5F).sounds(BlockSoundGroup.BAMBOO_WOOD)));
	public static final Supplier<Block> CUTTING_BOARD = regBlock("cutting_board",
			() -> new CuttingBoardBlock(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F).sounds(BlockSoundGroup.WOOD)));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = regBlock("carrot_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> POTATO_CRATE = regBlock("potato_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> BEETROOT_CRATE = regBlock("beetroot_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> CABBAGE_CRATE = regBlock("cabbage_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> TOMATO_CRATE = regBlock("tomato_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> ONION_CRATE = regBlock("onion_crate",
			() -> new Block(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD)));
	public static final Supplier<Block> RICE_BALE = regBlock("rice_bale",
			() -> new RiceBaleBlock(Block.Properties.copy(Blocks.HAY_BLOCK)));
	public static final Supplier<Block> RICE_BAG = regBlock("rice_bag",
			() -> new Block(Block.Properties.copy(Blocks.WHITE_WOOL)));
	public static final Supplier<Block> STRAW_BALE = regBlock("straw_bale",
			() -> new StrawBaleBlock(Block.Properties.copy(Blocks.HAY_BLOCK)));

	// Building
	public static final Supplier<Block> ROPE = regBlock("rope",
			() -> new RopeBlock(Block.Properties.copy(Blocks.BROWN_CARPET).noCollision().nonOpaque().strength(0.2F).sounds(BlockSoundGroup.WOOL)));
	public static final Supplier<Block> SAFETY_NET = regBlock("safety_net",
			() -> new SafetyNetBlock(Block.Properties.copy(Blocks.BROWN_CARPET).strength(0.2F).sounds(BlockSoundGroup.WOOL)));
	public static final Supplier<Block> OAK_CABINET = regBlock("oak_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> SPRUCE_CABINET = regBlock("spruce_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> BIRCH_CABINET = regBlock("birch_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> JUNGLE_CABINET = regBlock("jungle_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> ACACIA_CABINET = regBlock("acacia_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> DARK_OAK_CABINET = regBlock("dark_oak_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> MANGROVE_CABINET = regBlock("mangrove_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final Supplier<Block> CHERRY_CABINET = regBlock("cherry_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL).sounds(BlockSoundGroup.CHERRY_WOOD)));
	public static final Supplier<Block> BAMBOO_CABINET = regBlock("bamboo_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL).sounds(BlockSoundGroup.BAMBOO_WOOD)));
	public static final Supplier<Block> CRIMSON_CABINET = regBlock("crimson_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL).sounds(BlockSoundGroup.NETHER_WOOD)));
	public static final Supplier<Block> WARPED_CABINET = regBlock("warped_cabinet",
			() -> new CabinetBlock(Block.Properties.copy(Blocks.BARREL).sounds(BlockSoundGroup.NETHER_WOOD)));
	public static final Supplier<Block> CANVAS_RUG = regBlock("canvas_rug",
			() -> new CanvasRugBlock(Block.Properties.copy(Blocks.WHITE_CARPET).sounds(BlockSoundGroup.GRASS).strength(0.2F)));
	public static final Supplier<Block> TATAMI = regBlock("tatami",
			() -> new TatamiBlock(Block.Properties.copy(Blocks.WHITE_WOOL)));
	public static final Supplier<Block> FULL_TATAMI_MAT = regBlock("full_tatami_mat",
			() -> new TatamiMatBlock(Block.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F)));
	public static final Supplier<Block> HALF_TATAMI_MAT = regBlock("half_tatami_mat",
			() -> new TatamiHalfMatBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).strength(0.3F).pistonBehavior(PistonBehavior.DESTROY)));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = regBlock("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.getRegistryEntry(), Block.Properties.copy(Blocks.BROWN_MUSHROOM)));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = regBlock("red_mushroom_colony",
			() -> new MushroomColonyBlock(Items.RED_MUSHROOM.getRegistryEntry(), Block.Properties.copy(Blocks.RED_MUSHROOM)));
	public static final Supplier<Block> ORGANIC_COMPOST = regBlock("organic_compost",
			() -> new OrganicCompostBlock(Block.Properties.copy(Blocks.DIRT).strength(1.2F).sounds(BlockSoundGroup.CROP)));
	public static final Supplier<Block> RICH_SOIL = regBlock("rich_soil",
			() -> new RichSoilBlock(Block.Properties.copy(Blocks.DIRT).ticksRandomly()));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = regBlock("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(Block.Properties.copy(Blocks.FARMLAND)));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = regBlock("apple_pie",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.APPLE_PIE_SLICE.get())); //dont kill double lambda
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = regBlock("sweet_berry_cheesecake",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()));
	public static final Supplier<Block> CHOCOLATE_PIE = regBlock("chocolate_pie",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.CHOCOLATE_PIE_SLICE.get()));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = regBlock("sandy_shrub",
			() -> new SandyShrubBlock(Block.Properties.copy(Blocks.TALL_GRASS)));

	public static final Supplier<Block> WILD_CABBAGES = regBlock("wild_cabbages",
			() -> new WildCropBlock(MobEffects.DAMAGE_BOOST, 6, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_ONIONS = regBlock("wild_onions",
			() -> new WildCropBlock(StatusEffects.FIRE_RESISTANCE, 6, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_TOMATOES = regBlock("wild_tomatoes",
			() -> new WildCropBlock(StatusEffects.POISON, 10, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_CARROTS = regBlock("wild_carrots",
			() -> new WildCropBlock(MobEffects.DIG_SLOWDOWN, 6, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_POTATOES = regBlock("wild_potatoes",
			() -> new WildCropBlock(MobEffects.CONFUSION, 8, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_BEETROOTS = regBlock("wild_beetroots",
			() -> new WildCropBlock(StatusEffects.WATER_BREATHING, 8, Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_RICE = regBlock("wild_rice",
			() -> new WildRiceBlock(Block.Properties.copy(Blocks.TALL_GRASS)));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = regBlock("cabbages",
			() -> new CabbageBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final Supplier<Block> ONION_CROP = regBlock("onions",
			() -> new OnionBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = regBlock("budding_tomatoes",
			() -> new BuddingTomatoBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final Supplier<Block> TOMATO_CROP = regBlock("tomatoes",
			() -> new TomatoVineBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final Supplier<Block> RICE_CROP = regBlock("rice",
			() -> new RiceBlock(Block.Properties.copy(Blocks.WHEAT).strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = regBlock("rice_panicles",
			() -> new RicePaniclesBlock(Block.Properties.copy(Blocks.WHEAT)));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = regBlock("roast_chicken_block",
			() -> new RoastChickenBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.ROAST_CHICKEN.get(), true));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = regBlock("stuffed_pumpkin_block",
			() -> new FeastBlock(Block.Properties.copy(Blocks.PUMPKIN), ()->ModItems.STUFFED_PUMPKIN.get(), false));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = regBlock("honey_glazed_ham_block",
			() -> new HoneyGlazedHamBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.HONEY_GLAZED_HAM.get(), true));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = regBlock("shepherds_pie_block",
			() -> new ShepherdsPieBlock(Block.Properties.copy(Blocks.CAKE), ()->ModItems.SHEPHERDS_PIE.get(), true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = regBlock("rice_roll_medley_block",
			() -> new RiceRollMedleyBlock(Block.Properties.copy(Blocks.CAKE)));

	public static void touch() {

	}
}
