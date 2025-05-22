package vectorwing.farmersdelight.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

@SuppressWarnings("deprecation")
public class OrganicCompostBlock extends Block
{
	public static IntProperty COMPOSTING = IntProperty.of("composting", 0, 7);

	public OrganicCompostBlock(Settings properties) {
		super(properties);
		this.setDefaultState(super.getDefaultState().with(COMPOSTING, 0));
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COMPOSTING);
		super.appendProperties(builder);
	}

	public int getMaxCompostingStage() {
		return 7;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		if (level.isClient) return;

		float chance = 0F;
		boolean hasWater = false;
		int maxLight = 0;

		for (BlockPos neighborPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
			BlockState neighborState = level.getBlockState(neighborPos);
			if (neighborState.isIn(ModTags.COMPOST_ACTIVATORS)) {
				chance += 0.02F;
			}
			if (neighborState.getFluidState().isIn(FluidTags.WATER)) {
				hasWater = true;
			}
			int light = level.getLightLevel(LightType.SKY, neighborPos.up());
			if (light > maxLight) {
				maxLight = light;
			}
		}

		chance += maxLight > 12 ? 0.1F : 0.05F;
		chance += hasWater ? 0.1F : 0.0F;

		if (level.getRandom().nextFloat() <= chance) {
			if (state.get(COMPOSTING) == this.getMaxCompostingStage())
				level.setBlockState(pos, ModBlocks.RICH_SOIL.get().getDefaultState(), 3); // finished
			else
				level.setBlockState(pos, state.with(COMPOSTING, state.get(COMPOSTING) + 1), 3); // next stage
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
		return (getMaxCompostingStage() + 1 - blockState.get(COMPOSTING));
	}

	@Override
	public void randomDisplayTick(BlockState state, World level, BlockPos pos, Random random) {
		super.randomDisplayTick(state, level, pos, random);
		if (random.nextInt(10) == 0) {
			level.addParticleClient(ParticleTypes.MYCELIUM, (double) pos.getX() + (double) random.nextFloat(), (double) pos.getY() + 1.1D, (double) pos.getZ() + (double) random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
}
