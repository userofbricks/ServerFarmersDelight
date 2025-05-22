package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

;

@SuppressWarnings("deprecation")
public class TatamiBlock extends Block
{
	public static final DirectionProperty FACING = Properties.FACING;
	public static final BooleanProperty PAIRED = BooleanProperty.of("paired");

	public TatamiBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.DOWN).setValue(PAIRED, false));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction face = context.getSide();
		BlockPos targetPos = context.getBlockPos().offset(face.getOpposite());
		BlockState targetState = context.getWorld().getBlockState(targetPos);
		boolean pairing = false;

		if (context.getPlayer() != null && !context.getPlayer().isSneaking() && targetState.getBlock() == this && !targetState.get(PAIRED)) {
			pairing = true;
		}

		return this.getDefaultState().with(FACING, context.getSide().getOpposite()).setValue(PAIRED, pairing);
	}

	@Override
	public void onPlaced(World level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(level, pos, state, placer, stack);
		if (!level.isClient) {
			if (placer != null && placer.isSneaking()) {
				return;
			}
			BlockPos facingPos = pos.offset(state.get(FACING));
			BlockState facingState = level.getBlockState(facingPos);
			if (facingState.getBlock() == this && !facingState.get(PAIRED)) {
				level.setBlockState(facingPos, state.with(FACING, state.get(FACING).getOpposite()).setValue(PAIRED, true), 3);
				level.blockUpdated(pos, Blocks.AIR);
				state.updateNeighbors(level, pos, 3);
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess level, BlockPos currentPos, BlockPos facingPos) {
		if (facing.equals(stateIn.get(FACING)) && stateIn.get(PAIRED) && level.getBlockState(facingPos).getBlock() != this) {
			return stateIn.with(PAIRED, false);
		}
		return super.getStateForNeighborUpdate(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, PAIRED);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.get(FACING)));
	}
}
