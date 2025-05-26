package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CookingPotScreen extends HandledScreen<CookingPotMenu>
{
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
	private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);

    public CookingPotScreen(CookingPotMenu screenContainer, PlayerInventory inv, Text titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void init() {
		super.init();
        this.titleX = 28;
	}

	@Override
	public void render(DrawContext gui, final int mouseX, final int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		this.renderMealDisplayTooltip(gui, mouseX, mouseY);
		this.renderHeatIndicatorTooltip(gui, mouseX, mouseY);
	}

	private void renderHeatIndicatorTooltip(DrawContext gui, int mouseX, int mouseY) {
		if (this.isPointWithinBounds(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
			String key = "container.cooking_pot." + (this.handler.isHeated() ? "heated" : "not_heated");
			gui.drawTooltip(this.textRenderer, TextUtils.getTranslation(key), mouseX, mouseY);
		}
	}

	protected void renderMealDisplayTooltip(DrawContext gui, int mouseX, int mouseY) {
		if (this.client != null && this.client.player != null && this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
			if (this.focusedSlot.id == 6) {
				List<Text> tooltip = new ArrayList<>();

				ItemStack mealStack = this.focusedSlot.getStack();
				tooltip.add(((MutableText) mealStack.getItem().getName()).formatted(mealStack.getRarity().getFormatting()));

				ItemStack containerStack = this.handler.blockEntity.getContainer();
				String container = !containerStack.isEmpty() ? containerStack.getItem().getName().getString() : "";

				tooltip.add(TextUtils.getTranslation("container.cooking_pot.served_on", container).formatted(Formatting.GRAY));

				gui.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
			} else {
				gui.drawItemTooltip(textRenderer, this.focusedSlot.getStack(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawForeground(DrawContext gui, int mouseX, int mouseY) {
		super.drawForeground(gui, mouseX, mouseY);
		gui.drawText(this.textRenderer, this.playerInventoryTitle, 8, (this.backgroundHeight - 96 + 2), 4210752, false);
	}

	@Override
	protected void drawBackground(DrawContext gui, float partialTicks, int mouseX, int mouseY) {
		// Render UI background
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.client == null)
			return;

		gui.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);

		// Render heat icon
		if (this.handler.isHeated()) {
			gui.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, this.x + HEAT_ICON.x, this.y + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height, 256, 256);
		}

		// Render progress arrow
		int l = this.handler.getCookProgressionScaled();
		gui.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, this.x + PROGRESS_ARROW.x, this.y + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height, 256, 256);
	}
}
