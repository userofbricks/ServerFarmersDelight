package vectorwing.farmersdelight.refabricated;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.EnchantmentsPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

import static net.minecraft.predicate.component.ComponentPredicateTypes.ENCHANTMENTS;

/**
 * Events for modifying vanilla/FD loot tables based on Farmer's Delight's Loot Modifiers.
 * FD loot tables are included for easier upstream merges.
 */
public class LootModificationEvents {
    private static final RegistryKey<LootTable> BLOCKS_CAKE = vanillaKey("blocks/cake");
    private static final RegistryKey<LootTable> BLOCKS_PUMPKIN = vanillaKey("blocks/pumpkin");
    private static final RegistryKey<LootTable> BLOCKS_SHORT_GRASS = vanillaKey("blocks/short_grass");
    private static final RegistryKey<LootTable> BLOCKS_TALL_GRASS = vanillaKey("blocks/tall_grass");
    private static final RegistryKey<LootTable> ENTITIES_CAVE_SPIDER = vanillaKey("entities/cave_spider");
    private static final RegistryKey<LootTable> ENTITIES_CHICKEN = vanillaKey("entities/chicken");
    private static final RegistryKey<LootTable> ENTITIES_HOGLIN = vanillaKey("entities/hoglin");
    private static final RegistryKey<LootTable> ENTITIES_PIG = vanillaKey("entities/pig");
    private static final RegistryKey<LootTable> ENTITIES_RABBIT = vanillaKey("entities/rabbit");
    private static final RegistryKey<LootTable> ENTITIES_SHULKER = vanillaKey("entities/shulker");
    private static final RegistryKey<LootTable> ENTITIES_SPIDER = vanillaKey("entities/spider");
    private static final RegistryKey<LootTable> BLOCKS_WHEAT = vanillaKey("blocks/wheat");

    private static final RegistryKey<LootTable> BLOCKS_APPLE_PIE = key("blocks/apple_pie");
    private static final RegistryKey<LootTable> BLOCKS_CHOCOLATE_PIE = key("blocks/chocolate_pie");
    private static final RegistryKey<LootTable> BLOCKS_RICE_PANICLES = key("blocks/rice_panicles");
    private static final RegistryKey<LootTable> BLOCKS_SANDY_SHRUB = key("blocks/sandy_shrub");
    private static final RegistryKey<LootTable> BLOCKS_SWEET_BERRY_CHEESECAKE = key("blocks/sweet_berry_cheesecake");

    public static final RegistryKey<LootTable> FD_ABANDONED_MINESHAFT = key("chests/fd_abandoned_mineshaft");
    public static final RegistryKey<LootTable> FD_BASTION_HOGLIN_STABLE = key("chests/fd_bastion_hoglin_stable");
    public static final RegistryKey<LootTable> FD_BASTION_TREASURE = key("chests/fd_bastion_treasure");
    public static final RegistryKey<LootTable> FD_END_CITY_TREASURE = key("chests/fd_end_city_treasure");
    public static final RegistryKey<LootTable> FD_PILLAGER_OUTPOST = key("chests/fd_pillager_outpost");
    public static final RegistryKey<LootTable> FD_RUINED_PORTAL = key("chests/fd_ruined_portal");
    public static final RegistryKey<LootTable> FD_SHIPWRECK_SUPPLY = key("chests/fd_shipwreck_supply");
    public static final RegistryKey<LootTable> FD_SIMPLE_DUNGEON = key("chests/fd_simple_dungeon");
    public static final RegistryKey<LootTable> FD_VILLAGE_BUTCHER = key("chests/fd_village_butcher");
    public static final RegistryKey<LootTable> FD_VILLAGE_DESERT_HOUSE = key("chests/fd_village_desert_house");
    public static final RegistryKey<LootTable> FD_VILLAGE_PLAINS_HOUSE = key("chests/fd_village_plains_house");
    public static final RegistryKey<LootTable> FD_VILLAGE_SAVANNA_HOUSE = key("chests/fd_village_savanna_house");
    public static final RegistryKey<LootTable> FD_VILLAGE_SNOWY_HOUSE = key("chests/fd_village_snowy_house");
    public static final RegistryKey<LootTable> FD_VILLAGE_TAIGA_HOUSE = key("chests/fd_village_taiga_house");

    public static void init() {
        LootTableEvents.MODIFY.register(LootModificationEvents::modifyTable);
    }

    private static void modifyTable(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        if (!source.isBuiltin()) // Will return if the current loot table is modified via datapack.
            return;
        chestLoot(key, tableBuilder, source, registries);
        scavengingLoot(key, tableBuilder, source, registries);
        slicingLoot(key, tableBuilder, source, registries);
        straw(key, tableBuilder, source, registries);
    }

