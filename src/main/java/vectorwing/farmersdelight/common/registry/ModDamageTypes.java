package vectorwing.farmersdelight.common.registry;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vectorwing.farmersdelight.FarmersDelight;

public class ModDamageTypes
{
	public static final RegistryKey<DamageType> STOVE_BURN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(FarmersDelight.MODID, "stove_burn"));

	public static DamageSource getSimpleDamageSource(World level, RegistryKey<DamageType> type) {
		return new DamageSource(level.getRegistryManager().registryOrThrow(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(type));
	}
}
