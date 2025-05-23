package vectorwing.farmersdelight.common.block.entity;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.List;
import java.util.Optional;

public class CuttingBoardBlockEntity extends SyncedBlockEntity
{
	private final ItemStackHandler inventory;
	private final ServerRecipeManager.MatchGetter<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
	private Identifier lastRecipeID;
	private boolean isItemCarvingBoard;

	public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.CUTTING_BOARD.get(), pos, state);
		inventory = createHandler();
		isItemCarvingBoard = false;
		quickCheck = ServerRecipeManager.createCachedMatchGetter(ModRecipeTypes.CUTTING.get());
	}

    public static void init() {
        ItemStorage.SIDED.registerForBlockEntity(CuttingBoardBlockEntity::getStorage, ModBlockEntityTypes.CUTTING_BOARD.get());
    }

    @NotNull
    public Storage<ItemVariant> getStorage(@Nullable Direction side) {
        return getInventory();
    }

	@Override
	public void readNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(compound, registries);
		isItemCarvingBoard = compound.getBoolean("IsItemCarved").orElseThrow();
		inventory.deserializeNBT(registries, compound.getCompound("Inventory").orElseThrow());
	}

	@Override
	public void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(compound, registries);
		compound.put("Inventory", inventory.serializeNBT(registries));
		compound.putBoolean("IsItemCarved", isItemCarvingBoard);
	}

	public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable PlayerEntity player) {
		if (world == null) return false;

		if (isItemCarvingBoard) return false;

		Optional<RecipeEntry<CuttingBoardRecipe>> matchingRecipe = getMatchingRecipe(toolStack, player);

		matchingRecipe.ifPresent(recipe -> {
			List<ItemStack> results = recipe.value().rollResults(world.random, EnchantmentHelper.getLevel(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), toolStack));
			for (ItemStack resultStack : results) {
				Direction direction = getCachedState().get(CuttingBoardBlock.FACING).rotateYCounterclockwise();
				ItemUtils.spawnItemEntity(world, resultStack.copy(),
						pos.getX() + 0.5 + (direction.getOffsetX() * 0.2), pos.getY() + 0.2, pos.getZ() + 0.5 + (direction.getOffsetZ() * 0.2),
						direction.getOffsetX() * 0.2F, 0.0F, direction.getOffsetZ() * 0.2F);
			}
			if (!world.isClient) {
				toolStack.damage(1, (ServerWorld) world, (ServerPlayerEntity) player, (item) -> {
				});
			}

			playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, getStoredItem());
			removeItem();
			if (player instanceof ServerPlayerEntity) {
				ModAdvancements.USE_CUTTING_BOARD.get().trigger((ServerPlayerEntity) player);
			}
		});

		return matchingRecipe.isPresent();
	}

	private Optional<RecipeEntry<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable PlayerEntity player) {
		if (world == null) return Optional.empty();

		Optional<RecipeEntry<CuttingBoardRecipe>> recipe = quickCheck.getFirstMatch(new CuttingBoardRecipeInput(getStoredItem(), toolStack), (ServerWorld) world);
		if (recipe.isPresent()) {
			if (recipe.get().value().getTool().test(toolStack)) {
				return recipe;
			} else if (player != null) {
				player.sendMessage(TextUtils.getTranslation("block.cutting_board.invalid_tool"), true);
			}
		} else if (player != null) {
			player.sendMessage(TextUtils.getTranslation("block.cutting_board.invalid_item"), true);
		}

		return Optional.empty();
	}

	public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
		if (sound != null) {
			playSound(sound, 1.0F, 1.0F);
		} else if (tool.isIn(ConventionalItemTags.SHEAR_TOOLS)) {
			playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
		} else if (tool.isIn(CommonTags.TOOLS_KNIFE)) {
			playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
		} else if (boardItem.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			BlockSoundGroup soundType = block.getDefaultState().getSoundGroup();
			playSound(soundType.getBreakSound(), 1.0F, 0.8F);
		} else {
			playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 0.8F);
		}
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		if (world != null)
			world.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, sound, SoundCategory.BLOCKS, volume, pitch);
	}

	public boolean addItem(ItemStack itemStack) {
		if (isEmpty() && !itemStack.isEmpty()) {
			inventory.setStackInSlot(0, itemStack.split(1));
			isItemCarvingBoard = false;
			inventoryChanged();
			return true;
		}
		return false;
	}

	public boolean carveToolOnBoard(ItemStack tool) {
		if (addItem(tool)) {
			isItemCarvingBoard = true;
			return true;
		}
		return false;
	}

	public ItemStack removeItem() {
		if (!isEmpty()) {
			isItemCarvingBoard = false;
			ItemStack item = getStoredItem().split(1);
			inventory.commitModifiedStacks();
//			inventoryChanged();
			return item;
		}
		return ItemStack.EMPTY;
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredItem() {
		return inventory.getStackInSlot(0);
	}

	public boolean isEmpty() {
		return inventory.getStackInSlot(0).isEmpty();
	}

	public boolean isItemCarvingBoard() {
		return isItemCarvingBoard;
	}

	@Override
	public void markRemoved() {
		super.markRemoved();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}
}