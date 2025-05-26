package vectorwing.farmersdelight.common.loot.modifier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.refabricated.LootModifier;

/**
 * Credits to Commoble for this implementation!
 */
public class FDAddTableLootModifier extends LootModifier {

    private final RegistryKey<LootTable> lootTable;

    public FDAddTableLootModifier(LootCondition[] conditionsIn, RegistryKey<LootTable> lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Refabricated: The game will loop if we don't make a new context.
//      LootContext extraContext = new LootContext.Builder(((LootContextAccessor) context).getParams()).create(Optional.empty());
//      extraContext.setQueriedLootTableId(this.lootTable.location());
//      context.getResolver().get(Registries.LOOT_TABLE, this.lootTable).ifPresent((extraTable) -> {
//          extraTable.value().getRandomItemsRaw(extraContext, createStackSplitter(context.getLevel(), generatedLoot::add));
//      });
        return generatedLoot;
    }

}
