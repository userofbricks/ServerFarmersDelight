package vectorwing.farmersdelight.common.world.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.AbstractConditionalPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import vectorwing.farmersdelight.common.registry.ModPlacementModifiers;

public class BiomeTagFilter extends AbstractConditionalPlacementModifier
{
	public static final MapCodec<BiomeTagFilter> CODEC = RecordCodecBuilder.mapCodec((builder) ->
			builder.group(
					TagKey.unprefixedCodec(RegistryKeys.BIOME).fieldOf("tag").forGetter((instance) -> instance.biomeTag)
			).apply(builder, BiomeTagFilter::new));
	private final TagKey<Biome> biomeTag;

	private BiomeTagFilter(TagKey<Biome> biomeTag) {
		this.biomeTag = biomeTag;
	}

	public static BiomeTagFilter biomeIsInTag(TagKey<Biome> biomeTag) {
		return new BiomeTagFilter(biomeTag);
	}

	@Override
	protected boolean shouldPlace(FeaturePlacementContext context, Random random, BlockPos pos) {
		RegistryEntry<Biome> biome = context.getWorld().getBiome(pos);
		return biome.isIn(biomeTag);
	}

	@Override
	public PlacementModifierType<?> getType() {
		return ModPlacementModifiers.BIOME_TAG.get();
	}
}
