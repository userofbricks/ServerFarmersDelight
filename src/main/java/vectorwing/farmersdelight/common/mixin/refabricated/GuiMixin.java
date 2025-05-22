package vectorwing.farmersdelight.common.mixin.refabricated;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.client.gui.HUDOverlays;


@Mixin(value = InGameHud.class, priority = 999) // Before AppleSkin
public class GuiMixin {
    @Unique
    private RenderTickCounter farmersdelightrefabricated$deltaTracker;

    @Inject(method = "renderHotbarAndDecorations", at = @At("HEAD"))
    private void farmersdelightrefabricated$captureDeltaTracker(DrawContext guiGraphics, RenderTickCounter deltaTracker, CallbackInfo ci) {
        farmersdelightrefabricated$deltaTracker = deltaTracker;
    }

    @Inject(method = "renderHotbarAndDecorations", at = @At("TAIL"))
    private void farmersdelightrefabricated$clearDeltaTracker(DrawContext guiGraphics, RenderTickCounter deltaTracker, CallbackInfo ci) {
        farmersdelightrefabricated$deltaTracker = null;
    }

    @Inject(method = "renderHearts", at = @At("TAIL"))
    private void farmersdelightrefabricated$renderHearts(DrawContext guiGraphics, PlayerEntity player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        HUDOverlays.ComfortOverlay.INSTANCE.render(guiGraphics, farmersdelightrefabricated$deltaTracker);
    }

    @Inject(method = "renderFood", at = @At("TAIL"))
    private void farmersdelightrefabricated$renderNourishment(DrawContext guiGraphics, PlayerEntity player, int y, int x, CallbackInfo ci) {
        HUDOverlays.NourishmentOverlay.INSTANCE.render(guiGraphics, farmersdelightrefabricated$deltaTracker);
    }
}
