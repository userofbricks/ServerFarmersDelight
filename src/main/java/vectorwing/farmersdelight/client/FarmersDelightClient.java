package vectorwing.farmersdelight.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.networking.ModNetworking;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class FarmersDelightClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register(ClientSetupEvents::registerCustomTooltipRenderers);
        ClientSetupEvents.onRegisterRenderers();
        ClientSetupEvents.registerParticles();

        HandledScreens.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new);

        ModNetworking.initClient();

        // Obscure Fabric event to the rescue!
        ClientPreAttackCallback.EVENT.register((client, player, clickCount) -> {
            if (player != null && !player.isSpectator() && player.isUsingItem() && player.getActiveItem().getItem() instanceof SkilletItem && clickCount != 0 && !player.getActiveItem().contains(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
                ClientPlayNetworking.send(ModNetworking.FlipSkilletMessage.INSTANCE);
            }
            return false;
        });

        // render type stuff
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.BROWN_MUSHROOM_COLONY.get(), ModBlocks.RED_MUSHROOM_COLONY.get(), ModBlocks.BUDDING_TOMATO_CROP.get(),
                ModBlocks.CABBAGE_CROP.get(), ModBlocks.CUTTING_BOARD.get(), ModBlocks.ONION_CROP.get(),
                ModBlocks.WILD_CABBAGES.get(), ModBlocks.WILD_BEETROOTS.get(), ModBlocks.WILD_CARROTS.get(),
                ModBlocks.WILD_ONIONS.get(), ModBlocks.WILD_POTATOES.get(), ModBlocks.WILD_RICE.get(), ModBlocks.WILD_TOMATOES.get(),
                ModBlocks.RICE_CROP.get(), ModBlocks.TOMATO_CROP.get(), ModBlocks.RICE_CROP_PANICLES.get(),
                ModBlocks.ROAST_CHICKEN_BLOCK.get(), ModBlocks.SANDY_SHRUB.get(), ModBlocks.ROPE.get(),
                ModBlocks.CANVAS_RUG.get(), ModBlocks.COOKING_POT.get(), ModBlocks.SAFETY_NET.get());
    }
}
