package vectorwing.farmersdelight.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import java.util.List;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public class CopySkilletFunction extends ConditionalLootFunction
{
	public static final Identifier ID = Identifier.of(FarmersDelight.MODID, "copy_skillet");
	public static final MapCodec<CopySkilletFunction> CODEC = RecordCodecBuilder.mapCodec(
			p_298131_ -> addConditionsField(p_298131_).apply(p_298131_, CopySkilletFunction::new)
	);

	private CopySkilletFunction(List<LootCondition> conditions) {
		super(conditions);
	}

	public static net.minecraft.loot.function.ConditionalLootFunction.Builder<?> builder() {
		return builder(CopySkilletFunction::new);
	}

	@Override
	protected ItemStack process(ItemStack stack, LootContext context) {
		BlockEntity tile = context.getParamOrNull(LootContextParameters.BLOCK_ENTITY);
		if (tile instanceof SkilletBlockEntity blockEntity) {
			stack = blockEntity.getSkilletAsItem();
		}
		return stack;
	}

	@Override
	public LootFunctionType<CopySkilletFunction> getType() {
		return ModLootFunctions.COPY_SKILLET.get();
	}
}
