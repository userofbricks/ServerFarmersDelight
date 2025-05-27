package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

@SuppressWarnings("deprecation")
public class RiceBlock extends FDBushBlock implements Fertilizable, FluidFillable
{
	public static final MapCodec<RiceBlock> CODEC = createCodec(RiceBlock::new);

	public static final IntProperty AGE = Properties.AGE_7;
	public static final BooleanProperty SUPPORTING = BooleanProperty.of("supporting");
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
			Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)};

	public RiceBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(AGE, 0).with(SUPPORTING, false));
	}

	@Override
	public MapCodec<RiceBlock> getCodec() {
		return CODEC;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		super.scheduledTick(state, level, pos, random);
		if (!level.isRegionLoaded(pos.add(-1, -1, -1), pos.add(1, 1, 1))) return;
		if (level.getBaseLightLevel(pos.up(), 0) >= 6) {
			int age = this.getAge(state);
			if (age <= this.getMaxAge()) {
				float chance = 10;
				if (random.nextInt((int) (25.0F / chance) + 1) == 0) {
					if (age == this.getMaxAge()) {
						RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
						if (riceUpper.getDefaultState().canPlaceAt(level, pos.up()) && level.isAir(pos.up())) {
							level.setBlockState(pos.up(), riceUpper.getDefaultState());
						}
					} else {
						level.setBlockState(pos, this.withAge(age + 1), 2);
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		FluidState fluid = level.getFluidState(pos);
		return super.canPlaceAt(state, level, pos) && fluid.isIn(FluidTags.WATER) && fluid.getLevel() == 8;
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return super.canPlantOnTop(state, level, pos) || state.isIn(BlockTags.DIRT);
	}

	public IntProperty getAgeProperty() {
		return AGE;
	}

	protected int getAge(BlockState state) {
		return state.get(this.getAgeProperty());
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public ItemStack getPickStack(WorldView level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(this.getAgeProperty(), age);
	}

//	public boolean isMaxAge(BlockState state) {
//		return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
//	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, SUPPORTING);
	}

	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState fluid = context.getWorld().getFluidState(context.getBlockPos());
		return fluid.isIn(FluidTags.WATER) && fluid.getLevel() == 8 ? super.getPlacementState(context) : null;
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		BlockState upperState = level.getBlockState(pos.up());
		if (upperState.getBlock() instanceof RicePaniclesBlock) {
			return !((RicePaniclesBlock) upperState.getBlock()).isMature(upperState);
		}
		return true;
	}

	@Override
	public boolean canGrow(World level, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(World level) {
		return MathHelper.nextInt(level.random, 1, 4);
	}

	@Override
	public void grow(ServerWorld level, Random rand, BlockPos pos, BlockState state) {
		int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
		if (ageGrowth <= this.getMaxAge()) {
			level.setBlockState(pos, state.with(AGE, ageGrowth));
		} else {
			BlockState top = level.getBlockState(pos.up());
			if (top.getBlock() == ModBlocks.RICE_CROP_PANICLES.get()) {
				Fertilizable growable = (Fertilizable) level.getBlockState(pos.up()).getBlock();
				if (growable.isFertilizable(level, pos.up(), top)) {
					growable.grow(level, level.random, pos.up(), top);
				}
			} else {
				RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
				int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
				if (riceUpper.getDefaultState().canPlaceAt(level, pos.up()) && level.isAir(pos.up())) {
					level.setBlockState(pos, state.with(AGE, this.getMaxAge()));
					level.setBlockState(pos.up(), riceUpper.getDefaultState().with(RicePaniclesBlock.RICE_AGE, remainingGrowth), 2);
				}
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Override
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return false;
	}
}
