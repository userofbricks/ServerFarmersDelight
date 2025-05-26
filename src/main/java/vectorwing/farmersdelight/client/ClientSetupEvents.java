package vectorwing.farmersdelight.client;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.tooltip.TooltipData;
import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.renderer.*;
import vectorwing.farmersdelight.common.registry.*;

public class ClientSetupEvents
{
	public static TooltipComponent registerCustomTooltipRenderers(TooltipData data) {
		if (CookingPotTooltip.CookingPotTooltipComponent.class.isAssignableFrom(data.getClass())) {
			return new CookingPotTooltip((CookingPotTooltip.CookingPotTooltipComponent) data);
		}
		return null;
	}

	public static void onRegisterRenderers() {
		EntityRendererRegistry.register(ModEntityTypes.ROTTEN_TOMATO.get(), FlyingItemEntityRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntityTypes.STOVE, StoveRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntityTypes.CUTTING_BOARD, CuttingBoardRenderer::new);
		BlockEntityRendererFactories.register(ModBlockEntityTypes.SKILLET, SkilletRenderer::new);
	}

	public static void registerParticles() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}
}
