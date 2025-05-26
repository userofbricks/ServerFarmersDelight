package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Optional;

public class SkilletBlock extends BlockWithEntity implements Waterloggable
{
	public static final MapCodec<SkilletBlock> CODEC = createCodec(SkilletBlock::new);

	public static final int MINIMUM_COOKING_TIME = 60;

	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty SUPPORT = BooleanProperty.of("support");
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = VoxelShapes.union(SHAPE, Block.createCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public SkilletBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SUPPORT, false).with(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof SkilletBlockEntity skilletEntity) {
			if (!level.isClient) {
				ItemStack heldStack = player.getStackInHand(hand);
				EquipmentSlot heldSlot = hand.equals(Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
				if (heldStack.isEmpty()) {
					ItemStack extractedStack = skilletEntity.removeItem();
					if (!player.isCreative()) {
						player.equipStack(heldSlot, extractedStack);
					}
					return ActionResult.SUCCESS;
				} else {
					ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
					if (remainderStack.getCount() != heldStack.getCount()) {
						if (!player.isCreative()) {
							player.equipStack(heldSlot, remainderStack);
						}
						level.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.CONSUME;
		}
		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	@Override
	public BlockRenderType getRenderType(BlockState pState) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.getBlock() != world.getBlockState(pos).getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof SkilletBlockEntity) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((SkilletBlockEntity) tileEntity).getInventory().getStackInSlot(0));
			}

			return super.onBreak(world, pos, state, player);
		}
		return super.onBreak(world, pos, state, player);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return state.get(SUPPORT).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World level = context.getWorld();
		FluidState fluid = level.getFluidState(context.getBlockPos());

		return this.getDefaultState()
				.with(FACING, context.getHorizontalPlayerFacing())
				.with(WATERLOGGED, fluid.getFluid() == Fluids.WATER)
				.with(SUPPORT, getTrayState(context.getWorld(), context.getBlockPos()));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (direction.getAxis().equals(Direction.Axis.Y)) {
			return state.with(SUPPORT, getTrayState(world, pos));
		}
		return state;
	}

	@Override
	public ItemStack getPickStack(WorldView level, BlockPos pos, BlockState state, boolean allData) {
		if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skillet) {
			return skillet.getSkilletAsItem();
		}

		return super.getPickStack(level, pos, state, allData);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, SUPPORT, WATERLOGGED);
	}

	@Override
	public void randomDisplayTick(BlockState stateIn, World level, BlockPos pos, Random rand) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof SkilletBlockEntity skilletEntity) {
			if (skilletEntity.isCooking()) {
				double x = (double) pos.getX() + 0.5D;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5D;
				if (rand.nextInt(10) == 0) {
					level.playSoundClient(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
				}
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.SKILLET.instantiate(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntity) {
		if (level.isClient) {
			return validateTicker(blockEntity, ModBlockEntityTypes.SKILLET, SkilletBlockEntity::animationTick);
		} else {
			return validateTicker(blockEntity, ModBlockEntityTypes.SKILLET, SkilletBlockEntity::cookingTick);
		}
	}

	private boolean getTrayState(WorldView world, BlockPos pos) {
		return world.getBlockState(pos.down()).isIn(ModTags.TRAY_HEAT_SOURCES);
	}

	/**
	 * Calculates the total cooking time for the Skillet, affected by Fire Aspect.
	 * Assuming a default of 30 seconds (600 ticks), the time is divided by 5, then reduced further per level of Fire Aspect, to a minimum of 3 seconds.
	 * Times are always rounded to a multiple of 20, to ensure exact seconds.
	 */
	public static int getSkilletCookingTime(int originalCookingTime, int fireAspectLevel) {
		int cookingTime = originalCookingTime > 0 ? originalCookingTime : 600;
		int cookingSeconds = cookingTime / 20;
		float cookingTimeReduction = 0.2F;

		if (fireAspectLevel > 0) {
			cookingTimeReduction -= fireAspectLevel * 0.05;
		}

		int result = (int) (cookingSeconds * cookingTimeReduction) * 20;

		return MathHelper.clamp(result, MINIMUM_COOKING_TIME, originalCookingTime);
	}
}
