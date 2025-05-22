package vectorwing.farmersdelight.common.mixin.refabricated;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vectorwing.farmersdelight.common.block.RichSoilBlock;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;
import vectorwing.farmersdelight.common.utility.SoilUtils;

/**
 * Fabric should <b>really</b> have an event for this...
 * This is the bare minimum to keep parity with Forge.
 */
@Mixin(CropBlock.class)
public class CropBlockMixin {
    @ModifyVariable(method = "getGrowthSpeed", at = @At(value = "LOAD", ordinal = 1), ordinal = 1)
    private static float farmersdelightrefabricated$modifyGrowthSpeedForNonFarmland(float original, Block block, BlockView level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.down());
        if (belowState.getBlock() instanceof RichSoilBlock && SoilUtils.isAbleToPlaceRichSoil(block) && original < 0.00001F)
            return 1.0F;

        if (belowState.getBlock() instanceof RichSoilFarmlandBlock && SoilUtils.isAbleToPlaceRichSoilFarmland(block) && original < 0.00001F) {
            if (belowState.contains(RichSoilFarmlandBlock.MOISTURE) && belowState.get(RichSoilFarmlandBlock.MOISTURE) > 0)
                return 3.0F;
            return 1.0F;
        }

        return original;
    }
}
