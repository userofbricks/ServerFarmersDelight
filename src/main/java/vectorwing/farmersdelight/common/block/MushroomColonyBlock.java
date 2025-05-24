package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.SoilUtils;

public class MushroomColonyBlock extends FDBushBlock implements Fertilizable
{
	public static final MapCodec<MushroomColonyBlock> CODEC = RecordCodecBuilder.mapCodec(
			builder -> builder.group(Registries.ITEM.getEntryCodec().fieldOf("mushroom").forGetter(block -> block.mushroomType), createSettingsCodec())
					.apply(builder, MushroomColonyBlock::new)
	);

	public static final int PLACING_LIGHT_LEVEL = 13;
	public final RegistryEntry<Item> mushroomType;

	public static final IntProperty COLONY_AGE = Properties.AGE_3;
	protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D),
			Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
	};

	public MushroomColonyBlock(RegistryEntry<Item> mushroomType, Settings properties) {
		super(properties);
		this.mushroomType = mushroomType;
		this.setDefaultState(this.stateManager.getDefaultState().with(COLONY_AGE, 0));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
		return SHAPE_BY_AGE[state.get(getAgeProperty())];
	}

	public IntProperty getAgeProperty() {
		return COLONY_AGE;
	}

	@Override
	public MapCodec<MushroomColonyBlock> getCodec() {
		return CODEC;
	}

	@Override
	public boolean canPlantOnTop(BlockState state, BlockView level, BlockPos pos) {
		return state.isOpaqueFullCube();
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView level, BlockPos pos) {
		BlockPos floorPos = pos.down();
		BlockState floorState = level.getBlockState(floorPos);
		if (floorState.isIn(BlockTags.MUSHROOM_GROW_BLOCK)) {
			return true;
		} else if (state.isOf(this) && floorState.getBlock() instanceof RichSoilBlock) {
			return SoilUtils.isAbleToPlaceRichSoil(this);
		} else if (state.isOf(this) && floorState.getBlock() instanceof RichSoilFarmlandBlock) {
			return SoilUtils.isAbleToPlaceRichSoilFarmland(this);
		} else {
			return level.getBaseLightLevel(pos, 0) < PLACING_LIGHT_LEVEL && this.canPlantOnTop(state, level, pos);
		}
	}

	@Override
	public ActionResult onUseWithItem(ItemStack heldStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int age = state.get(COLONY_AGE);

		if (age > 0 && heldStack.isIn(ConventionalItemTags.SHEAR_TOOLS)) {
			dropStack(level, pos, getPickStack(level, pos, state, true));
			level.playSound(null, pos, SoundEvents.ENTITY_MOOSHROOM_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
			level.setBlockState(pos, state.with(COLONY_AGE, age - 1), 2);
			if (!level.isClient) {
				heldStack.damage(1, player, LivingEntity.getSlotForHand(hand));
			}
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		int age = state.get(COLONY_AGE);
		BlockState groundState = level.getBlockState(pos.down());
		if (age < getMaxAge() && groundState.isIn(ModTags.MUSHROOM_COLONY_GROWABLE_ON) &&  random.nextInt(4) == 0) {
			level.setBlockState(pos, state.with(COLONY_AGE, age + 1), 2);
		}
	}

	@Override
	public ItemStack getPickStack(WorldView level, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(this.mushroomType.value());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COLONY_AGE);
	}

	@Override
	public boolean isFertilizable(WorldView level, BlockPos pos, BlockState state) {
		return state.get(getAgeProperty()) < getMaxAge();
	}

	@Override
	public boolean canGrow(World level, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(World level) {
		return MathHelper.nextInt(level.random, 1, 2);
	}

	@Override
	public void grow(ServerWorld level, Random random, BlockPos pos, BlockState state) {
		int age = Math.min(getMaxAge(), state.get(COLONY_AGE) + getBonemealAgeIncrease(level));
		level.setBlockState(pos, state.with(COLONY_AGE, age), 2);
	}
}
