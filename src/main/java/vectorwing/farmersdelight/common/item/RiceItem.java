package vectorwing.farmersdelight.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class RiceItem extends BlockItem
{
	public RiceItem(Block block, Item.Settings properties) {
		super(block, properties);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult result = this.place(new ItemPlacementContext(context));
		if (result.equals(ActionResult.FAIL)) {
			PlayerEntity player = context.getPlayer();
			BlockState targetState = context.getWorld().getBlockState(context.getBlockPos());
			if (player != null && context.getSide().equals(Direction.UP) && (targetState.isIn(BlockTags.DIRT) || targetState.getBlock() instanceof FarmlandBlock)) {
				player.sendMessage(TextUtils.getTranslation("block.rice.invalid_placement"), true);
			}
		}
		return !result.isAccepted() ? this.use(context.getWorld(), context.getPlayer(), context.getHand()) : result;
	}
}
