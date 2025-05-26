package vectorwing.farmersdelight.common.world;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class VillageStructures
{
    public static void init() {
        // As the config cannot be loaded on init, we must do this.
        ServerLifecycleEvents.SERVER_STARTING.register(VillageStructures::addNewVillageBuilding);
    }

	public static void addNewVillageBuilding(MinecraftServer server) {
		if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
			Registry<StructurePool> templatePools = server.getRegistryManager().getOrThrow(RegistryKeys.TEMPLATE_POOL);
			Registry<StructureProcessorList> processorLists = server.getRegistryManager().getOrThrow(RegistryKeys.PROCESSOR_LIST);

			VillageStructures.addBuildingToPool(templatePools, processorLists, Identifier.of("minecraft:village/plains/houses"), FarmersDelight.MODID + ":village/houses/plains_compost_pile", 5);
			VillageStructures.addBuildingToPool(templatePools, processorLists, Identifier.of("minecraft:village/snowy/houses"), FarmersDelight.MODID + ":village/houses/snowy_compost_pile", 3);
			VillageStructures.addBuildingToPool(templatePools, processorLists, Identifier.of("minecraft:village/savanna/houses"), FarmersDelight.MODID + ":village/houses/savanna_compost_pile", 4);
			VillageStructures.addBuildingToPool(templatePools, processorLists, Identifier.of("minecraft:village/desert/houses"), FarmersDelight.MODID + ":village/houses/desert_compost_pile", 3);
			VillageStructures.addBuildingToPool(templatePools, processorLists, Identifier.of("minecraft:village/taiga/houses"), FarmersDelight.MODID + ":village/houses/taiga_compost_pile", 4);
		}

		if (Configuration.GENERATE_VILLAGE_FARM_FD_CROPS.get()) {
			Registry<StructureProcessorList> processorLists = server.getRegistryManager().getOrThrow(RegistryKeys.PROCESSOR_LIST);

			StructureProcessor temperateCropProcessor = new RuleStructureProcessor(List.of(
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.TOMATO_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.ONION_CROP.get().getDefaultState())
			));

			StructureProcessor coldCropProcessor = new RuleStructureProcessor(List.of(
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.ONION_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.POTATOES, 0.2F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.POTATOES, 0.2F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.ONION_CROP.get().getDefaultState())
			));

			StructureProcessor aridCropProcessor = new RuleStructureProcessor(List.of(
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().getDefaultState()),
					new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, ModBlocks.TOMATO_CROP.get().getDefaultState())
			));

			addNewRuleToProcessorList(Identifier.of("minecraft:farm_plains"), temperateCropProcessor, processorLists);
			addNewRuleToProcessorList(Identifier.of("minecraft:farm_savanna"), aridCropProcessor, processorLists);
			addNewRuleToProcessorList(Identifier.of("minecraft:farm_snowy"), coldCropProcessor, processorLists);
			addNewRuleToProcessorList(Identifier.of("minecraft:farm_taiga"), temperateCropProcessor, processorLists);
			addNewRuleToProcessorList(Identifier.of("minecraft:farm_desert"), aridCropProcessor, processorLists);
		}
	}

	public static void addBuildingToPool(Registry<StructurePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, Identifier poolRL, String nbtPieceRL, int weight) {
		StructurePool pool = templatePoolRegistry.getEntry(poolRL).get().value();
		if (pool == null) return;

		Identifier emptyProcessor = Identifier.ofVanilla("empty");
		RegistryEntry<StructureProcessorList> processorHolder = processorListRegistry.getOrThrow(RegistryKey.of(RegistryKeys.PROCESSOR_LIST, emptyProcessor));

		SinglePoolElement piece = SinglePoolElement.ofProcessedSingle(nbtPieceRL, processorHolder).apply(StructurePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.elements.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.elementWeights);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.elementWeights = listOfPieceEntries;
	}

	private static void addNewRuleToProcessorList(Identifier targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
		processorListRegistry.getOptionalValue(targetProcessorList)
				.ifPresent(processorList -> {
					List<StructureProcessor> newSafeList = new ArrayList<>(processorList.getList());
					newSafeList.add(processorToAdd);
					processorList.list = newSafeList;
				});
	}
}
