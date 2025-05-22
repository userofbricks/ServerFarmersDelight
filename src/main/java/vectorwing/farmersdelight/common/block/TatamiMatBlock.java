package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.Nullable;

;

@SuppressWarnings("deprecation")
public class TatamiMatBlock extends HorizontalFacingBlock
{
	public static final MapCodec<TatamiMatBlock> CODEC = createCodec(TatamiMatBlock::new);
	public static final EnumProperty<BedPart> PART = Properties.BED_PART;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

	public TatamiMatBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(PART, BedPart.FOOT));
	}

	@Override
	protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
		return CODEC;
	}

	private static Direction getDirectionToOther(BedPart part, Direction direction) {
		return part == BedPart.FOOT ? direction : direction.getOpposite();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HorizontalFacingBlock.FACING, PART);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (facing == getDirectionToOther(stateIn.get(PART), stateIn.get(HorizontalFacingBlock.FACING))) {
			return stateIn.canPlaceAt(level, currentPos) && facingState.isOf(this) && facingState.get(PART) != stateIn.get(PART) ? stateIn : Blocks.AIR.getDefaultState();
		} else {
			return !stateIn.canPlaceAt(level, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		return !level.isAir(pos.down());
	}

	@Override
	public BlockState onBreak(World level, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!level.isClient && player.isCreative()) {
			BedPart part = state.get(PART);
			if (part == BedPart.FOOT) {
				BlockPos pairPos = pos.offset(getDirectionToOther(part, state.get(HorizontalFacingBlock.FACING)));
				BlockState pairState = level.getBlockState(pairPos);
				if (pairState.getBlock() == this && pairState.get(PART) == BedPart.HEAD) {
					level.setBlockState(pairPos, Blocks.AIR.getDefaultState(), 35);
					level.syncWorldEvent(player, 2001, pairPos, Block.getRawIdFromState(pairState));
				}
			}
		}

		return super.onBreak(level, pos, state, player);
	}

	@Override
	public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(level, pos, state, placer, stack);
		if (!level.isClient) {
			BlockPos facingPos = pos.offset(state.get(HorizontalFacingBlock.FACING));
			level.setBlockState(facingPos, state.with(PART, BedPart.HEAD), 3);
			level.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbors(level, pos, 3);
		}
	}

	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		World level = context.getWorld();
		Direction facing = context.getHorizontalPlayerFacing();
		BlockPos pairPos = context.getBlockPos().offset(facing);
		BlockState pairState = context.getWorld().getBlockState(pairPos);
		if (pairState.canReplace(context) && canPlaceAt(pairState, level, pairPos)) {
			return this.getDefaultState().with(HorizontalFacingBlock.FACING, facing);
		}
		return null;
	}
}
