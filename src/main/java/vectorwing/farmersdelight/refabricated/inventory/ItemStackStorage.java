package vectorwing.farmersdelight.refabricated.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;

public class ItemStackStorage extends SingleItemStorage {
    public final int index;
    private final ItemStackHandler handler;

    public ItemStackStorage(int index, ItemStackHandler handler) {
        this.index = index;
        this.handler = handler;
    }

    @Override
    protected long getCapacity(ItemVariant variant) {
        return handler.getSlotLimit(index);
    }

    @Override
    public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        nbt.copyFrom((NbtCompound) ItemStack.CODEC.encodeStart(RegistryOps.of(NbtOps.INSTANCE, provider), variant.toStack((int)amount)).getOrThrow());
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup provider) {
        ItemStack stack = ItemStack.CODEC.decode(RegistryOps.of(NbtOps.INSTANCE, provider), nbt).getOrThrow().getFirst();
        variant = ItemVariant.of(stack);
        amount = stack.getCount();
    }
}
