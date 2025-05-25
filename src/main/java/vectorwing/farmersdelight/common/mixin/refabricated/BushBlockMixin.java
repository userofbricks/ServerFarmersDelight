package vectorwing.farmersdelight.common.mixin.refabricated;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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
@Mixin(BushBlock.class)
public class BushBlockMixin {
    @ModifyReturnValue(method = "canGrow", at = @At("RETURN"))
    private boolean farmersdelightrefabricated$allowPlantsOnBushes(boolean original, World level, Random random, BlockPos pos, BlockState state) {
        if (state.getBlock() != (Object)this)
            return original;

        if (level.getBlockState(pos.down()).getBlock() instanceof RichSoilBlock)
            return SoilUtils.isAbleToPlaceRichSoil((Block)(Object) this);

        if (level.getBlockState(pos.down()).getBlock() instanceof RichSoilFarmlandBlock)
            return SoilUtils.isAbleToPlaceRichSoilFarmland((Block)(Object) this);

        return original;
    }
}
