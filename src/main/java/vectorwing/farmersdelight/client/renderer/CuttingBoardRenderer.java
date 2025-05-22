package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity>
{
	public CuttingBoardRenderer(BlockEntityRendererFactory.Context pContext) {
	}

	@Override
	public void render(CuttingBoardBlockEntity cuttingBoardEntity, float partialTicks, MatrixStack poseStack, VertexConsumerProvider buffer, int combinedLight, int combinedOverlay, Vec3d cameraPos) {
		Direction direction1 = cuttingBoardEntity.getCachedState().get(CuttingBoardBlock.FACING);
		Direction direction = direction1.getOpposite();
		ItemStack boardStack = cuttingBoardEntity.getStoredItem();
		int posLong = (int) cuttingBoardEntity.getPos().asLong();

		if (!boardStack.isEmpty()) {
			poseStack.push();

			ItemRenderer itemRenderer = MinecraftClient.getInstance()
					.getItemRenderer();

			boolean isBlockItem = boardStack.getItem() instanceof BlockItem;

			if (cuttingBoardEntity.isItemCarvingBoard()) {
				renderItemCarved(poseStack, direction, boardStack);
			} else if (isBlockItem && !boardStack.isIn(ModTags.FLAT_ON_CUTTING_BOARD)) {
				renderBlock(poseStack, direction);
			} else {
				renderItemLayingDown(poseStack, direction);
			}

			itemRenderer.renderItem(boardStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, cuttingBoardEntity.getWorld(), posLong);
			poseStack.pop();
		}
	}

	public void renderItemLayingDown(MatrixStack matrixStackIn, Direction direction) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.08D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.getPositiveHorizontalDegrees();
		matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));

		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	public void renderBlock(MatrixStack matrixStackIn, Direction direction) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D, 0.27D, 0.5D);

		// Rotate block to face the cutting board's front side
		float f = -direction.getPositiveHorizontalDegrees();
		matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));

		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	public void renderItemCarved(MatrixStack matrixStackIn, Direction direction, ItemStack itemStack) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.getPositiveHorizontalDegrees() + 180;
		matrixStackIn.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));

		// Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
		Item toolItem = itemStack.getItem();
		float poseAngle;
		if (toolItem.getRegistryEntry().registryKey().getValue().getPath().contains("pickaxe") || toolItem instanceof HoeItem) {
			poseAngle = 225.0F;
		} else if (toolItem instanceof TridentItem) {
			poseAngle = 135.0F;
		} else {
			poseAngle = 180.0F;
		}
		matrixStackIn.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(poseAngle));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}
}
