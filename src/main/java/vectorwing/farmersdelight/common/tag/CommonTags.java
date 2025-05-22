package vectorwing.farmersdelight.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * References to tags under the Forge namespace.
 * These tags are generally used for crafting recipes across different mods.
 */
public class CommonTags
{
	// Blocks that are efficiently mined with a Knife.
	public static final TagKey<Block> MINEABLE_WITH_KNIFE = commonBlockTag("mineable/knife");

	public static final TagKey<Block> STORAGE_BLOCKS_CARROT = commonBlockTag("storage_blocks/carrot");
	public static final TagKey<Block> STORAGE_BLOCKS_POTATO = commonBlockTag("storage_blocks/potato");
	public static final TagKey<Block> STORAGE_BLOCKS_BEETROOT = commonBlockTag("storage_blocks/beetroot");
	public static final TagKey<Block> STORAGE_BLOCKS_CABBAGE = commonBlockTag("storage_blocks/cabbage");
	public static final TagKey<Block> STORAGE_BLOCKS_TOMATO = commonBlockTag("storage_blocks/tomato");
	public static final TagKey<Block> STORAGE_BLOCKS_ONION = commonBlockTag("storage_blocks/onion");
	public static final TagKey<Block> STORAGE_BLOCKS_RICE = commonBlockTag("storage_blocks/rice");
	public static final TagKey<Block> STORAGE_BLOCKS_RICE_PANICLE = commonBlockTag("storage_blocks/rice_panicle");
	public static final TagKey<Block> STORAGE_BLOCKS_STRAW = commonBlockTag("storage_blocks/straw");

	// Tags under the "crops" subgroup.
	public static final TagKey<Item> CROPS_CABBAGE = commonItemTag("crops/cabbage");
	public static final TagKey<Item> CROPS_TOMATO = commonItemTag("crops/tomato");
	public static final TagKey<Item> CROPS_ONION = commonItemTag("crops/onion");
	public static final TagKey<Item> CROPS_RICE = commonItemTag("crops/rice");
	public static final TagKey<Item> CROPS_GRAIN = commonItemTag("crops/grain");

	// Tags under the "foods" subgroup.
	public static final TagKey<Item> FOODS_CABBAGE = commonItemTag("foods/cabbage");
	public static final TagKey<Item> FOODS_TOMATO = commonItemTag("foods/tomato");
	public static final TagKey<Item> FOODS_ONION = commonItemTag("foods/onion");

	public static final TagKey<Item> FOODS_LEAFY_GREEN = commonItemTag("foods/leafy_green");
	public static final TagKey<Item> FOODS_DOUGH = commonItemTag("foods/dough");
	public static final TagKey<Item> FOODS_PASTA = commonItemTag("foods/pasta");
	public static final TagKey<Item> FOODS_MILK = commonItemTag("foods/milk");

	public static final TagKey<Item> FOODS_RAW_BACON = commonItemTag("foods/raw_bacon");
	public static final TagKey<Item> FOODS_RAW_BEEF = commonItemTag("foods/raw_beef");
	public static final TagKey<Item> FOODS_RAW_CHICKEN = commonItemTag("foods/raw_chicken");
	public static final TagKey<Item> FOODS_RAW_PORK = commonItemTag("foods/raw_pork");
	public static final TagKey<Item> FOODS_RAW_MUTTON = commonItemTag("foods/raw_mutton");
	public static final TagKey<Item> FOODS_SAFE_RAW_FISH = commonItemTag("foods/safe_raw_fish");
	public static final TagKey<Item> FOODS_RAW_COD = commonItemTag("foods/raw_cod");
	public static final TagKey<Item> FOODS_RAW_SALMON = commonItemTag("foods/raw_salmon");

	public static final TagKey<Item> FOODS_COOKED_BACON = commonItemTag("foods/cooked_bacon");
	public static final TagKey<Item> FOODS_COOKED_BEEF = commonItemTag("foods/cooked_beef");
	public static final TagKey<Item> FOODS_COOKED_CHICKEN = commonItemTag("foods/cooked_chicken");
	public static final TagKey<Item> FOODS_COOKED_PORK = commonItemTag("foods/cooked_pork");
	public static final TagKey<Item> FOODS_COOKED_MUTTON = commonItemTag("foods/cooked_mutton");
	public static final TagKey<Item> FOODS_COOKED_EGG = commonItemTag("foods/cooked_egg");
	public static final TagKey<Item> FOODS_COOKED_COD = commonItemTag("foods/cooked_cod");
	public static final TagKey<Item> FOODS_COOKED_SALMON = commonItemTag("foods/cooked_salmon");

	// Tags under the "tools" subgroup.
	public static final TagKey<Item> TOOLS_KNIFE = commonItemTag("tools/knife");

	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_CARROT = commonItemTag("storage_blocks/carrot");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_POTATO = commonItemTag("storage_blocks/potato");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_BEETROOT = commonItemTag("storage_blocks/beetroot");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_CABBAGE = commonItemTag("storage_blocks/cabbage");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_TOMATO = commonItemTag("storage_blocks/tomato");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_ONION = commonItemTag("storage_blocks/onion");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_RICE = commonItemTag("storage_blocks/rice");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_RICE_PANICLE = commonItemTag("storage_blocks/rice_panicle");
	public static final TagKey<Item> STORAGE_BLOCKS_ITEM_STRAW = commonItemTag("storage_blocks/straw");

	private static TagKey<Block> commonBlockTag(String path) {
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
	}

	private static TagKey<Item> commonItemTag(String path) {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
	}

}
