package vectorwing.farmersdelight.common.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public class ItemStackWrapper
{
	public static final ItemStackWrapper EMPTY = new ItemStackWrapper(ItemStack.EMPTY);
	public static final Codec<ItemStackWrapper> CODEC = ItemStack.OPTIONAL_CODEC.xmap(ItemStackWrapper::new, ItemStackWrapper::getStack);
	public static final PacketCodec<RegistryByteBuf, ItemStackWrapper> STREAM_CODEC =
			ItemStack.OPTIONAL_PACKET_CODEC.xmap(ItemStackWrapper::new, (itemStackWrapper) -> itemStackWrapper.itemStack);

	private final ItemStack itemStack;
	private final int hashCode;

	public ItemStackWrapper(ItemStack stack) {
		this.itemStack = stack;
		this.hashCode = ItemStack.hashCode(stack);
	}

	public ItemStack getStack() {
		return this.itemStack;
	}

	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ItemStackWrapper itemStackWrapper) {
			return ItemStack.areEqual(this.itemStack, itemStackWrapper.getStack());
		}
		return false;
	}
}
