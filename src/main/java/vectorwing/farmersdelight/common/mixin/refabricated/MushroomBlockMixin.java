package vectorwing.farmersdelight.common.mixin.refabricated;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.block.RichSoilBlock;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;
import vectorwing.farmersdelight.common.utility.SoilUtils;

/**
 * Fabric should <b>really</b> have an event for this...
 * This is the bare minimum to keep parity with Forge.
 */
@Mixin(MushroomPlantBlock.class)
public class MushroomBlockMixin {
    @ModifyExpressionValue(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/MushroomPlantBlock;canPlantOnTop(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean farmersdelightrefabricated$allowPlantsOnMushroom(boolean original, BlockState state, WorldView level, BlockPos pos) {
        if (state.getBlock() != (Object)this)
            return original;

        if (level.getBlockState(pos.down()).getBlock() instanceof RichSoilBlock)
            return SoilUtils.isAbleToPlaceRichSoil((Block)(Object) this);

        if (level.getBlockState(pos.down()).getBlock() instanceof RichSoilFarmlandBlock)
            return SoilUtils.isAbleToPlaceRichSoilFarmland((Block)(Object) this);

        return original;
    }
}
