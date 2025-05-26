package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.Optional;

public class StoveBlockEntity extends SyncedBlockEntity
{
	private static final VoxelShape GRILLING_AREA = Block.createCuboidShape(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private static final int INVENTORY_SLOT_COUNT = 6;

	private final ItemStackHandler inventory;
	private final int[] cookingTimes;
	private final int[] cookingTimesTotal;

	private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, CampfireCookingRecipe> quickCheck;

	public StoveBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.STOVE, pos, state);
		inventory = createHandler();
		cookingTimes = new int[INVENTORY_SLOT_COUNT];
		cookingTimesTotal = new int[INVENTORY_SLOT_COUNT];
		quickCheck = ServerRecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);
	}

	@Override
	public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(tag, registries);
		if (tag.contains("Inventory")) {
			inventory.deserializeNBT(registries, tag.getCompound("Inventory").orElseThrow());
		} else {
			inventory.deserializeNBT(registries, tag);
		}
		if (tag.contains("CookingTimes")) {
			int[] arrayCookingTimes = tag.getIntArray("CookingTimes").orElseThrow();
			System.arraycopy(arrayCookingTimes, 0, cookingTimes, 0, Math.min(cookingTimesTotal.length, arrayCookingTimes.length));
		}

		if (tag.contains("CookingTotalTimes")) {
			int[] arrayCookingTimesTotal = tag.getIntArray("CookingTotalTimes").orElseThrow();
			System.arraycopy(arrayCookingTimesTotal, 0, cookingTimesTotal, 0, Math.min(cookingTimesTotal.length, arrayCookingTimesTotal.length));
		}
	}

	@Override
	public void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		writeItems(compound, registries);
		compound.putIntArray("CookingTimes", cookingTimes);
		compound.putIntArray("CookingTotalTimes", cookingTimesTotal);
	}

	private NbtCompound writeItems(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(compound, registries);
		compound.put("Inventory", inventory.serializeNBT(registries));
		return compound;
	}

	public static void cookingTick(World level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
		boolean isStoveLit = state.get(StoveBlock.LIT);

		if (stove.isStoveBlockedAbove()) {
			if (!ItemUtils.isInventoryEmpty(stove.inventory)) {
				ItemUtils.dropItems(level, pos, stove.inventory);
				stove.inventoryChanged();
			}
		} else if (isStoveLit) {
			stove.cookAndOutputItems();
		} else {
			for (int i = 0; i < stove.inventory.getSlotCount(); ++i) {
				if (stove.cookingTimes[i] > 0) {
					stove.cookingTimes[i] = MathHelper.clamp(stove.cookingTimes[i] - 2, 0, stove.cookingTimesTotal[i]);
				}
			}
		}
	}

	public static void animationTick(World level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
		for (int i = 0; i < stove.inventory.getSlotCount(); ++i) {
			if (!stove.inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
				Vec2f stoveItemVector = stove.getStoveItemOffset(i);
				Direction direction = state.get(StoveBlock.FACING);
				int directionIndex = direction.getHorizontalQuarterTurns();
				Vec2f offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2f(stoveItemVector.y, stoveItemVector.x);

				double x = ((double) pos.getX() + 0.5D) - (direction.getOffsetX() * offset.x) + (direction.rotateYClockwise().getOffsetX() * offset.x);
				double y = (double) pos.getY() + 1.0D;
				double z = ((double) pos.getZ() + 0.5D) - (direction.getOffsetZ() * offset.y) + (direction.rotateYClockwise().getOffsetZ() * offset.y);

				for (int k = 0; k < 3; ++k) {
					level.addParticleClient(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
				}
			}
		}
	}

	private void cookAndOutputItems() {
		if (world == null) return;

		boolean didInventoryChange = false;
		for (int i = 0; i < inventory.getSlotCount(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				++cookingTimes[i];
				if (cookingTimes[i] >= cookingTimesTotal[i]) {
					Optional<RecipeEntry<CampfireCookingRecipe>> recipe = getMatchingRecipe(stoveStack);
					if (recipe.isPresent()) {
						ItemStack resultStack = recipe.get().value().craft(new SingleStackRecipeInput(stoveStack), world.getRegistryManager());
						if (!resultStack.isEmpty()) {ItemUtils.spawnItemEntity(world, resultStack.copy(),
									pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
									world.random.nextGaussian() * (double) 0.01F, 0.1F, world.random.nextGaussian() * (double) 0.01F);
						}
					}
					inventory.setStackInSlot(i, ItemStack.EMPTY);
					didInventoryChange = true;
				}
			}
		}

		if (didInventoryChange) {
			inventoryChanged();
		}
	}

	public int getNextEmptySlot() {
		for (int i = 0; i < inventory.getSlotCount(); ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public boolean addItem(ItemStack itemStackIn, RecipeEntry<CampfireCookingRecipe> recipe, int slot) {
		if (0 <= slot && slot < inventory.getSlotCount()) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				cookingTimesTotal[slot] = recipe.value().getCookingTime();
				cookingTimes[slot] = 0;
				inventory.setStackInSlot(slot, itemStackIn.split(1));
				inventoryChanged();
				return true;
			}
		}
		return false;
	}

	public Optional<RecipeEntry<CampfireCookingRecipe>> getMatchingRecipe(ItemStack stack) {
		if (world == null) return Optional.empty();
		return this.quickCheck.getFirstMatch(new SingleStackRecipeInput(stack), (ServerWorld) this.world);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public boolean isStoveBlockedAbove() {
		if (world != null) {
			BlockState above = world.getBlockState(pos.up());
			return VoxelShapes.matchesAnywhere(GRILLING_AREA, above.getOutlineShape(world, pos.up()), BooleanBiFunction.AND);
		}
		return false;
	}

	public Vec2f getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vec2f[] OFFSETS = {
				new Vec2f(X_OFFSET, Y_OFFSET),
				new Vec2f(0.0F, Y_OFFSET),
				new Vec2f(-X_OFFSET, Y_OFFSET),
				new Vec2f(X_OFFSET, -Y_OFFSET),
				new Vec2f(0.0F, -Y_OFFSET),
				new Vec2f(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
		return writeItems(new NbtCompound(), registries);
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SLOT_COUNT)
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}
}
