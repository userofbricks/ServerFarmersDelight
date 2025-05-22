package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RicePaniclesBlock extends CropBlock
{
	public static final IntProperty RICE_AGE = Properties.AGE_3;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
			Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)
	};

	public RicePaniclesBlock(Settings properties) {
		super(properties);
	}

	@Override
	public IntProperty getAgeProperty() {
		return RICE_AGE;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected ItemConvertible getSeedsItem() {
		return ModItems.RICE.get();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(RICE_AGE);
	}

	@Override
	protected int getGrowthAmount(World level) {
		return super.getGrowthAmount(level) / 3;
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return state.isOf(ModBlocks.RICE_CROP.get());
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		return (level.getBaseLightLevel(pos, 0) >= 8 || level.isSkyVisible(pos)) && this.canPlantOnTop(level.getBlockState(pos.down()), level, pos);
	}
}
