package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
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
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PieBlock extends Block
{
	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	public static final IntProperty BITES = IntProperty.of("bites", 0, 3);

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);

	public final Supplier<Item> pieSlice;

	public PieBlock(Settings properties, Supplier<Item> pieSlice) {
		super(properties);
		this.pieSlice = pieSlice;
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(BITES, 0));
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(this.pieSlice.get());
	}

	public int getMaxBites() {
		return 4;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing());
	}

	@Override
	public ActionResult onUseWithItem(ItemStack heldStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (heldStack.isIn(ModTags.KNIVES)) {
			return cutSlice(level, pos, state, player);
		}

		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	protected ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hitResult) {
		if (level.isClient) {
			if (consumeBite(level, pos, state, player).isAccepted()) {
				return ActionResult.SUCCESS;
			}

			if (player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
				return ActionResult.CONSUME;
			}
		}

		return consumeBite(level, pos, state, player);
	}

	/**
	 * Eats a slice from the pie, feeding the player.
	 */
	protected ActionResult consumeBite(World level, BlockPos pos, BlockState state, PlayerEntity playerIn) {
		if (!playerIn.canConsume(false)) {
			return ActionResult.PASS;
		} else {
			ItemStack sliceStack = this.getPieSliceItem();
			FoodComponent sliceFood = sliceStack.get(DataComponentTypes.FOOD);

			if (sliceFood != null) {
				playerIn.getHungerManager().eat(sliceFood);
				/*for (FoodComponent.PossibleEffect effect : sliceFood.onConsume();) {
					if (!level.isClient && effect != null && level.random.nextFloat() < effect.probability()) {
						playerIn.addStatusEffect(effect.effect());
					}
				}*/
			}

			int bites = state.get(BITES);
			if (bites < getMaxBites() - 1) {
				level.setBlockState(pos, state.with(BITES, bites + 1), 3);
			} else {
				level.removeBlock(pos, false);
			}
			level.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT.value(), SoundCategory.PLAYERS, 0.8F, 0.8F);
			return ActionResult.SUCCESS;
		}
	}

	/**
	 * Cuts off a bite and drops a slice item, without feeding the player.
	 */
	protected ActionResult cutSlice(World level, BlockPos pos, BlockState state, PlayerEntity player) {
		int bites = state.get(BITES);
		if (bites < getMaxBites() - 1) {
			level.setBlockState(pos, state.with(BITES, bites + 1), 3);
		} else {
			level.removeBlock(pos, false);
		}

		Direction direction = player.getHorizontalFacing().getOpposite();
		ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
				direction.getOffsetX() * 0.15, 0.05, direction.getOffsetZ() * 0.15);
		level.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);
		return ActionResult.SUCCESS;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		return level.getBlockState(pos.down()).isSolid();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, BITES);
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
		return getMaxBites() - blockState.get(BITES);
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
