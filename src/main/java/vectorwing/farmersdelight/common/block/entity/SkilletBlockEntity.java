package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.Optional;

public class SkilletBlockEntity extends SyncedBlockEntity implements HeatableBlockEntity
{
	private final ItemStackHandler inventory = createHandler();
	private int cookingTime;
	private int cookingTimeTotal;

	private ItemStack skilletStack;
	private int fireAspectLevel;

	private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, CampfireCookingRecipe> quickCheck;

	public SkilletBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.SKILLET.get(), pos, state);
		skilletStack = new ItemStack(ModItems.SKILLET.get());
		quickCheck = ServerRecipeManager.createCachedMatchGetter(RecipeType.CAMPFIRE_COOKING);
	}

	public static void cookingTick(World level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		boolean isHeated = skillet.isHeated(level, pos);

		if (state.get(SkilletBlock.WATERLOGGED)) {
			if (!ItemUtils.isInventoryEmpty(skillet.inventory)) {
				ItemUtils.dropItems(level, pos, skillet.inventory);
				skillet.inventoryChanged();
			}
		} else if (isHeated) {
			ItemStack cookingStack = skillet.getStoredStack();
			if (cookingStack.isEmpty()) {
				skillet.cookingTime = 0;
			} else {
				skillet.cookAndOutputItems(cookingStack, level);
			}
		} else if (skillet.cookingTime > 0) {
			skillet.cookingTime = MathHelper.clamp(skillet.cookingTime - 2, 0, skillet.cookingTimeTotal);
		}
	}

	public static void animationTick(World level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		if (skillet.isHeated(level, pos) && skillet.hasStoredStack()) {
			Random random = level.random;
			if (random.nextFloat() < 0.2F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionY = random.nextBoolean() ? 0.015D : 0.005D;
				level.addParticleClient(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
			}
			if (skillet.fireAspectLevel > 0 && random.nextFloat() < skillet.fireAspectLevel * 0.05F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionX = level.random.nextFloat() - 0.5F;
				double motionY = level.random.nextFloat() * 0.5F + 0.2f;
				double motionZ = level.random.nextFloat() - 0.5F;
				level.addParticleClient(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
			}
		}

	}

	private void cookAndOutputItems(ItemStack cookingStack, World level) {
		++cookingTime;
		if (cookingTime >= cookingTimeTotal) {
			Optional<RecipeEntry<CampfireCookingRecipe>> recipe = getMatchingRecipe(cookingStack);
			if (recipe.isPresent()) {
				ItemStack resultStack = recipe.get().value().craft(new SingleStackRecipeInput(cookingStack), level.getRegistryManager());
				Direction direction = getCachedState().get(SkilletBlock.FACING).getClockWise();
				ItemUtils.spawnItemEntity(level, resultStack.copy(),
						pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
						direction.getOffsetX() * 0.08F, 0.25F, direction.getOffsetZ() * 0.08F);

				cookingTime = 0;
				inventory.removeItem(0, 1, false);
			}
		}
	}

	public boolean isCooking() {
		return isHeated() && hasStoredStack();
	}

	public boolean isHeated() {
		if (world != null) {
			return isHeated(world, pos);
		}
		return false;
	}

	private Optional<RecipeEntry<CampfireCookingRecipe>> getMatchingRecipe(ItemStack stack) {
		if (world == null) return Optional.empty();
		return this.quickCheck.getFirstMatch(new SingleStackRecipeInput(stack), this.world);
	}

	@Override
	public void readNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.readNbt(compound, registries);
		inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
		cookingTime = compound.getInt("CookTime");
		cookingTimeTotal = compound.getInt("CookTimeTotal");
		skilletStack = ItemStack.parseOptional(registries, compound.getCompound("Skillet"));
		fireAspectLevel = EnchantmentHelper.getLevel(registries.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FIRE_ASPECT), skilletStack);
	}

	@Override
	public void writeNbt(NbtCompound compound, RegistryWrapper.WrapperLookup registries) {
		super.writeNbt(compound, registries);
		compound.put("Inventory", inventory.serializeNBT(registries));
		compound.putInt("CookTime", cookingTime);
		compound.putInt("CookTimeTotal", cookingTimeTotal);
		if (!skilletStack.isEmpty()) {
			compound.put("Skillet", skilletStack.toNbt(registries));
		}
	}

	public ItemStack getSkilletAsItem() {
		return skilletStack;
	}

	public void setSkilletItem(ItemStack stack) {
		skilletStack = stack.copy();
		fireAspectLevel = EnchantmentHelper.getLevel(world.getRegistryManager().registryOrThrow(RegistryKeys.ENCHANTMENT).getHolderOrThrow(Enchantments.FIRE_ASPECT), stack);
		inventoryChanged();
	}

	public ItemStack addItemToCook(ItemStack addedStack, PlayerEntity player) {
		Optional<RecipeEntry<CampfireCookingRecipe>> recipe = getMatchingRecipe(addedStack);
		if (recipe.isPresent() && getStoredStack().isEmpty()) {
			if (getCachedState().get(SkilletBlock.WATERLOGGED)) {
				player.sendMessage(TextUtils.getTranslation("block.skillet.underwater"), true);
				return addedStack;
			}
			boolean wasEmpty = getStoredStack().isEmpty();
			ItemStack remainderStack = inventory.insertItem(0, addedStack.copy(), false);
			if (!ItemStack.areEqual(remainderStack, addedStack)) {
				cookingTimeTotal = SkilletBlock.getSkilletCookingTime(recipe.get().value().getCookingTime(), fireAspectLevel);
				cookingTime = 0;
				if (wasEmpty && world != null && isHeated(world, pos)) {
					world.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
				}
				return remainderStack;
			}
		} else {
			player.sendMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		return inventory.removeItem(0, getStoredStack().getMaxCount(), false);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredStack() {
		return inventory.getStackInSlot(0);
	}

	public boolean hasStoredStack() {
		return !getStoredStack().isEmpty();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	@Override
	public void markRemoved() {
		super.markRemoved();
	}
}
