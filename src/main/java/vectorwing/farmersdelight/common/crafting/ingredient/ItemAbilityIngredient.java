package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.refabricated.ItemAbility;

import java.util.List;
import java.util.stream.Stream;

/**
 * Ingredient that checks if the given stack can perform a ItemAbility from Forge.
 */
@MethodsReturnNonnullByDefault
public class ItemAbilityIngredient implements CustomIngredient
{
	public static final Serializer SERIALIZER = new Serializer();
	public static final Identifier SERIALIZER_ID = FarmersDelight.res("item_ability");

	protected final ItemAbility itemAbility;
	protected Stream<RegistryEntry<Item>>  itemStacks;

	public ItemAbilityIngredient(ItemAbility itemAbility) {
		this.itemAbility = itemAbility;
	}

	public static void init() {
		CustomIngredientSerializer.register(SERIALIZER);
	}

    protected void dissolve() {
		if (this.itemStacks == null) {
			itemStacks = Registries.ITEM.stream()
					.map(ItemStack::new)
					.filter(itemAbility::canPerformAction)
					.map(ItemStack::getRegistryEntry);
		}
	}

	@Override
	public boolean test(@Nullable ItemStack stack) {
		return stack != null &&  itemAbility.canPerformAction(stack);
	}

	@Override
	public Stream<RegistryEntry<Item>> getMatchingItems() {
		dissolve();
		return itemStacks;
	}

	public ItemAbility getItemAbility() {
		return itemAbility;
	}

	@Override
	public boolean requiresTesting() {
		return false;
	}

	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements CustomIngredientSerializer<ItemAbilityIngredient> {
		public static final MapCodec<ItemAbilityIngredient> CODEC = RecordCodecBuilder.mapCodec(inst ->
				inst.group(ItemAbility.CODEC.fieldOf("action").forGetter(ItemAbilityIngredient::getItemAbility)
				).apply(inst, ItemAbilityIngredient::new));
		public static final PacketCodec<RegistryByteBuf, ItemAbilityIngredient> STREAM_CODEC = PacketCodecs.codec(ItemAbility.CODEC).xmap(ItemAbilityIngredient::new, ItemAbilityIngredient::getItemAbility).cast();

		@Override
		public Identifier getIdentifier() {
			return SERIALIZER_ID;
		}

		@Override
		public MapCodec<ItemAbilityIngredient> getCodec() {return CODEC;}

		@Override
		public PacketCodec<RegistryByteBuf, ItemAbilityIngredient> getPacketCodec() {
			return STREAM_CODEC;
		}
	}

}
