package vectorwing.farmersdelight.common.block;

import com.google.common.base.Suppliers;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class RiceRollMedleyBlock extends FeastBlock {
    public static final IntProperty ROLL_SERVINGS = IntProperty.of("servings", 0, 8);

    protected static final VoxelShape PLATE_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    protected static final VoxelShape FOOD_SHAPE = VoxelShapes.combine(PLATE_SHAPE, Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 4.0D, 14.0D), BooleanBiFunction.OR);

    public final Supplier<List<Item>> riceRollServings = Suppliers.memoize(() -> List.of(
                    ModItems.COD_ROLL.get(),
                    ModItems.COD_ROLL.get(),
                    ModItems.SALMON_ROLL.get(),
                    ModItems.SALMON_ROLL.get(),
                    ModItems.SALMON_ROLL.get(),
                    ModItems.KELP_ROLL_SLICE.get(),
                    ModItems.KELP_ROLL_SLICE.get(),
                    ModItems.KELP_ROLL_SLICE.get())
    );

    public RiceRollMedleyBlock(Settings properties) {
        super(properties, () -> ModItems.SALMON_ROLL.get(), true);
    }

    @Override
    public IntProperty getServingsProperty() {
        return ROLL_SERVINGS;
    }

    @Override
    public int getMaxServings() {
        return 8;
    }

    @Override
    public ItemStack getServingItem(BlockState state) {
        return new ItemStack(riceRollServings.get().get(state.get(getServingsProperty()) - 1));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView level, BlockPos pos, ShapeContext context) {
        return state.get(getServingsProperty()) == 0 ? PLATE_SHAPE : FOOD_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROLL_SERVINGS);
    }
}
