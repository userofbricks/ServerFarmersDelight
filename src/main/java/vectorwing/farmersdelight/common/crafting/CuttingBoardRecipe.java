package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import static net.minecraft.recipe.book.RecipeBookCategories.CAMPFIRE;

public class CuttingBoardRecipe implements Recipe<CuttingBoardRecipeInput>
{
	public static final int MAX_RESULTS = 4;

	private final String group;
	private final Ingredient input;
	private final Ingredient tool;
	private final List<ChanceResult> results;
	private final Optional<SoundEvent> soundEvent;

	public CuttingBoardRecipe(String group, Ingredient input, Ingredient tool, List<ChanceResult> results, Optional<SoundEvent> soundEvent) {
		this.group = group;
		this.input = input;
		this.tool = tool;
		this.results = results;
		this.soundEvent = soundEvent;
	}

	@Override
	public boolean matches(CuttingBoardRecipeInput input, World level) {
		return this.input.test(input.item()) && this.tool.test(input.tool());
	}

	@Override
	public ItemStack craft(CuttingBoardRecipeInput inv, RegistryWrapper.WrapperLookup provider) {
		return this.results.get(0).stack().copy();
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		DefaultedList<Optional<Ingredient>> nonnulllist = DefaultedList.of();
		nonnulllist.add(Optional.of(input));
		return IngredientPlacement.forMultipleSlots(nonnulllist);
	}

	public Ingredient getTool() {
		return this.tool;
	}

	public List<ItemStack> getResults() {
		return getRollableResults().stream()
				.map(ChanceResult::stack)
				.collect(Collectors.toList());
	}

	public List<ChanceResult> getRollableResults() {
		return this.results;
	}

	public List<ItemStack> rollResults(Random rand, int fortuneLevel) {
		List<ItemStack> results = new ArrayList<>();
		List<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(rand, fortuneLevel);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public Optional<SoundEvent> getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public RecipeSerializer<? extends Recipe<CuttingBoardRecipeInput>> getSerializer() {
		return ModRecipeSerializers.CUTTING.get();
	}

	@Override
	public RecipeType<? extends Recipe<CuttingBoardRecipeInput>> getType() {
		return ModRecipeTypes.CUTTING.get();
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return CAMPFIRE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CuttingBoardRecipe that = (CuttingBoardRecipe) o;

		if (!getGroup().equals(that.getGroup())) return false;
		if (!input.equals(that.input)) return false;
		if (!getTool().equals(that.getTool())) return false;
		if (!getResults().equals(that.getResults())) return false;
		return Objects.equals(soundEvent, that.soundEvent);
	}

	@Override
	public int hashCode() {
		int result = (getGroup() != null ? getGroup().hashCode() : 0);
		result = 31 * result + input.hashCode();
		result = 31 * result + getTool().hashCode();
		result = 31 * result + getResults().hashCode();
		result = 31 * result + (soundEvent.map(Object::hashCode).orElse(0));
		return result;
	}

	public static class Serializer implements RecipeSerializer<CuttingBoardRecipe>
	{
		public static final PacketCodec<RegistryByteBuf, CuttingBoardRecipe> STREAM_CODEC =
				PacketCodec.ofStatic(Serializer::toNetwork, Serializer::fromNetwork);

		private static final MapCodec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(
				inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(CuttingBoardRecipe::getGroup),
								Ingredient.CODEC.fieldOf("ingredient").forGetter(cuttingBoardRecipe -> cuttingBoardRecipe.input),
								Ingredient.CODEC.fieldOf("tool").forGetter(CuttingBoardRecipe::getTool),
								ChanceResult.CODEC.listOf().fieldOf("result").flatXmap(chanceResults -> {
									if (chanceResults.size() > 4) {
										return DataResult.error(
												() -> "Too many results for cutting recipe! The maximum quantity of unique results is "
														+ MAX_RESULTS);
									}
									return DataResult.success(chanceResults);
								}, DataResult::success).forGetter(CuttingBoardRecipe::getRollableResults),
								SoundEvent.CODEC.optionalFieldOf("sound").forGetter(CuttingBoardRecipe::getSoundEvent))
						.apply(inst, CuttingBoardRecipe::new));

		public Serializer() {
		}

		public static CuttingBoardRecipe fromNetwork(RegistryByteBuf buffer) {
			String groupIn = buffer.readString(32767);
			Ingredient inputItemIn = Ingredient.PACKET_CODEC.decode(buffer);
			Ingredient toolIn = Ingredient.PACKET_CODEC.decode(buffer);

			int i = buffer.readVarInt();
			DefaultedList<ChanceResult> resultsIn = DefaultedList.ofSize(i, ChanceResult.EMPTY);
			resultsIn.replaceAll(ignored -> ChanceResult.read(buffer));
			Optional<SoundEvent> soundEventIn = Optional.empty();
			if (buffer.readBoolean()) {
				Optional<RegistryEntry.Reference<SoundEvent>> holder = Registries.SOUND_EVENT.getOptional(buffer.readRegistryKey(RegistryKeys.SOUND_EVENT));
				if (holder.isPresent() && holder.get().hasKeyAndValue()) {
					soundEventIn = Optional.of(holder.get().value());
				}
			}

			return new CuttingBoardRecipe(groupIn, inputItemIn, toolIn, resultsIn, soundEventIn);
		}

		public static void toNetwork(RegistryByteBuf buffer, CuttingBoardRecipe recipe) {
			buffer.writeString(recipe.group);
			Ingredient.PACKET_CODEC.encode(buffer, recipe.input);
			Ingredient.PACKET_CODEC.encode(buffer, recipe.tool);
			buffer.writeVarInt(recipe.results.size());
			for (ChanceResult result : recipe.results) {
				result.write(buffer);
			}
			if (recipe.getSoundEvent().isPresent()) {
				Optional<RegistryKey<SoundEvent>> resourceKey = Registries.SOUND_EVENT.getKey(recipe.getSoundEvent().get());
				resourceKey.ifPresentOrElse(rk -> {
					buffer.writeBoolean(true);
					buffer.writeRegistryKey(rk);
				}, () -> buffer.writeBoolean(false));
			} else {
				buffer.writeBoolean(false);
			}
		}

		@Override
		public MapCodec<CuttingBoardRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, CuttingBoardRecipe> packetCodec() {
			return STREAM_CODEC;
		}
	}
}
