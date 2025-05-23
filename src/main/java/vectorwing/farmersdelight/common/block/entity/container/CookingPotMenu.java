package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;
import vectorwing.farmersdelight.refabricated.inventory.ItemHandlerSlot;

import java.util.Objects;

public class CookingPotMenu extends AbstractRecipeScreenHandler
{
	public static final Identifier EMPTY_CONTAINER_SLOT_BOWL = Identifier.of(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	public final CookingPotBlockEntity blockEntity;
	public final ItemStackHandler inventory;
	private final PropertyDelegate cookingPotData;
	private final ScreenHandlerContext canInteractWithCallable;
	protected final World level;

	public CookingPotMenu(final int windowId, final PlayerInventory playerInventory, final BlockPos data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data), new ArrayPropertyDelegate(4));
	}

	public CookingPotMenu(final int windowId, final PlayerInventory playerInventory, final CookingPotBlockEntity blockEntity, PropertyDelegate cookingPotDataIn) {
		super(ModMenuTypes.COOKING_POT.get(), windowId);
		this.blockEntity = blockEntity;
		this.inventory = blockEntity.getInventory();
		this.cookingPotData = cookingPotDataIn;
		this.level = playerInventory.player.getWorld();
		this.canInteractWithCallable = ScreenHandlerContext.create(blockEntity.getWorld(), blockEntity.getPos());

		// Ingredient Slots - 2 Rows x 3 Columns
		int startX = 8;
		int startY = 18;
		int inputStartX = 30;
		int inputStartY = 17;
		int borderSlotSize = 18;
		for (int row = 0; row < 2; ++row) {
			for (int column = 0; column < 3; ++column) {
				this.addSlot(new ItemHandlerSlot(inventory, (row * 3) + column,
						inputStartX + (column * borderSlotSize),
						inputStartY + (row * borderSlotSize)));
			}
		}

		// Meal Display
		this.addSlot(new CookingPotMealSlot(inventory, 6, 124, 26));

		// Bowl Input
		this.addSlot(new ItemHandlerSlot(inventory, 7, 92, 55)
		{
			public Identifier getBackgroundSprite() {
				return EMPTY_CONTAINER_SLOT_BOWL;
			}
		});

		// Bowl Output
		this.addSlot(new CookingPotResultSlot(playerInventory.player, blockEntity, inventory, 8, 124, 55));

		// Main Player Inventory
		int startPlayerInvY = startY * 4 + 12;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column, startX + (column * borderSlotSize),
						startPlayerInvY + (row * borderSlotSize)));
			}
		}

		// Hotbar
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), 142));
		}

		this.addProperties(cookingPotDataIn);
	}

	private static CookingPotBlockEntity getTileEntity(final PlayerInventory playerInventory, final BlockPos data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final BlockEntity tileAtPos = playerInventory.player.getWorld().getBlockEntity(data);
		if (tileAtPos instanceof CookingPotBlockEntity) {
			return (CookingPotBlockEntity) tileAtPos;
		}
		throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
	}

	@Override
	public boolean canUse(PlayerEntity playerIn) {
		return canUse(canInteractWithCallable, playerIn, ModBlocks.COOKING_POT.get());
	}

	@Override
	public ItemStack quickMove(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
	}

	public int getCookProgressionScaled() {
		int i = this.cookingPotData.get(0);
		int j = this.cookingPotData.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public boolean isHeated() {
		return blockEntity.isHeated();
	}

	//@Override
	public void fillCraftSlotsStackedContents(RecipeMatcher helper) {
		for (int i = 0; i < inventory.getSlotCount(); i++) {
			//helper.accountSimpleStack(inventory.getStackInSlot(i));
		}
	}

	//@Override
	public void clearCraftingContent() {
		for (int i = 0; i < 6; i++) {
			this.inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	//@Override
	public boolean recipeMatches(RecipeEntry<CookingPotRecipe> recipe) {
		return recipe.value().matches(new RecipeWrapper(inventory), level);
	}

	//@Override
	public int getResultSlotIndex() {
		return 7;
	}

	//@Override
	public int getSize() {
		return 7;
	}

	@Override
	public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
		return PostFillAction.NOTHING;
	}

	@Override
	public void populateRecipeFinder(RecipeFinder finder) {
		if (this.inventory instanceof RecipeInputProvider) {
			((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
		}
	}

	@Override
	public RecipeBookType getCategory() {
		return RecipeBookType.valueOf("FARMERSDELIGHT_COOKING");
	}
}
