package vectorwing.farmersdelight.client.renderer;

import net.minecraft.util.math.Vec3d;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.Random;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity>
{
	private final Random random = new Random();

	public SkilletRenderer(BlockEntityRendererFactory.Context context) {
	}

	@Override
	public void render(SkilletBlockEntity skilletEntity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int combinedLight, int combinedOverlay, Vec3d cameraPos) {
		Direction direction = skilletEntity.getCachedState().get(StoveBlock.FACING);
		ItemStackHandler inventory = skilletEntity.getInventory();
		int posLong = (int) skilletEntity.getPos().asLong();

		ItemStack stack = inventory.getStackInSlot(0);
		int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
		this.random.setSeed(seed);

		if (!stack.isEmpty()) {
			int itemRenderCount = this.getModelCount(stack);
			for (int i = 0; i < itemRenderCount; i++) {
				poseStack.push();

				// Stack up items in the skillet, with a slight offset per item
				float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				poseStack.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);

				// Rotate item to face the skillet's front side
				float degrees = -direction.getPositiveHorizontalDegrees();
				poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degrees));

				// Rotate item flat on the skillet. Use X and Y from now on
				poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

				// Resize the items
				poseStack.scale(0.5F, 0.5F, 0.5F);

				if (skilletEntity.getWorld() != null)
					MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, skilletEntity.getWorld(), posLong);
				poseStack.pop();
			}
		}
	}

	protected int getModelCount(ItemStack stack) {
		if (stack.getCount() > 48) {
			return 5;
		} else if (stack.getCount() > 32) {
			return 4;
		} else if (stack.getCount() > 16) {
			return 3;
		} else if (stack.getCount() > 1) {
			return 2;
		}
		return 1;
	}
}
