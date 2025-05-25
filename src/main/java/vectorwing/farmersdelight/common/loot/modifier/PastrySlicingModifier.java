package vectorwing.farmersdelight.common.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.refabricated.LootModifier;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class PastrySlicingModifier extends LootModifier
{

	public static final int MAX_CAKE_BITES = 7;
	public static final int MAX_PIE_BITES = 4;
	private final Item pastrySlice;

	/**
	 * This loot modifier drops a slice for every remaining bite of a broken pastry block.
	 * If the block is a CakeBlock, it drops up to 7 slices.
	 * If the block is a PieBlock, it drops up to 4 slices.
	 * Otherwise, this does nothing.
	 */
	public PastrySlicingModifier(LootCondition[] conditionsIn, Item pastrySliceIn) {
		super(conditionsIn);
		this.pastrySlice = pastrySliceIn;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		BlockState state = context.get(LootContextParameters.BLOCK_STATE);
		if (state != null) {
			Block targetBlock = state.getBlock();
			if (targetBlock instanceof CakeBlock) {
				int bites = state.get(CakeBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_CAKE_BITES - bites));
			} else if (targetBlock instanceof PieBlock) {
				int bites = state.get(PieBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_PIE_BITES - bites));
			}
		}

		return generatedLoot;
	}

}
