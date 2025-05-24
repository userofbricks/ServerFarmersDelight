package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.recipe.book.RecipeBookCategories.CAMPFIRE;

public class CookingPotRecipe implements Recipe<RecipeWrapper>
{
	public static final int INPUT_SLOTS = 6;

	private final String group;
	private final DefaultedList<Ingredient> inputItems;
	private final ItemStack output;
	private final ItemStack container;
	private final ItemStack containerOverride;
	private final float experience;
	private final int cookTime;
	@Nullable
	private IngredientPlacement ingredientPlacement;

	public CookingPotRecipe(String group, DefaultedList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		this.group = group;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		} else if (!output.getRecipeRemainder().isEmpty()) {
			this.container = output.getRecipeRemainder();
		} else {
			this.container = ItemStack.EMPTY;
		}

		this.containerOverride = container;
		this.experience = experience;
		this.cookTime = cookTime;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	public DefaultedList<Ingredient> getIngredients() {
		return this.inputItems;
	}

	public IngredientPlacement getIngredientPlacement() {
		if (this.ingredientPlacement == null) {
			this.ingredientPlacement = IngredientPlacement.forShapeless(this.inputItems);
		}

		return this.ingredientPlacement;
	}

	public ItemStack getResultItem(RegistryWrapper.WrapperLookup provider) {
		return this.output;
	}

	public ItemStack getOutputContainer() {
		return this.container;
	}

	public ItemStack getContainerOverride() {
		return this.containerOverride;
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World level) {
		return inv.ingredientAmount() == this.inputItems.size() && inv.getRecipeMatcher().isCraftable(this, null);
	}

	@Override
	public ItemStack craft(RecipeWrapper input, RegistryWrapper.WrapperLookup registries) {
		return this.output.copy();
	}

	@Override
	public RecipeSerializer<CookingPotRecipe> getSerializer() {
		return ModRecipeSerializers.COOKING.get();
	}

	@Override
	public RecipeType<? extends Recipe<RecipeWrapper>> getType() {
		return ModRecipeTypes.COOKING.get();
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return CAMPFIRE;
	}

	public List<RecipeDisplay> getDisplays() {
		return List.of(new ShapelessCraftingRecipeDisplay(this.inputItems.stream().map(Ingredient::toDisplay).toList(), new SlotDisplay.StackSlotDisplay(this.output), new SlotDisplay.ItemSlotDisplay(ModItems.COOKING_POT.get())));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CookingPotRecipe that = (CookingPotRecipe) o;

		if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
		if (getCookTime() != that.getCookTime()) return false;
		if (!getGroup().equals(that.getGroup())) return false;
		if (!inputItems.equals(that.inputItems)) return false;
		if (!output.equals(that.output)) return false;
		return container.equals(that.container);
	}

	@Override
	public int hashCode() {
		int result = getGroup().hashCode();
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + container.hashCode();
		result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
	}

	public static class Serializer implements RecipeSerializer<CookingPotRecipe>
	{
		private static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(CookingPotRecipe::getGroup),
				Ingredient.CODEC.listOf().fieldOf("ingredients").xmap(ingredients -> DefaultedList.copyOf(Ingredient.ofItem(ItemStack.EMPTY.getItem()),
						ingredients.toArray(new Ingredient[0])), ingredients -> ingredients).forGetter(CookingPotRecipe::getIngredients),
				ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(r -> r.output),
				ItemStack.VALIDATED_CODEC.optionalFieldOf("container", ItemStack.EMPTY).forGetter(CookingPotRecipe::getContainerOverride),
				Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
				Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
		).apply(inst, CookingPotRecipe::new));

		public static final PacketCodec<RegistryByteBuf, CookingPotRecipe> STREAM_CODEC = PacketCodec.ofStatic(Serializer::toNetwork, Serializer::fromNetwork);

		public Serializer() {
		}

		@Override
		public MapCodec<CookingPotRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, CookingPotRecipe> packetCodec() {
			return STREAM_CODEC;
		}

		private static CookingPotRecipe fromNetwork(RegistryByteBuf buffer) {
			String groupIn = buffer.readString();
			int i = buffer.readVarInt();
			DefaultedList<Ingredient> inputItemsIn = DefaultedList.ofSize(i, Ingredient.ofItem(Items.AIR));

			inputItemsIn.replaceAll(ignored -> Ingredient.PACKET_CODEC.decode(buffer));

			ItemStack outputIn = ItemStack.PACKET_CODEC.decode(buffer);
			ItemStack container = ItemStack.OPTIONAL_PACKET_CODEC.decode(buffer);
			float experienceIn = buffer.readFloat();
			int cookTimeIn = buffer.readVarInt();
			return new CookingPotRecipe(groupIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
		}

		private static void toNetwork(RegistryByteBuf buffer, CookingPotRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeVarInt(recipe.inputItems.size());

			for (Ingredient ingredient : recipe.inputItems) {
				Ingredient.PACKET_CODEC.encode(buffer, ingredient);
			}

			ItemStack.PACKET_CODEC.encode(buffer, recipe.output);
			ItemStack.OPTIONAL_PACKET_CODEC.encode(buffer, recipe.container);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
