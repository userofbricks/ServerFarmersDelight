package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;


@SuppressWarnings("deprecation")
public class TomatoVineBlock extends CropBlock
{
	public static final IntProperty VINE_AGE = Properties.AGE_7;
	public static final BooleanProperty ROPELOGGED = BooleanProperty.of("ropelogged");
	private static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public TomatoVineBlock(Settings properties) {
		super(properties);
		setDefaultState(stateManager.getDefaultState().with(getAgeProperty(), 0).with(ROPELOGGED, false));
	}

	protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		int age = state.get(getAgeProperty());
		boolean isMature = age == getMaxAge();
		return !isMature && stack.isOf(Items.BONE_MEAL) ? ActionResult.CONSUME : super.onUseWithItem(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		int age = state.get(getAgeProperty());
		boolean isMature = age == getMaxAge();
		if (isMature) {
			int quantity = 1 + level.random.nextInt(2);
			dropStack(level, pos, new ItemStack(ModItems.TOMATO.get(), quantity));

			if (level.random.nextFloat() < 0.05) {
				dropStack(level, pos, new ItemStack(ModItems.ROTTEN_TOMATO.get()));
			}

			level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundCategory.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
			level.setBlockState(pos, state.with(getAgeProperty(), 0), 2);
			return ActionResult.SUCCESS;
		} else {
			return super.onUse(state, level, pos, player, hit);
		}
	}

	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		if (!level.isRegionLoaded(pos.add(-1, -1 , -1), pos.add(1, 1, 1))) return;
		if (level.getBaseLightLevel(pos, 0) >= 9) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				float speed = getAvailableMoisture(state.getBlock(), level, pos);
				if (random.nextInt((int) (25.0F / speed) + 1) == 0) {
					level.setBlockState(pos, state.with(getAgeProperty(), age + 1), 2);
				}
			}
			attemptRopeClimb(level, pos, random);
		}
	}

	public void attemptRopeClimb(ServerWorld level, BlockPos pos, Random random) {
		if (random.nextFloat() < 0.3F) {
			BlockPos posAbove = pos.up();
			BlockState stateAbove = level.getBlockState(posAbove);
			boolean canClimb = stateAbove.isOf(ModBlocks.ROPE.get());
			if (canClimb) {
				int vineHeight;
				for (vineHeight = 1; level.getBlockState(pos.down(vineHeight)).isOf(this); ++vineHeight) {
				}
				if (vineHeight < 3) {
					level.setBlockState(posAbove, getDefaultState().with(ROPELOGGED, true));
				}
			}
		}

	}

	@Override
	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), age);
	}

	@Override
	public IntProperty getAgeProperty() {
		return VINE_AGE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return ModItems.TOMATO_SEEDS.get();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(VINE_AGE, ROPELOGGED);
	}

	@Override
	protected int getGrowthAmount(World level) {
		return super.getGrowthAmount(level) / 2;
	}

	@Override
	public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
		int newAge = this.getAge(state) + this.getGrowthAmount(level);
		int maxAge = this.getMaxAge();
		if (newAge > maxAge) {
			newAge = maxAge;
		}

		level.setBlockState(pos, state.with(getAgeProperty(), newAge));
		attemptRopeClimb(level, pos, random);
	}

	public boolean isLadder(BlockState state, WorldView level, BlockPos pos, LivingEntity entity) {
		return state.get(ROPELOGGED) && state.isIn(BlockTags.CLIMBABLE);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		BlockPos belowPos = pos.down();
		BlockState belowState = level.getBlockState(belowPos);

		if (state.get(TomatoVineBlock.ROPELOGGED)) {
			return belowState.isOf(ModBlocks.TOMATO_CROP.get()) && hasGoodCropConditions(level, pos);
		}

		return super.canPlaceAt(state, level, pos);
	}

	public boolean hasGoodCropConditions(WorldView level, BlockPos pos) {
		return level.getBaseLightLevel(pos, 0) >= 8 || level.isSkyVisible(pos);
	}

	@Override
	public void afterBreak(World level, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		boolean isRopelogged = state.get(TomatoVineBlock.ROPELOGGED);
		super.afterBreak(level, player, pos, state, blockEntity, stack);

		if (isRopelogged) {
			destroyAndPlaceRope(level, pos);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			tickView.scheduleBlockTick(pos, this, 1);
		}

		return state;
	}

	public static void destroyAndPlaceRope(World level, BlockPos pos) {
		Block configuredRopeBlock = ModBlocks.ROPE.get();
		Block finalRopeBlock = configuredRopeBlock != null ? configuredRopeBlock : ModBlocks.ROPE.get();
		level.setBlockState(pos, finalRopeBlock.getDefaultState());
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		if (!state.canPlaceAt(level, pos)) {
			level.breakBlock(pos, true);
			if (state.get(TomatoVineBlock.ROPELOGGED)) {
				destroyAndPlaceRope(level, pos);
			}
		}
	}
}
