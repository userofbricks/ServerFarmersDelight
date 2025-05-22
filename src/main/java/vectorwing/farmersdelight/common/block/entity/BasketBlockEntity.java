package vectorwing.farmersdelight.common.block.entity;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import vectorwing.farmersdelight.common.block.BasketBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

;

public class BasketBlockEntity extends LootableContainerBlockEntity implements Basket
{
	private DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
	private int transferCooldown = -1;

	public BasketBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.BASKET.get(), pos, state);
	}

	@Override
	protected void readNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(compound, registries);
		this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.readLootTable(compound)) {
			Inventories.readNbt(compound, this.items, registries);
		}
		this.transferCooldown = compound.getInt("TransferCooldown");
	}

	@Override
	public void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(compound, registries);
		if (!this.writeLootTable(compound)) {
			Inventories.writeNbt(compound, this.items, registries);
		}

		compound.putInt("TransferCooldown", this.transferCooldown);
	}

	@Override
	public int size() {
		return this.items.size();
	}

	@Override
	public ItemStack removeStack(int index, int count) {
		this.generateLoot(null);
		return Inventories.splitStack(this.getHeldStacks(), index, count);
	}

	@Override
	public void setStack(int index, ItemStack stack) {
		this.generateLoot(null);
		this.getHeldStacks().set(index, stack);
		if (stack.getCount() > this.getMaxCountPerStack()) {
			stack.setCount(this.getMaxCountPerStack());
		}
	}

	@Override
	protected Text getContainerName() {
		return TextUtils.getTranslation("container.basket");
	}

	public static boolean pullItems(World level, Basket basket, int facingIndex) {
		for (ItemEntity itementity : getCaptureItems(level, basket, facingIndex)) {
			if (captureItem(basket, itementity)) {
				return true;
			}
		}
		return false;
	}

	public static ItemStack putStackInInventoryAllSlots(Inventory destination, ItemStack stack) {
		int i = destination.size();

		for (int j = 0; j < i && !stack.isEmpty(); ++j) {
			stack = insertStack(destination, stack, j);
		}

		return stack;
	}

	private static boolean canInsertItemInSlot(Inventory inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
		if (!inventoryIn.isValid(index, stack)) return false;
		return !(inventoryIn instanceof SidedInventory) || ((SidedInventory) inventoryIn).canInsert(index, stack, side);
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		return stack1.getCount() <= stack1.getMaxCount() && ItemStack.areItemsAndComponentsEqual(stack1, stack2);
	}

	private static ItemStack insertStack(Inventory destination, ItemStack stack, int index) {
		ItemStack itemstack = destination.getStack(index);
		if (canInsertItemInSlot(destination, stack, index, null)) {
			boolean flag = false;
			boolean isDestinationEmpty = destination.isEmpty();
			if (itemstack.isEmpty()) {
				destination.setStack(index, stack);
				stack = ItemStack.EMPTY;
				flag = true;
			} else if (canCombine(itemstack, stack)) {
				int i = stack.getMaxCount() - itemstack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.decrement(j);
				itemstack.increment(j);
				flag = j > 0;
			}

			if (flag) {
				if (isDestinationEmpty && destination instanceof BasketBlockEntity firstBasket) {
					if (!firstBasket.mayTransfer()) {
						int k = 0;
						firstBasket.setTransferCooldown(8 - k);
					}
				}

				destination.markDirty();
			}
		}

		return stack;
	}

	public static boolean captureItem(Inventory inventory, ItemEntity itemEntity) {
		boolean flag = false;
		ItemStack entityItemStack = itemEntity.getStack().copy();
		ItemStack remainderStack = putStackInInventoryAllSlots(inventory, entityItemStack);
		if (remainderStack.isEmpty()) {
			flag = true;
			itemEntity.discard();
		} else {
			itemEntity.setStack(remainderStack);
		}

		return flag;
	}

	public static List<ItemEntity> getCaptureItems(World level, Basket basket, int facingIndex) {
		return basket.getFacingCollectionArea(facingIndex).getBoundingBoxes().stream().flatMap((aabb) -> level.getEntitiesByClass(ItemEntity.class, aabb.offset(basket.getLevelX() - 0.5D, basket.getLevelY() - 0.5D, basket.getLevelZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
	}

	// -- STANDARD INVENTORY STUFF --
	@Override
	protected DefaultedList<ItemStack> getHeldStacks() {
		return this.items;
	}

	@Override
	protected void setHeldStacks(DefaultedList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	@Override
	protected ScreenHandler createScreenHandler(int id, PlayerInventory player) {
		return GenericContainerScreenHandler.createGeneric9x3(id, player, this);
	}

	public void setTransferCooldown(int ticks) {
		this.transferCooldown = ticks;
	}

	private boolean isOnTransferCooldown() {
		return this.transferCooldown > 0;
	}

	public boolean mayTransfer() {
		return this.transferCooldown > 8;
	}

	private void updateHopper(Supplier<Boolean> supplier) {
		if (this.world != null && !this.world.isClient) {
			if (!this.isOnTransferCooldown() && this.getCachedState().get(Properties.ENABLED)) {
				boolean flag = false;
				if (!this.isFull()) {
					flag = supplier.get();
				}

				if (flag) {
					this.setTransferCooldown(8);
					this.markDirty();
				}
			}
		}
	}

	private boolean isFull() {
		for (ItemStack itemstack : this.items) {
			if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxCount()) {
				return false;
			}
		}

		return true;
	}

	public void onEntityCollision(Entity entity) {
		if (entity instanceof ItemEntity) {
			BlockPos blockpos = this.getPos();
			int facing = this.getCachedState().get(BasketBlock.FACING).get3DDataValue();
			if (VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-blockpos.getX(), -blockpos.getY(), -blockpos.getZ())), this.getFacingCollectionArea(facing), BooleanBiFunction.AND)) {
				this.updateHopper(() -> captureItem(this, (ItemEntity) entity));
			}
		}
	}

	@Override
	public double getLevelX() {
		return (double) this.pos.getX() + 0.5D;
	}

	@Override
	public double getLevelY() {
		return (double) this.pos.getY() + 0.5D;
	}

	@Override
	public double getLevelZ() {
		return (double) this.pos.getZ() + 0.5D;
	}

	public static void pushItemsTick(World level, BlockPos pos, BlockState state, BasketBlockEntity blockEntity) {
		--blockEntity.transferCooldown;
		if (!blockEntity.isOnTransferCooldown()) {
			blockEntity.setTransferCooldown(0);
			int facing = state.get(BasketBlock.FACING).get3DDataValue();
			blockEntity.updateHopper(() -> pullItems(level, blockEntity, facing));
		}
	}
}
