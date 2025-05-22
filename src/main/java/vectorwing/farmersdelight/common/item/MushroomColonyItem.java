package vectorwing.farmersdelight.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;

;

public class MushroomColonyItem extends BlockItem
{
	public MushroomColonyItem(Block blockIn, net.minecraft.item.Item.Settings properties) {
		super(blockIn, properties);
	}

	@Override
	@Nullable
	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState originalState = this.getBlock().getPlacementState(context);
		if (originalState != null) {
			BlockState matureState = originalState.with(MushroomColonyBlock.COLONY_AGE, 3);
			return this.canPlace(context, matureState) ? matureState : null;
		}
		return null;
	}
}

