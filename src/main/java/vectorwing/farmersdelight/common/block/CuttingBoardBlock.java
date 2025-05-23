package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.TridentItem;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("deprecation")
public class CuttingBoardBlock extends BlockWithEntity implements Waterloggable
{
	public static final MapCodec<CuttingBoardBlock> CODEC = createCodec(CuttingBoardBlock::new);

	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);

	public CuttingBoardBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	public static void init() {
		UseBlockCallback.EVENT.register(ToolCarvingEvent::onSneakPlaceTool);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return null;
	}

	@Override
	public BlockRenderType getRenderType(BlockState pState) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof CuttingBoardBlockEntity cuttingBoardEntity) {
			ItemStack heldStack = player.getStackInHand(hand);
			ItemStack offhandStack = player.getOffHandStack();

			if (cuttingBoardEntity.isEmpty()) {
				if (!offhandStack.isEmpty()) {
					if (hand.equals(Hand.MAIN_HAND) && !offhandStack.isIn(ModTags.OFFHAND_EQUIPMENT) && !(heldStack.getItem() instanceof BlockItem)) {
						return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION; // Pass to off-hand if that item is placeable
					}
					if (hand.equals(Hand.OFF_HAND) && offhandStack.isIn(ModTags.OFFHAND_EQUIPMENT)) {
						return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION; // Items in this tag should not be placed from the off-hand
					}
				}
				if (heldStack.isEmpty()) {
					return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
				} else if (cuttingBoardEntity.addItem(player.getAbilities().creativeMode ? heldStack.copy() : heldStack)) {
					level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
					return ItemInteractionResult.SUCCESS;
				}

			} else if (!heldStack.isEmpty()) {
				ItemStack boardStack = cuttingBoardEntity.getStoredItem().copy();
				if (cuttingBoardEntity.processStoredItemUsingTool(heldStack, player)) {
					spawnCuttingParticles(level, pos, boardStack, 5);
					return ItemInteractionResult.SUCCESS;
				}
				return ItemInteractionResult.CONSUME;

			} else if (hand.equals(Hand.MAIN_HAND)) {
				if (!player.isCreative()) {
					if (!player.getInventory().insertStack(cuttingBoardEntity.removeItem())) {
						ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(), cuttingBoardEntity.removeItem());
					}
				} else {
					cuttingBoardEntity.removeItem();
				}
				level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 0.25F, 0.5F);
				return ItemInteractionResult.SUCCESS;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) {
			return;
		}

		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof CuttingBoardBlockEntity cuttingBoard) {
			ItemScatterer.spawn(level, pos.getX(), pos.getY(), pos.getZ(), cuttingBoard.getStoredItem());
			level.updateComparators(pos, this);
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public boolean canMobSpawnInside(BlockState state) {
		return true;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
		return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite())
				.setValue(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			level.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(level));
		}
		return facing == Direction.DOWN && !stateIn.canPlaceAt(level, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		BlockPos floorPos = pos.down();
		return hasTopRim(level, floorPos) || sideCoversSmallSquare(level, floorPos, Direction.UP);
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof CuttingBoardBlockEntity) {
			return !((CuttingBoardBlockEntity) blockEntity).isEmpty() ? 15 : 0;
		}
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.CUTTING_BOARD.get().instantiate(pos, state);
	}

	@Override
	public BlockState rotate(BlockState pState, BlockRotation pRot) {
		return pState.with(FACING, pRot.rotate(pState.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, BlockMirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.get(FACING)));
	}

	public static void spawnCuttingParticles(World level, BlockPos pos, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vec3d vec3d = new Vec3d(((double) level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) level.random.nextFloat() - 0.5D) * 0.1D);
			if (level instanceof ServerWorld) {
				((ServerWorld) level).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
			} else {
				level.addParticleClient(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
			}
		}
	}

	public static class ToolCarvingEvent
	{
		public static ActionResult onSneakPlaceTool(PlayerEntity player, World level, Hand hand, BlockHitResult hit) {
			if (player.isSpectator())
				return ActionResult.PASS;

			BlockPos pos = hit.getBlockPos();
			ItemStack heldStack = player.getMainHandStack();
			BlockEntity tileEntity = level.getBlockEntity(pos);

			if (player.shouldCancelInteraction() && !heldStack.isEmpty() && tileEntity instanceof CuttingBoardBlockEntity) {
				if (heldStack.getItem() instanceof TieredItem ||
						heldStack.getItem() instanceof TridentItem ||
						heldStack.getItem() instanceof ShearsItem) {
					boolean success = ((CuttingBoardBlockEntity) tileEntity).carveToolOnBoard(player.getAbilities().creativeMode ? heldStack.copy() : heldStack);
					if (success) {
						level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
						return ActionResult.SUCCESS;
					}
				}
			}
			return ActionResult.PASS;
		}
	}
}
