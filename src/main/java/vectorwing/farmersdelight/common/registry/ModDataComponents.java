package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;

import java.util.function.Supplier;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;

import static vectorwing.farmersdelight.refabricated.RegUtils.regComponent;

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

	public static final Supplier<ComponentType<Boolean>> COOKING =regComponent(
			"cooking", (builder) -> builder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).cache()
	);

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
