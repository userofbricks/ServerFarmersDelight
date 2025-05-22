package vectorwing.farmersdelight.common.mixin.refabricated;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.networking.ModNetworking;
import vectorwing.farmersdelight.refabricated.FDRecipeBookTypes;

import java.util.List;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin extends RecipeBook {
    @Inject(method = "sendRecipes", at = @At("TAIL"))
    private void fdrf$sendCookingRecipeValues(ClientboundRecipePacket.State state, ServerPlayerEntity player, List<Identifier> recipes, CallbackInfo ci) {
        ServerPlayNetworking.send(player, new ModNetworking.SendRecipeBookValuesMessage(getOptions().isGuiOpen(FDRecipeBookTypes.COOKING), getOptions().isFilteringCraftable(FDRecipeBookTypes.COOKING)));
    }
}
