package vectorwing.farmersdelight.common.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

@SuppressWarnings("deprecation")
public class RiceBaleBlock extends Block
{
	public static final DirectionProperty FACING = Properties.FACING;

	public RiceBaleBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.UP));
		FlammableBlockRegistry.getDefaultInstance().add(this, this.getFlammability(null, null, null, null), this.getFireSpreadSpeed(null, null, null, null));
	}

	@Override
	public void fallOn(World level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.handleFallDamage(fallDistance, 0.2F, level.getDamageSources().fall());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getSide());
	}

	public int getFireSpreadSpeed(BlockState state, BlockView world, BlockPos pos, Direction face) {
		return 60;
	}

	public int getFlammability(BlockState state, BlockView world, BlockPos pos, Direction face) {
		return 20;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
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
