package vectorwing.farmersdelight.common.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.effect.ComfortEffect;
import vectorwing.farmersdelight.common.effect.NourishmentEffect;

public class ModEffects
{
	public static final RegistryEntry<StatusEffect> NOURISHMENT = Registry.registerReference(Registries.STATUS_EFFECT, FarmersDelight.res("nourishment"), new NourishmentEffect());
	public static final RegistryEntry<StatusEffect> COMFORT = Registry.registerReference(Registries.STATUS_EFFECT, FarmersDelight.res("comfort"), new ComfortEffect());

	public static void register() {
	}

	public static void touch() {

	}
}
