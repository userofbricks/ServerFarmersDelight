package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.UnknownNullability;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CabinetBlockEntity extends LootableContainerBlockEntity
{
	private DefaultedList<ItemStack> contents = DefaultedList.ofSize(27, ItemStack.EMPTY);
	private ViewerCountManager openersCounter = new ViewerCountManager()
	{
		protected void onContainerOpen(World level, BlockPos pos, BlockState state) {
			CabinetBlockEntity.this.playSound(state, ModSounds.BLOCK_CABINET_OPEN.get());
			CabinetBlockEntity.this.updateBlockState(state, true);
		}

		protected void onContainerClose(World level, BlockPos pos, BlockState state) {
			CabinetBlockEntity.this.playSound(state, ModSounds.BLOCK_CABINET_CLOSE.get());
			CabinetBlockEntity.this.updateBlockState(state, false);
		}

		protected void onViewerCountUpdate(World level, BlockPos pos, BlockState sta, int arg1, int arg2) {
		}

		protected boolean isPlayerViewing(PlayerEntity p_155060_) {
			if (p_155060_.currentScreenHandler instanceof GenericContainerScreenHandler) {
				Inventory container = ((GenericContainerScreenHandler) p_155060_.currentScreenHandler).getInventory();
				return container == CabinetBlockEntity.this;
			} else {
				return false;
			}
		}
	};

	public CabinetBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.CABINET, pos, state);
	}

	@Override
	public void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(compound, registries);
		if (!writeLootTable(compound)) {
			Inventories.writeNbt(compound, contents, registries);
		}
	}

	@Override
	public void readNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(compound, registries);
		contents = DefaultedList.ofSize(size(), ItemStack.EMPTY);
		if (!readLootTable(compound)) {
			Inventories.readNbt(compound, contents, registries);
		}
	}

	@Override
	public int size() {
		return 27;
	}

	@Override
	protected DefaultedList<ItemStack> getHeldStacks() {
		return contents;
	}

	@Override
	protected void setHeldStacks(DefaultedList<ItemStack> itemsIn) {
		contents = itemsIn;
	}

	@Override
	protected Text getContainerName() {
		return TextUtils.getTranslation("container.cabinet");
	}

	@Override
	protected ScreenHandler createScreenHandler(int id, PlayerInventory player) {
		return GenericContainerScreenHandler.createGeneric9x3(id, player, this);
	}

	public void onOpen(PlayerEntity pPlayer) {
		if (world != null && !this.removed && !pPlayer.isSpectator()) {
			this.openersCounter.openContainer(pPlayer, world, this.getPos(), this.getCachedState());
		}
	}

	public void onClose(PlayerEntity pPlayer) {
		if (world != null && !this.removed && !pPlayer.isSpectator()) {
			this.openersCounter.closeContainer(pPlayer, world, this.getPos(), this.getCachedState());
		}
	}

	public void recheckOpen() {
		if (world != null && !this.removed) {
			this.openersCounter.updateViewerCount(world, this.getPos(), this.getCachedState());
		}
	}

	void updateBlockState(BlockState state, boolean open) {
		if (world != null) {
			this.world.setBlockState(this.getPos(), state.with(CabinetBlock.OPEN, open), 3);
		}
	}

	private void playSound(BlockState state, SoundEvent sound) {
		if (world == null) return;

		Vec3i cabinetFacingVector = state.get(CabinetBlock.FACING).getVector();
		double x = (double) pos.getX() + 0.5D + (double) cabinetFacingVector.getX() / 2.0D;
		double y = (double) pos.getY() + 0.5D + (double) cabinetFacingVector.getY() / 2.0D;
		double z = (double) pos.getZ() + 0.5D + (double) cabinetFacingVector.getZ() / 2.0D;
		world.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}
}
