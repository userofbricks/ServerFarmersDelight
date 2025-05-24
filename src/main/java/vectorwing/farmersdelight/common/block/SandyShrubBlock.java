package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.world.WildCropGeneration;

public class SandyShrubBlock extends FDBushBlock implements Fertilizable
{
	public static final MapCodec<SandyShrubBlock> CODEC = createCodec(SandyShrubBlock::new);

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

	public SandyShrubBlock(Settings properties) {
		super(properties);
	}

	@Override
	public MapCodec<SandyShrubBlock> getCodec() {
		return CODEC;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return state.isIn(BlockTags.SAND);
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World level, Random random, BlockPos pos, BlockState state) {
		return true;
	}
}
