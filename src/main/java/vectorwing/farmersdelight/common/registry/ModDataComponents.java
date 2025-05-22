package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.network.codec.PacketCodecs;

import static vectorwing.farmersdelight.refabricated.RegUtils.regComponent;
import static vectorwing.farmersdelight.refabricated.RegUtils.regEnchComponent;

public class ModDataComponents
{
	// Cooking Pot
	public static final Supplier<ComponentType<ItemStackWrapper>> MEAL =regComponent(
			"meal", builder -> builder.codec(ItemStackWrapper.CODEC).packetCodec(ItemStackWrapper.STREAM_CODEC).cache()
	);
	public static final Supplier<ComponentType<ItemStackWrapper>> CONTAINER =regComponent(
			"container", builder -> builder.codec(ItemStackWrapper.CODEC).packetCodec(ItemStackWrapper.STREAM_CODEC).cache()
	);

	// Skillet
	public static final Supplier<ComponentType<Integer>> COOKING_TIME_LENGTH =regComponent(
			"cooking_time_length", (builder) -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER)
	);

	public static final Supplier<ComponentType<ItemStackWrapper>> SKILLET_INGREDIENT =regComponent(
			"skillet_ingredient", (builder) -> builder.codec(ItemStackWrapper.CODEC).packetCodec(ItemStackWrapper.STREAM_CODEC).cache()
	);

	/*  //TODO: add to Skillet for model change
        // could have been done with item renderer but this way we can have easier control over item positioning using the model json
        ItemProperties.register(ModItems.SKILLET.get(), Identifier.ofVanilla("cooking"),
                (stack, world, entity, s) -> stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY).getStack().isEmpty() ? 0 : 1);
	 */
	public static final Supplier<ComponentType<Boolean>> COOKING =regComponent(
			"cooking", (builder) -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).cache()
	);

	// Enchantment Effects
	public static final Supplier<ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>>> BACKSTABBING = regEnchComponent(
			"backstabbing", builder -> builder.codec(
					EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_DAMAGE).listOf()
			));

	// Refabricated
	public static final Supplier<ComponentType<Long>> SKILLET_FLIP_TIMESTAMP =regComponent(
			"skillet_flip_timestamp", (builder) -> builder.codec(Codec.LONG).packetCodec(PacketCodecs.VAR_LONG).cache()
	);
	public static final Supplier<ComponentType<Boolean>> SKILLET_FLIPPED =regComponent(
			"skillet_flipped", (builder) -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).cache()
	);

	public static void touch() {

	}
}
