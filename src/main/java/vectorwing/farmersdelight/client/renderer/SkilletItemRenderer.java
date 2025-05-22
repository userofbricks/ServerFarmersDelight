package vectorwing.farmersdelight.client.renderer;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class SkilletItemRenderer implements SpecialModelRenderer<ItemStack> {
    public SkilletItemRenderer() {
    }

    public void render(ItemStack stack, ItemDisplayContext mode, MatrixStack poseStack, VertexConsumerProvider buffer, int packedLight, int packedOverlay, boolean glint) {
        //render block
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().getDefaultState();


        MinecraftClient mc = MinecraftClient.getInstance();

        ItemStackWrapper stackWrapper = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY);
        ItemStack ingredientStack = stackWrapper.getStack();

        float animation = 0;

        if (!ingredientStack.isEmpty()) {
            poseStack.push();
            poseStack.translate(0.5, 1 / 16f, 0.5);

            long gameTime = mc.world.getTime();
            if (stack.contains(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get()) && mode != ItemDisplayContext.GUI) {
                long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
                float partialTicks = mc.getRenderTickCounter().getTickProgress(false);
                animation = ((gameTime - time) + partialTicks) / SkilletItem.FLIP_TIME;
                animation = MathHelper.clamp(animation, 0, 1);
                float maxH = 0.4F;
                poseStack.translate(0, maxH * MathHelper.sin(animation * MathHelper.PI), 0);
                float rotationAnimation = stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false) ? animation + 1.0F : animation;
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180 * rotationAnimation));
            } else {
                poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false) ? 180 : 0));
            }

            poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            poseStack.scale(0.5F, 0.5F, 0.5F);

            if (mode != ItemDisplayContext.GUI) {
                var itemRenderer = mc.getItemRenderer();
                itemRenderer.renderItem(ingredientStack, ItemDisplayContext.FIXED, packedLight,
                        packedOverlay, poseStack, buffer, null, 0);
            }

            poseStack.pop();
        }

        poseStack.push();

        if (animation != 0 && mode.isFirstPerson()) {
            poseStack.translate(0, 0, 1);
            poseStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(MathHelper.sin(animation * MathHelper.TAU) * 15));
            poseStack.translate(0F, 0, -1);
            poseStack.translate(0, 0, -MathHelper.sin(animation * MathHelper.PI) * 0.2);
        }
        mc.getBlockRenderManager().renderBlockAsEntity(state, poseStack, buffer, packedLight, packedOverlay);

        poseStack.pop();

    }

    //stack held, flip timestamp, flipped
    @Override
    public ItemStack getData(ItemStack stack) {
        return stack;
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        public Unbaked() {}

        public MapCodec<Unbaked> getCodec() {
            return CODEC;
        }

        public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new SkilletItemRenderer();
        }
    }
}
