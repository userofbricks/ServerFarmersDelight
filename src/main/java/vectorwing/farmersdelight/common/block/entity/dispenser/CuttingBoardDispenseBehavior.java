package vectorwing.farmersdelight.common.block.entity.dispenser;

import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Uses the given item as a tool when facing a Cutting Board.
 */
@MethodsReturnNonnullByDefault
public class CuttingBoardDispenseBehavior extends FallibleItemDispenserBehavior
{
	private static final HashMap<Item, DispenserBehavior> DISPENSE_ITEM_BEHAVIOR_HASH_MAP = new HashMap<>();
	public static final CuttingBoardDispenseBehavior INSTANCE = new CuttingBoardDispenseBehavior();

	public static void registerBehaviour(Item item, CuttingBoardDispenseBehavior behavior) {
		DISPENSE_ITEM_BEHAVIOR_HASH_MAP.put(item, DispenserBlock.BEHAVIORS.get(item)); // Save the old behaviours so they can be used later
		DispenserBlock.registerBehavior(item, behavior);
	}

	@Override
	public final ItemStack dispense(BlockPointer source, ItemStack stack) {
		if (tryDispenseStackOnCuttingBoard(source, stack)) {
			this.playSound(source); // I added this because i completely overrode the super implementation which had the sounds.
			this.spawnParticles(source, source.state().get(DispenserBlock.FACING)); // see above, same reasoning
			return stack;
		}
		return DISPENSE_ITEM_BEHAVIOR_HASH_MAP.get(stack.getItem()).dispense(source, stack); // Not targetted on cutting board, use vanilla/other mods behaviour
	}

	public boolean tryDispenseStackOnCuttingBoard(BlockPointer source, ItemStack stack) {
		setSuccess(false);
		World level = source.world();
		BlockPos pos = source.pos().offset(source.state().get(DispenserBlock.FACING));
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (block instanceof CuttingBoardBlock && blockEntity instanceof CuttingBoardBlockEntity cuttingBoard) {
			ItemStack boardItem = cuttingBoard.getStoredItem().copy();
			if (!boardItem.isEmpty() && cuttingBoard.processStoredItemUsingTool(stack, null)) {
				CuttingBoardBlock.spawnCuttingParticles(level, pos, boardItem, 5);
				setSuccess(true);
			}
			return true;
		}
		return false;
	}
}
