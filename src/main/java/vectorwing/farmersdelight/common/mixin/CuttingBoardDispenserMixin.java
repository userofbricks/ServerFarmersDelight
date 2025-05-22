package vectorwing.farmersdelight.common.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.entity.dispenser.CuttingBoardDispenseBehavior;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(DispenserBlock.class)
public abstract class CuttingBoardDispenserMixin
{
	@Shadow
	protected abstract DispenserBehavior getDispenseMethod(World level, ItemStack stack);

	@Inject(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/DispenserBlock;getDispenseMethod(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/core/dispenser/DispenseItemBehavior;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	public void onCuttingBoardDispenseFromInject(ServerWorld level, BlockState state, BlockPos pos, CallbackInfo ci, DispenserBlockEntity dispenser, BlockPointer source, int slot, ItemStack stack) {
		BlockState facingState = level.getBlockState(pos.offset(state.get(DispenserBlock.FACING)));
		if (Configuration.DISPENSER_TOOLS_CUTTING_BOARD.get() && facingState.isOf(ModBlocks.CUTTING_BOARD.get())) {
			dispenser.setStack(slot, CuttingBoardDispenseBehavior.INSTANCE.dispense(source, stack));
			ci.cancel();
		}
	}
}
