package vectorwing.farmersdelight.common.mixin.refabricated;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.client.renderer.SkilletItemRenderer;

import static vectorwing.farmersdelight.FarmersDelight.res;

@Mixin(SpecialModelTypes.class)
public class SpecialModelTypesMixin {
    @Shadow public static final Codecs.IdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER = new Codecs.IdMapper();

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(CallbackInfo ci) {
        ID_MAPPER.put(res("skillet"), SkilletItemRenderer.Unbaked.CODEC);
    }
}
