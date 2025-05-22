package vectorwing.farmersdelight.refabricated;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.registry.RegistryKeys;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.registry.RegistryKey;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagFile;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class TagUtils {
    private static ResourceManager resourceManager;
    // Vanilla loads tags after Loot Tables are loaded, so we need to do something about that.
    private static Collection<RegistryEntry<Block>> earlyDropsCakeTag;
    private static Collection<RegistryEntry<EntityType<?>>> earlyDropsLeatherTag;

    // This exists so we don't modify literally every loot table in the game just to add loot to a few
    public static boolean isCandleDropsCakeSliceTag(RegistryEntry<Block> block, RegistryWrapper<Block> lookup) {
        if (earlyDropsCakeTag == null) {
            TagGroupLoader<RegistryEntry<Block>> loader = new TagGroupLoader<>((rl,b) -> lookup.getOptional(RegistryKey.of(RegistryKeys.BLOCK, rl)), "tags/block");
            var dropsLeatherMap = loadTag(ModTags.DROPS_CAKE_SLICE);
            Map<Identifier, List<RegistryEntry<Block>>> loaded = loader.buildGroup(dropsLeatherMap);
            earlyDropsCakeTag = loaded.get(ModTags.DROPS_CAKE_SLICE.id());
            if (earlyDropsCakeTag == null)
                earlyDropsCakeTag = List.of();
        }

        return earlyDropsCakeTag.contains(block);
    }

    // This exists so we don't modify literally every loot table in the game just to add loot to a few
    public static boolean isDropsLeatherTag(RegistryEntry<EntityType<?>> entityType, RegistryWrapper<EntityType<?>> lookup) {
        if (earlyDropsLeatherTag == null) {
            TagGroupLoader<RegistryEntry<EntityType<?>>> loader = new TagGroupLoader<>((rl,b) -> lookup.getOptional(RegistryKey.of(RegistryKeys.ENTITY_TYPE, rl)), "tags/entity_type");
            var dropsLeatherMap = loadTag(ModTags.DROPS_LEATHER);
            Map<Identifier, List<RegistryEntry<EntityType<?>>>> loaded = loader.buildGroup(dropsLeatherMap);
            earlyDropsLeatherTag = loaded.get(ModTags.DROPS_LEATHER.id());
            if (earlyDropsLeatherTag == null)
                earlyDropsLeatherTag = List.of();
        }

        return earlyDropsLeatherTag.contains(entityType);
    }

    public static <T> Map<Identifier, List<TagGroupLoader.TrackedEntry>> loadTag(TagKey<T> tagKey) {
        Map<Identifier, List<TagGroupLoader.TrackedEntry>> map = Maps.newHashMap();
        String tagRegistryLocation = (tagKey.registryRef().getValue().getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? "" : tagKey.registryRef().getValue().getNamespace() + "/")  + tagKey.registryRef().getValue().getPath();
        Identifier jsonPath = Identifier.of(tagKey.id().getNamespace(), "tags/" +
                tagRegistryLocation + "/" + tagKey.id().getPath() + ".json");

        for (Resource entry : resourceManager.getAllResources(jsonPath)) {
            loadIndividualTag(tagRegistryLocation, jsonPath, entry, map);
        }

        return map;
    }

    private static void loadIndividualTag(String tagRegistryLocation, Identifier fileLocation, Resource resource, Map<Identifier, List<TagGroupLoader.TrackedEntry>> map) {
        ResourceFinder converter = ResourceFinder.json("tags/" + tagRegistryLocation);
        Identifier fileToId = converter.toResourceId(fileLocation);
        try (Reader reader = resource.getReader()) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            List<TagGroupLoader.TrackedEntry> list = map.getOrDefault(fileToId, new ArrayList<>());
            TagFile tagFile = TagFile.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement)).getOrThrow();
            if (tagFile.replace()) {
                list.clear();
            }

            tagFile.entries().forEach((tagEntry) -> {
                // Return value is unused, this was the easiest way to determine whether this was a tag or not.
                tagEntry.canAdd(resourceLocation -> {
                    list.add(new TagGroupLoader.TrackedEntry(tagEntry, resource.getPackId()));
                    return false;
                }, resourceLocation -> {
                    for (Resource innerEntry : resourceManager.getAllResources(converter.toResourcePath(resourceLocation))) {
                        loadIndividualTag(tagRegistryLocation, resourceLocation, innerEntry, map);
                    }
                    list.add(new TagGroupLoader.TrackedEntry(tagEntry, resource.getPackId()));
                    return false;
                });
            });
            map.putIfAbsent(fileToId, list);
        } catch (Exception ignored) {
            // The game should throw an exception itself upon failure.

        }
    }

    public static void setLootTableResourceManager(ResourceManager manager) {
        resourceManager = manager;
    }

    public static void resetEarlyTagCollections() {
        resourceManager = null;
        earlyDropsCakeTag = null;
        earlyDropsLeatherTag = null;
    }
}