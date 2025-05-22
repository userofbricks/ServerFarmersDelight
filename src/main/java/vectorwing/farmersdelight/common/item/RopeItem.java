package vectorwing.farmersdelight.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RopeItem extends FuelBlockItem
{
	public RopeItem(Block block, net.minecraft.item.Item.Settings properties) {
		super(block, properties, 200);
	}

	@Override
	@Nullable
	public ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
		BlockPos pos = context.getBlockPos();
		World level = context.getWorld();
		BlockState state = level.getBlockState(pos);
		Block block = this.getBlock();

		if (state.getBlock() != block) return context;
		Direction direction;
		if (context.shouldCancelInteraction()) {
			direction = context.getSide();
		} else {
			direction = Direction.DOWN;
		}

		int i = 0;
		BlockPos.Mutable blockpos$mutable = (new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ())).move(direction);

		while (i < 256) {
			state = level.getBlockState(blockpos$mutable);
			if (state.getBlock() != this.getBlock()) {
				FluidState fluid = state.getFluidState();
				if (!fluid.isIn(FluidTags.WATER) && !fluid.isEmpty()) {
					return null;
				}
				if (state.canReplace(context)) {
					return ItemPlacementContext.offset(context, blockpos$mutable, direction);
				}
				break;
			}

			if (direction != Direction.DOWN) {
				return context;
			}

			blockpos$mutable.move(direction);
			++i;
		}

		return null;
	}

	@Override
	protected boolean checkStatePlacement() {
		return false;
	}
}
