package vectorwing.farmersdelight.common.registry;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.Function;
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

import static vectorwing.farmersdelight.FarmersDelight.res;

public class ModBlocks
{
	private static ToIntFunction<BlockState> litBlockEmission() {
		return (state) -> state.get(Properties.LIT) ? 13 : 0;
	}

	// Workstations
	public static final Supplier<Block> STOVE = regBlock("stove",
            StoveBlock::new, AbstractBlock.Settings.copy(Blocks.BRICKS).luminance(litBlockEmission()));
	public static final Supplier<Block> COOKING_POT = regBlock("cooking_pot",
			CookingPotBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN));
	public static final Supplier<Block> SKILLET = regBlock("skillet",
			SkilletBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).strength(0.5F, 6.0F).sounds(BlockSoundGroup.LANTERN));
	public static final Supplier<Block> CUTTING_BOARD = regBlock("cutting_board",
			CuttingBoardBlock::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F).sounds(BlockSoundGroup.WOOD));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = regBlock("carrot_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> POTATO_CRATE = regBlock("potato_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> BEETROOT_CRATE = regBlock("beetroot_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> CABBAGE_CRATE = regBlock("cabbage_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> TOMATO_CRATE = regBlock("tomato_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> ONION_CRATE = regBlock("onion_crate",
			Block::new, AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD));
	public static final Supplier<Block> RICE_BALE = regBlock("rice_bale",
			RiceBaleBlock::new, AbstractBlock.Settings.copy(Blocks.HAY_BLOCK));
	public static final Supplier<Block> RICE_BAG = regBlock("rice_bag",
			Block::new, AbstractBlock.Settings.copy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> STRAW_BALE = regBlock("straw_bale",
			StrawBaleBlock::new, AbstractBlock.Settings.copy(Blocks.HAY_BLOCK));

	// Building
	public static final Supplier<Block> ROPE = regBlock("rope",
			RopeBlock::new, AbstractBlock.Settings.copy(Blocks.BROWN_CARPET).noCollision().nonOpaque().strength(0.2F).sounds(BlockSoundGroup.WOOL));
	public static final Supplier<Block> SAFETY_NET = regBlock("safety_net",
			SafetyNetBlock::new, AbstractBlock.Settings.copy(Blocks.BROWN_CARPET).strength(0.2F).sounds(BlockSoundGroup.WOOL));
	public static final Supplier<Block> OAK_CABINET = regBlock("oak_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> SPRUCE_CABINET = regBlock("spruce_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> BIRCH_CABINET = regBlock("birch_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> JUNGLE_CABINET = regBlock("jungle_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> ACACIA_CABINET = regBlock("acacia_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> DARK_OAK_CABINET = regBlock("dark_oak_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> MANGROVE_CABINET = regBlock("mangrove_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL));
	public static final Supplier<Block> CHERRY_CABINET = regBlock("cherry_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL).sounds(BlockSoundGroup.CHERRY_WOOD));
	public static final Supplier<Block> BAMBOO_CABINET = regBlock("bamboo_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL).sounds(BlockSoundGroup.BAMBOO_WOOD));
	public static final Supplier<Block> CRIMSON_CABINET = regBlock("crimson_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL).sounds(BlockSoundGroup.NETHER_WOOD));
	public static final Supplier<Block> WARPED_CABINET = regBlock("warped_cabinet",
			CabinetBlock::new, AbstractBlock.Settings.copy(Blocks.BARREL).sounds(BlockSoundGroup.NETHER_WOOD));
	public static final Supplier<Block> CANVAS_RUG = regBlock("canvas_rug",
			CanvasRugBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_CARPET).sounds(BlockSoundGroup.GRASS).strength(0.2F));
	public static final Supplier<Block> TATAMI = regBlock("tatami",
			TatamiBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> FULL_TATAMI_MAT = regBlock("full_tatami_mat",
			TatamiMatBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).strength(0.3F));
	public static final Supplier<Block> HALF_TATAMI_MAT = regBlock("half_tatami_mat",
			TatamiHalfMatBlock::new, AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).strength(0.3F).pistonBehavior(PistonBehavior.DESTROY));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = regBlock("brown_mushroom_colony",
			(s) -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.getRegistryEntry(), s), AbstractBlock.Settings.copy(Blocks.BROWN_MUSHROOM));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = regBlock("red_mushroom_colony",
			(s) -> new MushroomColonyBlock(Items.RED_MUSHROOM.getRegistryEntry(), s), AbstractBlock.Settings.copy(Blocks.RED_MUSHROOM));
	public static final Supplier<Block> ORGANIC_COMPOST = regBlock("organic_compost",
			OrganicCompostBlock::new, AbstractBlock.Settings.copy(Blocks.DIRT).strength(1.2F).sounds(BlockSoundGroup.CROP));
	public static final Supplier<Block> RICH_SOIL = regBlock("rich_soil",
			RichSoilBlock::new, AbstractBlock.Settings.copy(Blocks.DIRT).ticksRandomly());
	public static final Supplier<Block> RICH_SOIL_FARMLAND = regBlock("rich_soil_farmland",
			RichSoilFarmlandBlock::new, AbstractBlock.Settings.copy(Blocks.FARMLAND));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = regBlock("apple_pie",
			(s) -> new PieBlock(s, ()->ModItems.APPLE_PIE_SLICE.get()), AbstractBlock.Settings.copy(Blocks.CAKE)); //dont kill double lambda
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = regBlock("sweet_berry_cheesecake",
			(s) -> new PieBlock(s, ()->ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()), AbstractBlock.Settings.copy(Blocks.CAKE));
	public static final Supplier<Block> CHOCOLATE_PIE = regBlock("chocolate_pie",
			(s) -> new PieBlock(s, ()->ModItems.CHOCOLATE_PIE_SLICE.get()), AbstractBlock.Settings.copy(Blocks.CAKE));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = regBlock("sandy_shrub",
			SandyShrubBlock::new, AbstractBlock.Settings.copy(Blocks.TALL_GRASS));

	public static final Supplier<Block> WILD_CABBAGES = regBlock("wild_cabbages",
			(s) -> new WildCropBlock(StatusEffects.STRENGTH, 6,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_ONIONS = regBlock("wild_onions",
			(s) -> new WildCropBlock(StatusEffects.FIRE_RESISTANCE, 6,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_TOMATOES = regBlock("wild_tomatoes",
			(s) -> new WildCropBlock(StatusEffects.POISON, 10,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_CARROTS = regBlock("wild_carrots",
			(s) -> new WildCropBlock(StatusEffects.MINING_FATIGUE, 6,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_POTATOES = regBlock("wild_potatoes",
			(s) -> new WildCropBlock(StatusEffects.NAUSEA, 8,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_BEETROOTS = regBlock("wild_beetroots",
			(s) -> new WildCropBlock(StatusEffects.WATER_BREATHING, 8,s), AbstractBlock.Settings.copy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_RICE = regBlock("wild_rice",
			WildRiceBlock::new, AbstractBlock.Settings.copy(Blocks.TALL_GRASS));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = regBlock("cabbages",
			CabbageBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT));
	public static final Supplier<Block> ONION_CROP = regBlock("onions",
			OnionBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = regBlock("budding_tomatoes",
			BuddingTomatoBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT));
	public static final Supplier<Block> TOMATO_CROP = regBlock("tomatoes",
			TomatoVineBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT));
	public static final Supplier<Block> RICE_CROP = regBlock("rice",
			RiceBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT).strength(0.2F));
	public static final Supplier<Block> RICE_CROP_PANICLES = regBlock("rice_panicles",
			RicePaniclesBlock::new, AbstractBlock.Settings.copy(Blocks.WHEAT));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = regBlock("roast_chicken_block",
			(s) -> new RoastChickenBlock(s, ()->ModItems.ROAST_CHICKEN.get(), true), AbstractBlock.Settings.copy(Blocks.CAKE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = regBlock("stuffed_pumpkin_block",
			(s) -> new FeastBlock(s, ()->ModItems.STUFFED_PUMPKIN.get(), false), AbstractBlock.Settings.copy(Blocks.PUMPKIN));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = regBlock("honey_glazed_ham_block",
			(s) -> new HoneyGlazedHamBlock(s, ()->ModItems.HONEY_GLAZED_HAM.get(), true), AbstractBlock.Settings.copy(Blocks.CAKE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = regBlock("shepherds_pie_block",
			(s) -> new ShepherdsPieBlock(s, ()->ModItems.SHEPHERDS_PIE.get(), true), AbstractBlock.Settings.copy(Blocks.CAKE));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = regBlock("rice_roll_medley_block",
            RiceRollMedleyBlock::new, AbstractBlock.Settings.copy(Blocks.CAKE));

	public static void touch() {

	}
	private static Supplier<Block> regBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = keyOfBlock(name);
		Block block = blockFactory.apply(settings.registryKey(blockKey));
		Registry.register(Registries.BLOCK, blockKey, block);
		return () -> block;
	}

	private static RegistryKey<Block> keyOfBlock(String name) {
		return RegistryKey.of(RegistryKeys.BLOCK, res(name));
	}
}
