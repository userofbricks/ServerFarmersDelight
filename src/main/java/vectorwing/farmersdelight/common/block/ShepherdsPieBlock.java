package vectorwing.farmersdelight.common.block;

import java.util.function.Supplier;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ShepherdsPieBlock extends FeastBlock
{
	protected static final VoxelShape PLATE_SHAPE = createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
	protected static final VoxelShape PIE_SHAPE = VoxelShapes.combine(PLATE_SHAPE, createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 8.0D, 14.0D), BooleanBiFunction.OR);

	public ShepherdsPieBlock(Settings properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties, servingItem, hasLeftovers);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return state.get(SERVINGS) == 0 ? PLATE_SHAPE : PIE_SHAPE;
	}
}
