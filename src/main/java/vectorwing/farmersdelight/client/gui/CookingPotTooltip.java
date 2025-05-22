package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.joml.Matrix4f;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotTooltip implements TooltipComponent
{
	private static final int ITEM_SIZE = 16;
	private static final int MARGIN = 4;

	private final int textSpacing = MinecraftClient.getInstance().textRenderer.fontHeight + 1;
	private final ItemStack mealStack;

	public CookingPotTooltip(CookingPotTooltipComponent tooltip) {
		this.mealStack = tooltip.mealStack;
	}

	@Override
	public int getHeight() {
		return mealStack.isEmpty() ? textSpacing : textSpacing + ITEM_SIZE;
	}

	@Override
	public int getWidth(TextRenderer font) {
		if (!mealStack.isEmpty()) {
			MutableText textServingsOf = mealStack.getCount() == 1
					? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
					: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());
			return Math.max(font.getWidth(textServingsOf), font.getWidth(mealStack.getName()) + 20);
		} else {
			return font.getWidth(TextUtils.getTranslation("tooltip.cooking_pot.empty"));
		}
	}

	@Override
	public void renderImage(TextRenderer font, int mouseX, int mouseY, DrawContext gui) {
		if (mealStack.isEmpty()) return;
		gui.drawItem(mealStack, mouseX, mouseY + textSpacing, 0);
	}

	@Override
	public void drawText(TextRenderer font, int x, int y, Matrix4f matrix4f, VertexConsumerProvider.Immediate bufferSource) {
		Integer color = Formatting.GRAY.getColorValue();
		int gray = color == null ? -1 : color;

		if (!mealStack.isEmpty()) {
			MutableText textServingsOf = mealStack.getCount() == 1
					? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
					: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());

			font.draw(textServingsOf, (float) x, (float) y, gray, true, matrix4f, bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
			font.draw(mealStack.getName(), x + ITEM_SIZE + MARGIN, y + textSpacing + MARGIN, -1, true, matrix4f, bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
		} else {
			MutableText textEmpty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
			font.draw(textEmpty, x, y, gray, true, matrix4f, bufferSource, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
		}
	}

	public static record CookingPotTooltipComponent(ItemStack mealStack) implements TooltipData
	{
	}
}
