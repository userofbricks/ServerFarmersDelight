package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;


/**
 * Credits to squeek502 (AppleSkin) for the implementation reference!
 * <a href="https://www.curseforge.com/minecraft/mc-mods/appleskin">...</a>
 */

public class HUDOverlays
{
	public static int healthIconsOffset = 39;
	public static int foodIconsOffset = 39;
	private static final Identifier MOD_ICONS_TEXTURE = Identifier.of(FarmersDelight.MODID, "textures/gui/fd_icons.png");

	/**
	 * Moved to GuiMixin.
	 */
	@Deprecated
	public static void register() {
//		HudRenderCallback.EVENT.register(ComfortOverlay.INSTANCE::render);
//		HudRenderCallback.EVENT.register(NourishmentOverlay.INSTANCE::render);
	}

	public static abstract class BaseOverlay implements LayeredDrawer.Layer
	{
		public abstract void render(MinecraftClient mc, PlayerEntity player, DrawContext guiGraphics, int left, int right, int top, int guiTicks);

		@Override
		public final void render(@NotNull DrawContext guiGraphics, @NotNull RenderTickCounter deltaTracker) {
			MinecraftClient minecraft = MinecraftClient.getInstance();
			if (minecraft.player == null || !shouldRenderOverlay(minecraft, minecraft.player, guiGraphics, minecraft.inGameHud.getTicks()))
				return;

			int top = guiGraphics.getScaledWindowHeight();
			int left = guiGraphics.getScaledWindowWidth() / 2 - 91; // left of health bar
			int right = guiGraphics.getScaledWindowWidth() / 2 + 91; // right of food bar

			render(minecraft, minecraft.player, guiGraphics, left, right, top, minecraft.inGameHud.getTicks());
		}

		public boolean shouldRenderOverlay(MinecraftClient minecraft, PlayerEntity player, DrawContext guiGraphics, int guiTicks) {
			return !minecraft.options.hudHidden && minecraft.interactionManager != null && minecraft.interactionManager.hasStatusBars();
		}
	}

	public static class NourishmentOverlay extends BaseOverlay
	{
		public static final Identifier ID = Identifier.of(FarmersDelight.MODID, "nourishment");

		// Refabricated
		public static final NourishmentOverlay INSTANCE = new NourishmentOverlay();

		@Override
		public void render(MinecraftClient minecraft, PlayerEntity player, DrawContext guiGraphics, int left, int right, int top, int guiTicks) {
			HungerManager stats = player.getHungerManager();

			boolean isPlayerHealingWithSaturation =
					player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
							&& player.canFoodHeal()
							&& stats.getFoodLevel() >= 18;

			if (player.getStatusEffect(ModEffects.NOURISHMENT) != null) {
				drawNourishmentOverlay(stats, minecraft, guiGraphics, right, top - foodIconsOffset, isPlayerHealingWithSaturation);
			}
		}

		@Override
		public boolean shouldRenderOverlay(MinecraftClient mc, PlayerEntity player, DrawContext guiGraphics, int guiTicks) {
			if (!super.shouldRenderOverlay(mc, player, guiGraphics, guiTicks))
				return false;

			return Configuration.NOURISHED_HUNGER_OVERLAY.get();
		}
	}

	public static class ComfortOverlay extends BaseOverlay
	{
		public static final Identifier ID = Identifier.of(FarmersDelight.MODID, "comfort");

		// Refabricated
		public static final ComfortOverlay INSTANCE = new ComfortOverlay();

		@Override
		public void render(MinecraftClient minecraft, PlayerEntity player, DrawContext guiGraphics, int left, int right, int top, int guiTicks) {
			HungerManager stats = player.getHungerManager();

			boolean isPlayerEligibleForComfort = stats.getSaturationLevel() == 0.0F
					&& player.canFoodHeal()
					&& !player.hasStatusEffect(StatusEffects.REGENERATION);

			if (player.getStatusEffect(ModEffects.COMFORT) != null && isPlayerEligibleForComfort) {
				drawComfortOverlay(player, minecraft, guiGraphics, left, top - healthIconsOffset);
			}
		}

		@Override
		public boolean shouldRenderOverlay(MinecraftClient mc, PlayerEntity player, DrawContext guiGraphics, int guiTicks) {
			if (!super.shouldRenderOverlay(mc, player, guiGraphics, guiTicks))
				return false;

			return Configuration.COMFORT_HEALTH_OVERLAY.get();
		}
	}

	public static void drawNourishmentOverlay(HungerManager foodData, MinecraftClient minecraft, DrawContext graphics, int right, int top, boolean naturalHealing) {
		float saturation = foodData.getSaturationLevel();
		int foodLevel = foodData.getFoodLevel();
		int ticks = minecraft.inGameHud.getTicks();
		Random rand = new Random();
		rand.setSeed(ticks * 312871);

		RenderSystem.enableBlend();

		for (int j = 0; j < 10; ++j) {
			int x = right - j * 8 - 9;
			int y = top;

			if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
				y = top + (rand.nextInt(3) - 1);
			}

			// Background texture
			graphics.drawTexture(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);

			float effectiveHungerOfBar = (foodData.getFoodLevel()) / 2.0F - j;
			int naturalHealingOffset = naturalHealing ? 18 : 0;

			// Gilded hunger icons
			if (effectiveHungerOfBar >= 1)
				graphics.drawTexture(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
			else if (effectiveHungerOfBar >= .5)
				graphics.drawTexture(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
		}

		RenderSystem.disableBlend();
	}

	public static void drawComfortOverlay(PlayerEntity player, MinecraftClient minecraft, DrawContext graphics, int left, int top) {
		int ticks = minecraft.inGameHud.getTicks();
		Random rand = new Random();
		rand.setSeed((long) (ticks * 312871));

		int health = MathHelper.ceil(player.getHealth());
		float absorb = MathHelper.ceil(player.getAbsorptionAmount());
		EntityAttributeInstance attrMaxHealth = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		float healthMax = (float) attrMaxHealth.getValue();

		int regen = -1;
		if (player.hasStatusEffect(StatusEffects.REGENERATION)) regen = ticks % 25;

		int healthRows = MathHelper.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		int comfortSheen = ticks % 50;
		int comfortHeartFrame = comfortSheen % 2;
		int[] textureWidth = {5, 9};

		RenderSystem.enableBlend();

		int healthMaxSingleRow = MathHelper.ceil(Math.min(healthMax, 20) / 2.0F);
		int leftHeightOffset = ((healthRows - 1) * rowHeight); // This keeps the overlay on the bottommost row of hearts

		for (int i = 0; i < healthMaxSingleRow; ++i) {
			int column = i % 10;
			int x = left + column * 8;
			int y = top + leftHeightOffset;

			if (health <= 4) y += rand.nextInt(2);
			if (i == regen) y -= 2;

			if (column == comfortSheen / 2) {
				graphics.drawTexture(MOD_ICONS_TEXTURE, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
			}
			if (column == (comfortSheen / 2) - 1 && comfortHeartFrame == 0) {
				graphics.drawTexture(MOD_ICONS_TEXTURE, x + 5, y, 5, 9, 4, 9);
			}
		}

		RenderSystem.disableBlend();
	}
}
