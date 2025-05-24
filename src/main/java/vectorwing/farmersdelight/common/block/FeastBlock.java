package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class FeastBlock extends Block
{
	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	public static final IntProperty SERVINGS = IntProperty.of("servings", 0, 4);

	public final Supplier<Item> servingItem;
	public final boolean hasLeftovers;

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
	};

	/**
	 * This block provides up to 4 servings of food to players who interact with it.
	 * If a leftover item is specified, the block lingers at 0 servings, and is destroyed on right-click.
	 *
	 * @param properties   Block properties.
	 * @param servingItem  The meal to be served.
	 * @param hasLeftovers Whether the block remains when out of servings. If false, the block vanishes once it runs out.
	 */
	public FeastBlock(Settings properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties);
		this.servingItem = servingItem;
		this.hasLeftovers = hasLeftovers;
		this.setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(getServingsProperty(), getMaxServings()));
	}

	public IntProperty getServingsProperty() {
		return SERVINGS;
	}

	public int getMaxServings() {
		return 4;
	}

	public ItemStack getServingItem(BlockState state) {
		return new ItemStack(this.servingItem.get());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPES[state.get(SERVINGS)];
	}

	@Override
	public ActionResult onUseWithItem(ItemStack heldStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (level.isClient) {
			if (this.takeServing(level, pos, state, player, hand).isAccepted()) {
				return ActionResult.SUCCESS;
			}
		}

		return this.takeServing(level, pos, state, player, hand);
	}

	protected ActionResult takeServing(WorldAccess level, BlockPos pos, BlockState state, PlayerEntity player, Hand hand) {
		int servings = state.get(getServingsProperty());

		if (servings == 0) {
			level.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
			level.breakBlock(pos, true);
			return ActionResult.SUCCESS;
		}

		ItemStack serving = this.getServingItem(state);
		ItemStack heldStack = player.getStackInHand(hand);

		if (servings > 0) {
			if (serving.getRecipeRemainder().isEmpty() || ItemStack.areItemsEqual(heldStack, serving.getRecipeRemainder())) {
				level.setBlockState(pos, state.with(getServingsProperty(), servings - 1), 3);
				if (!player.getAbilities().creativeMode && !serving.getRecipeRemainder().isEmpty()) {
					heldStack.decrement(1);
				}
				if (!player.getInventory().insertStack(serving)) {
					player.dropItem(serving, false);
				}
				if (level.getBlockState(pos).get(getServingsProperty()) == 0 && !this.hasLeftovers) {
					level.removeBlock(pos, false);
				}
				level.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), SoundCategory.BLOCKS, 1.0F, 1.0F);
				return ActionResult.SUCCESS;
			} else {
				player.sendMessage(TextUtils.getTranslation("block.feast.use_container", serving.getRecipeRemainder().getName()), true);
			}
		}
		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		return level.getBlockState(pos.down()).isSolid();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
		return blockState.get(getServingsProperty());
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}
}