    private static void chestLoot(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        if (key == LootTables.ABANDONED_MINESHAFT_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_ABANDONED_MINESHAFT)));

        if (key == LootTables.BASTION_HOGLIN_STABLE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_BASTION_HOGLIN_STABLE)));

        if (key == LootTables.BASTION_TREASURE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_BASTION_TREASURE)));

        if (key == LootTables.END_CITY_TREASURE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_END_CITY_TREASURE)));

        if (key == LootTables.PILLAGER_OUTPOST_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_PILLAGER_OUTPOST)));

        if (key == LootTables.RUINED_PORTAL_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_RUINED_PORTAL)));

        if (key == LootTables.SHIPWRECK_SUPPLY_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_SHIPWRECK_SUPPLY)));

        if (key == LootTables.SIMPLE_DUNGEON_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_SIMPLE_DUNGEON)));

        if (key == LootTables.VILLAGE_BUTCHER_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_BUTCHER)));

        if (key == LootTables.VILLAGE_DESERT_HOUSE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_DESERT_HOUSE)));

        if (key == LootTables.VILLAGE_PLAINS_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_PLAINS_HOUSE)));

        if (key == LootTables.VILLAGE_SAVANNA_HOUSE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_SAVANNA_HOUSE)));

        if (key == LootTables.VILLAGE_SNOWY_HOUSE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_SNOWY_HOUSE)));

        if (key == LootTables.VILLAGE_TAIGA_HOUSE_CHEST)
            tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(FD_VILLAGE_TAIGA_HOUSE)));
    }

    private static void scavengingLoot(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        RegistryWrapper<Enchantment> enchantments = registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registries.getOrThrow(RegistryKeys.ITEM);

        // scavenging_feather
        if (key == ENTITIES_CHICKEN) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.FEATHER)
                    .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                    )))));
        }
        // scavenging_ham_from_hoglin and scavenging_smoked_ham_from_hoglin
        if (key == ENTITIES_HOGLIN) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.HAM.get())
                            .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                                    EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                            )).and(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(false)))))));
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.SMOKED_HAM.get())
                            .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                                    EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                            )).and(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true)))))));
        }
        // scavenging_ham_from_pig and scavenging_smoked_ham_from_pig
        if (key == ENTITIES_PIG) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.HAM.get())
                            .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                                    )).and(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(false))))
                                    .and(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.5F, 0.1F)))));
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.SMOKED_HAM.get())
                            .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                                    EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                            )).and(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true)))
                                    .and(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.5F, 0.1F))))));
        }
        // scavenging_pumpkin
        if (key == BLOCKS_PUMPKIN) {
            tableBuilder.modifyPools(builder -> builder.conditionally(
                    MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                            .tag(itemRegistryEntryLookup, ModTags.KNIVES)
                    ).and(MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                                    .components(ComponentsPredicate.Builder.create()
                                            .partial(ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(
                                                    new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.SILK_TOUCH), NumberRange.IntRange.ANY)
                                            ))).build()
                                    )
                            ).invert()
                    ).invert().build())
            ).pool(LootPool.builder().with(ItemEntry.builder(ModItems.PUMPKIN_SLICE.get())
                            .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                                    .tag(itemRegistryEntryLookup, ModTags.KNIVES)
                            ).and(MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                                            .components(ComponentsPredicate.Builder.create()
                                                    .partial(ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(
                                                            new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.SILK_TOUCH), NumberRange.IntRange.ANY)
                                                    ))).build()
                                            )
                                    ).invert()
                            )).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F)))));
        }
        // scavenging_leather
        if (key.getValue().getPath().startsWith("entities/")) {
            RegistryWrapper<EntityType<?>> lookup = registries.getOrThrow(RegistryKeys.ENTITY_TYPE);
            var entityType = lookup.getOptional(RegistryKey.of(RegistryKeys.ENTITY_TYPE, key.getValue()
                    .withPath(s -> s.substring(9))));
            // Make sure we only add to the necessary entity loot tables by loading the tag early.
            // Otherwise, you'd be modifying every entity with a condition that's never true, which seems like a no-no.
            if (entityType.isPresent() && TagUtils.isDropsLeatherTag(entityType.get(), lookup)) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.LEATHER)
                        .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                                EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                        )))));
            }
        }

        // scavenging_rabbit_hide
        if (key == ENTITIES_RABBIT) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.RABBIT_HIDE)
                    .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                    )))));
        }

        // scavenging_shulker_shell
        if (key == ENTITIES_SHULKER) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.SHULKER_SHELL)
                    .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                    )))));
        }

        // scavenging_shulker_shell
        if (key == ENTITIES_SHULKER) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.SHULKER_SHELL)
                    .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                    )))));
        }

        // scavenging_string
        if (key == ENTITIES_SPIDER || key == ENTITIES_CAVE_SPIDER) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.STRING)
                    .conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.create().equipment(
                            EntityEquipmentPredicate.Builder.create().mainhand(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                    )))));
        }
    }

    private static void slicingLoot(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registries.getOrThrow(RegistryKeys.ITEM);
        // slicing_apple_pie
        if (key == BLOCKS_APPLE_PIE)
            pastrySlicing(tableBuilder, ModBlocks.APPLE_PIE.get(), ModItems.APPLE_PIE_SLICE.get(), PieBlock.BITES, 4, itemRegistryEntryLookup);
        // slicing_cake
        if (key == BLOCKS_CAKE)
            pastrySlicing(tableBuilder, Blocks.CAKE, ModItems.CAKE_SLICE.get(), CakeBlock.BITES, 7, itemRegistryEntryLookup);
        // slicing_candle_cake
        if (key.getValue().getPath().startsWith("blocks/")) {
            RegistryWrapper<Block> lookup = registries.getOrThrow(RegistryKeys.BLOCK);
            var block = lookup.getOptional(RegistryKey.of(RegistryKeys.BLOCK, key.getValue().withPath(s -> s.substring(7))));
            if (block.isPresent() && TagUtils.isCandleDropsCakeSliceTag(block.get(), lookup)) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.CAKE_SLICE.get()).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(7.0F)))
                        .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES)))));
            }
        }
        // slicing_chocolate_pie
        if (key == BLOCKS_CHOCOLATE_PIE)
            pastrySlicing(tableBuilder, ModBlocks.CHOCOLATE_PIE.get(), ModItems.CHOCOLATE_PIE_SLICE.get(), PieBlock.BITES, 4, itemRegistryEntryLookup);
        // slicing_sweet_berry_cheesecake
        if (key == BLOCKS_SWEET_BERRY_CHEESECAKE)
            pastrySlicing(tableBuilder, ModBlocks.SWEET_BERRY_CHEESECAKE.get(), ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), PieBlock.BITES, 4, itemRegistryEntryLookup);
    }

    public static void pastrySlicing(LootTable.Builder tableBuilder, Block block, ItemConvertible slice, IntProperty property, int maxValue, RegistryEntryLookup<Item> itemRegistryEntryLookup) {
        tableBuilder.modifyPools(builder -> builder.conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES)).invert()));
        for (int value : property.getValues()) {
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(slice).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(maxValue - value))))
                    .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.KNIVES))
                            .and(BlockStatePropertyLootCondition.builder(block)
                                    .properties(StatePredicate.Builder.create().exactMatch(property, value)))));
        }
    }

    private static void straw(RegistryKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, RegistryWrapper.WrapperLookup registries) {
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registries.getOrThrow(RegistryKeys.ITEM);
        if (key == BLOCKS_SHORT_GRASS || key == BLOCKS_TALL_GRASS)
            strawChance02(tableBuilder, itemRegistryEntryLookup);
        if (key == BLOCKS_SANDY_SHRUB)
            strawChance03(tableBuilder, itemRegistryEntryLookup);
        if (key == BLOCKS_RICE_PANICLES)
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.STRAW.get())
                    .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.STRAW_HARVESTERS))
                            .and(BlockStatePropertyLootCondition.builder(ModBlocks.RICE_CROP_PANICLES.get())
                                    .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 3))))));
        if (key == BLOCKS_WHEAT)
            tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.STRAW.get())
                    .conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.STRAW_HARVESTERS))
                            .and(BlockStatePropertyLootCondition.builder(Blocks.WHEAT)
                                    .properties(StatePredicate.Builder.create().exactMatch(CropBlock.AGE, 7))))));
    }

    public static void strawChance02(LootTable.Builder tableBuilder, RegistryEntryLookup<Item> itemRegistryEntryLookup) {
        tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.STRAW.get())
                .conditionally(RandomChanceLootCondition.builder(0.3F)
                        .and(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.STRAW_HARVESTERS))))));
    }

    public static void strawChance03(LootTable.Builder tableBuilder, RegistryEntryLookup<Item> itemRegistryEntryLookup) {
        tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(ModItems.STRAW.get())
                .conditionally(RandomChanceLootCondition.builder(0.3F)
                        .and(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(itemRegistryEntryLookup, ModTags.STRAW_HARVESTERS))))));
    }

    private static RegistryKey<LootTable> vanillaKey(String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.ofVanilla(path));
    }

    private static RegistryKey<LootTable> key(String path) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, FarmersDelight.res(path));
    }

}
