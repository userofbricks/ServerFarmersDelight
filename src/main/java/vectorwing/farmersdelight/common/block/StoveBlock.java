package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.refabricated.ItemAbility;

import java.util.Optional;

@SuppressWarnings("deprecation")
public class StoveBlock extends BlockWithEntity
{
	public static final MapCodec<StoveBlock> CODEC = createCodec(StoveBlock::new);

	public static final BooleanProperty LIT = Properties.LIT;
	public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

	public StoveBlock(Settings properties) {
		super(properties);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, false));
		LandPathNodeTypesRegistry.registerDynamic(this, (state, world, pos, neighbor) -> getBlockPathType(state, world, pos));
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	protected ActionResult onUseWithItem(ItemStack heldStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		Item heldItem = heldStack.getItem();

		if (state.get(LIT)) {
			if (ItemAbility.SHOVEL_DIG.canPerformAction(heldStack)) {
				extinguish(state, level, pos);
				heldStack.damage(1, player, LivingEntity.getSlotForHand(hand));
				return ActionResult.SUCCESS;
			} else if (heldItem == Items.WATER_BUCKET) {
				if (!level.isClient()) {
					level.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				extinguish(state, level, pos);
				if (!player.isCreative()) {
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}
				return ActionResult.SUCCESS;
			}
		} else {
			if (heldItem instanceof FlintAndSteelItem) {
				level.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				level.setBlockState(pos, state.with(Properties.LIT, Boolean.TRUE), 11);
				heldStack.damage(1, player, LivingEntity.getSlotForHand(hand));
				return ActionResult.SUCCESS;
			} else if (heldItem instanceof FireChargeItem) {
				level.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
				level.setBlockState(pos, state.with(Properties.LIT, Boolean.TRUE), 11);
				if (!player.isCreative()) {
					heldStack.decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}

		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof StoveBlockEntity stoveEntity) {
			int stoveSlot = stoveEntity.getNextEmptySlot();
			if (stoveSlot < 0 || stoveEntity.isStoveBlockedAbove()) {
				return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
			}
			Optional<RecipeEntry<CampfireCookingRecipe>> recipe = stoveEntity.getMatchingRecipe(heldStack);
			if (recipe.isPresent()) {
				if (!level.isClient && stoveEntity.addItem(player.getAbilities().creativeMode ? heldStack.copy() : heldStack, recipe.get(), stoveSlot)) {
					return ActionResult.SUCCESS;
				}
				return ActionResult.CONSUME;
			}
		}

		return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
	}

	@Override
	public BlockRenderType getRenderType(BlockState pState) {
		return BlockRenderType.MODEL;
	}

	public void extinguish(BlockState state, World level, BlockPos pos) {
		level.setBlockState(pos, state.with(LIT, false), 2);
		double x = (double) pos.getX() + 0.5D;
		double y = pos.getY();
		double z = (double) pos.getZ() + 0.5D;
		level.playSoundClient(x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F, false);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite()).with(LIT, true);
	}

	@Override
	public void onSteppedOn(World level, BlockPos pos, BlockState state, Entity entity) {
		boolean isLit = level.getBlockState(pos).get(StoveBlock.LIT);
		if (isLit && !entity.bypassesSteppingEffects() && entity instanceof LivingEntity) {
			entity.serverDamage(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
		}

		super.onSteppedOn(level, pos, state, entity);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.getBlock() != world.getBlockState(pos).getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof StoveBlockEntity) {
				ItemUtils.dropItems(world, pos, ((StoveBlockEntity) tileEntity).getInventory());
			}
		}
		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(LIT, FACING);
	}

	@Override
	public void randomDisplayTick(BlockState stateIn, World level, BlockPos pos, Random rand) {
		if (stateIn.get(CampfireBlock.LIT)) {
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				level.playSoundClient(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.get(HorizontalFacingBlock.FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
			double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52D : horizontalOffset;
			double yOffset = rand.nextDouble() * 6.0D / 16.0D;
			double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52D : horizontalOffset;
			level.addParticleClient(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
			level.addParticleClient(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.STOVE.instantiate(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (state.get(LIT)) {
			return validateTicker(blockEntityType, ModBlockEntityTypes.STOVE, level.isClient
					? StoveBlockEntity::animationTick
					: StoveBlockEntity::cookingTick);
		}
		return null;
	}

	/**
	 * Refabricated: Deprecated but kept for cross-loader code. Use {@link StoveBlock#getBlockPathType(BlockState, BlockView, BlockPos)} instead.
	 */
	@Nullable
	@Deprecated
	public PathNodeType getBlockPathType(BlockState state, BlockView world, BlockPos pos, @Nullable MobEntity entity) {
		return getBlockPathType(state, world, pos);
	}

	@Nullable
	public PathNodeType getBlockPathType(BlockState state, BlockView world, BlockPos pos) {
		return state.get(LIT) ? PathNodeType.DAMAGE_FIRE : null;
	}

	@Override
	public BlockState rotate(BlockState pState, BlockRotation pRot) {
		return pState.with(FACING, pRot.rotate(pState.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, BlockMirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.get(FACING)));
	}
}
