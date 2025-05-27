package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.Optional;

public class CookingPotBlock extends BlockWithEntity implements Waterloggable {
    public static final MapCodec<CookingPotBlock> CODEC = createCodec(CookingPotBlock::new);

    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<CookingPotSupport> SUPPORT = EnumProperty.of("support", CookingPotSupport.class);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
    protected static final VoxelShape SHAPE_WITH_TRAY = VoxelShapes.union(SHAPE, Block.createCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

    public CookingPotBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(SUPPORT, CookingPotSupport.NONE).with(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public ActionResult onUseWithItem(ItemStack heldStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        if (heldStack.isEmpty() && player.isSneaking()) {
            level.setBlockState(pos, state.with(SUPPORT, state.get(SUPPORT).equals(CookingPotSupport.HANDLE)
                    ? getTrayState(level, pos) : CookingPotSupport.HANDLE));
            level.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
        } else if (!level.isClient) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CookingPotBlockEntity cookingPotEntity) {
                ItemStack servingStack = cookingPotEntity.useHeldItemOnMeal(heldStack);
                if (servingStack != ItemStack.EMPTY) {
                    if (!player.getInventory().insertStack(servingStack)) {
                        player.dropItem(servingStack, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else {
                    player.openHandledScreen(cookingPotEntity);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return state.get(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos pos = context.getBlockPos();
        World level = context.getWorld();
        FluidState fluid = level.getFluidState(context.getBlockPos());

        BlockState state = this.getDefaultState()
                .with(FACING, context.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);

        if (context.getSide().equals(Direction.DOWN)) {
            return state.with(SUPPORT, CookingPotSupport.HANDLE);
        }
        return state.with(SUPPORT, getTrayState(level, pos));
    }

    @Override
    public void onStateReplaced(BlockState state, ServerWorld level, BlockPos currentPos, boolean moved) {
        if (state.get(WATERLOGGED)) {
            level.scheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(level));
        }
    }

    private CookingPotSupport getTrayState(WorldAccess level, BlockPos pos) {
        if (level.getBlockState(pos.down()).isIn(ModTags.TRAY_HEAT_SOURCES)) {
            return CookingPotSupport.TRAY;
        }
        return CookingPotSupport.NONE;
    }

    @Override
    public ItemStack getPickStack(WorldView level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getPickStack(level, pos, state, includeData);

        Optional<CookingPotBlockEntity> cookingPot = level.getBlockEntity(pos, ModBlockEntityTypes.COOKING_POT);
        if (cookingPot.isPresent()) {
            stack = cookingPot.get().getAsItem();
        }

        return stack;
    }

    @Override
    public BlockState onBreak(World level, BlockPos pos, BlockState state, PlayerEntity player) {
        if (state.getBlock() != level.getBlockState(pos).getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CookingPotBlockEntity cookingPotEntity) {
                ItemScatterer.spawn(level, pos, cookingPotEntity.getDroppableInventory());
                //cookingPotEntity.getUsedRecipesAndPopExperience(level, Vec3d.ofCenter(pos));
                level.updateComparators(pos, this);
            }

            return super.onBreak(level, pos, state, player);
        }
        return super.onBreak(level, pos, state, player);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, SUPPORT, WATERLOGGED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World level, BlockPos pos, Random random) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CookingPotBlockEntity cookingPotEntity && cookingPotEntity.isHeated()) {
            SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty()
                    ? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get()
                    : ModSounds.BLOCK_COOKING_POT_BOIL.get();
            double x = (double) pos.getX() + 0.5D;
            double y = pos.getY();
            double z = (double) pos.getZ() + 0.5D;
            if (random.nextInt(10) == 0) {
                level.playSoundClient(x, y, z, boilSound, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.2F + 0.9F, false);
            }
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState blockState, World level, BlockPos pos) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CookingPotBlockEntity) {
            ItemStackHandler inventory = ((CookingPotBlockEntity) tileEntity).getInventory();
            return MathUtils.calcRedstoneFromItemHandler(inventory);
        }
        return 0;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COOKING_POT.instantiate(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntity) {
        if (level.isClient) {
            return validateTicker(blockEntity, ModBlockEntityTypes.COOKING_POT, CookingPotBlockEntity::animationTick);
        }
        return validateTicker(blockEntity, ModBlockEntityTypes.COOKING_POT, CookingPotBlockEntity::cookingTick);
    }
}
