package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.registry.ModItems;

/**
 * A bush which grows, representing the earlier stage of another plant.
 * Once mature, a budding bush can "grow past" it, and turn into something different.
 */
@SuppressWarnings("deprecation")
public class BuddingBushBlock extends FDBushBlock
{
	public static final MapCodec<BuddingBushBlock> CODEC = createCodec(BuddingBushBlock::new);

	public static final int MAX_AGE = 3;
	public static final IntProperty AGE = Properties.AGE_7;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)};

	public BuddingBushBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
	}

	@Override
	public MapCodec<BuddingBushBlock> getCodec() {
		return CODEC;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_AGE[state.get(getAgeProperty())];
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return state.isOf(Blocks.FARMLAND);
	}

	public IntProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return MAX_AGE;
	}

	protected int getAge(BlockState state) {
		return state.get(getAgeProperty());
	}

	public BlockState getStateForAge(int age) {
		return getDefaultState().with(getAgeProperty(), age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.get(getAgeProperty()) >= getMaxAge();
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return canGrowPastMaxAge() || !isMaxAge(state);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		if (!level.isRegionLoaded(pos.add(-1, -1 , -1), pos.add(1, 1, 1))) return;
		if (level.getBaseLightLevel(pos, 0) >= 9) {
			int age = getAge(state);
			if (age <= getMaxAge()) {
				float growthSpeed = getGrowthSpeed(state, level, pos);
				if (random.nextInt((int) (25.0F / growthSpeed) + 1) == 0) {
					if (isMaxAge(state)) {
						growPastMaxAge(state, level, pos, random);
					} else {
						level.setBlockState(pos, getStateForAge(age + 1));
					}
				}
			}
		}
	}

	/**
	 * Determines if this bush should keep ticking at max age. If true, calls growPastMaxAge() on each growth success.
	 */
	public boolean canGrowPastMaxAge() {
		return false;
	}

	public void growPastMaxAge(BlockState state, ServerWorld level, BlockPos pos, Random random) {
	}

	protected static float getGrowthSpeed(BlockState state, BlockView level, BlockPos pos) {
		float speed = 1.0F;
		BlockPos posBelow = pos.down();

		for (int posX = -1; posX <= 1; ++posX) {
			for (int posZ = -1; posZ <= 1; ++posZ) {
				float speedBonus = 1.0F;
				BlockState stateBelow = level.getBlockState(posBelow.add(posX, 0, posZ));
				if (stateBelow.contains(FarmlandBlock.MOISTURE) && stateBelow.get(FarmlandBlock.MOISTURE) > 0) {
					speedBonus = 3.0F;
				}

				if (posX != 0 || posZ != 0) {
					speedBonus /= 4.0F;
				}

				speed += speedBonus;
			}
		}

		BlockPos posNorth = pos.north();
		BlockPos posSouth = pos.south();
		BlockPos posWest = pos.west();
		BlockPos posEast = pos.east();
		Block block = state.getBlock();
		boolean matchesEastWestRow = level.getBlockState(posWest).isOf(block) || level.getBlockState(posEast).isOf(block);
		boolean matchesNorthSouthRow = level.getBlockState(posNorth).isOf(block) || level.getBlockState(posSouth).isOf(block);
		if (matchesEastWestRow && matchesNorthSouthRow) {
			speed /= 2.0F;
		} else {
			boolean matchesDiagonalRows = level.getBlockState(posWest.north()).isOf(block) || level.getBlockState(posEast.north()).isOf(block) || level.getBlockState(posEast.south()).isOf(block) || level.getBlockState(posWest.south()).isOf(block);
			if (matchesDiagonalRows) {
				speed /= 2.0F;
			}
		}

		return speed;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		return (level.getBaseLightLevel(pos, 0) >= 8 || level.isSkyVisible(pos)) && super.canPlaceAt(state, level, pos);
	}

	protected ItemConvertible getBaseSeedId() {
		return ModItems.TOMATO_SEEDS.get();
	}

	@Override
	public ItemStack getPickStack(WorldView level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(getBaseSeedId());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
