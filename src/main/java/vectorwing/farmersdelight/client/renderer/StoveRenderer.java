package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

public class StoveRenderer implements BlockEntityRenderer<StoveBlockEntity>
{
	public StoveRenderer(BlockEntityRendererFactory.Context context) {
	}

	@Override
	public void render(StoveBlockEntity stoveEntity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = stoveEntity.getCachedState().get(StoveBlock.FACING).getOpposite();

		ItemStackHandler inventory = stoveEntity.getInventory();
		int posLong = (int) stoveEntity.getPos().asLong();

		for (int i = 0; i < inventory.getSlotCount(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				poseStack.push();

				// Center item above the stove
				poseStack.translate(0.5D, 1.02D, 0.5D);

				// Rotate item to face the stove's front side
				float f = -direction.getPositiveHorizontalDegrees();
				poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));

				// Rotate item flat on the stove. Use X and Y from now on
				poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

				// Neatly align items according to their index
				Vec2f itemOffset = stoveEntity.getStoveItemOffset(i);
				poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items
				poseStack.scale(0.375F, 0.375F, 0.375F);

				if (stoveEntity.getWorld() != null)
					MinecraftClient.getInstance().getItemRenderer().renderItem(stoveStack, ItemDisplayContext.FIXED, WorldRenderer.getLightmapCoordinates(stoveEntity.getWorld(), stoveEntity.getPos().up()), combinedOverlayIn, poseStack, buffer, stoveEntity.getWorld(), posLong + i);
				poseStack.pop();
			}
		}
	}
}