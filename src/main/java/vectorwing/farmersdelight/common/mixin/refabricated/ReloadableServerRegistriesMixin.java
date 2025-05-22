package vectorwing.farmersdelight.common.mixin.refabricated;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.refabricated.TagUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.loot.LootDataType;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.resource.ResourceManager;

/**
 * @see TagUtils
 */
@Mixin(ReloadableRegistries.class)
public class ReloadableServerRegistriesMixin {
    @Inject(method = "method_58276", at = @At(value = "HEAD"))
    private static <T> void enchiridion$setLootTableAccess(RegistryOps registryOps, ResourceManager resourceManager, Executor executor, LootDataType<T> lootDataType, CallbackInfoReturnable<CompletableFuture> cir) {
        if (lootDataType != LootDataType.LOOT_TABLES)
            return;

        TagUtils.setLootTableResourceManager(resourceManager);
    }

    @Inject(method = "method_58288", at = @At("RETURN"))
    private static void enchiridion$clearLootTableAccess(CombinedDynamicRegistries layeredRegistryAccess, List list, CallbackInfoReturnable<CombinedDynamicRegistries> cir) {
        TagUtils.resetEarlyTagCollections();
    }
}