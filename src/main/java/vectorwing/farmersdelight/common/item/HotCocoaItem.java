package vectorwing.farmersdelight.common.item;

import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class HotCocoaItem extends DrinkableItem
{
	public HotCocoaItem(net.minecraft.item.Item.Settings properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, World level, LivingEntity consumer) {
		Iterator<StatusEffectInstance> itr = consumer.getStatusEffects().iterator();
		ArrayList<RegistryEntry<StatusEffect>> compatibleEffects = new ArrayList<>();

		while (itr.hasNext()) {
			StatusEffectInstance effect = itr.next();
			if (effect.getEffectType().value().getCategory().equals(StatusEffectCategory.HARMFUL) && !effect.getEffectType().isIn(ModTags.HOT_COCOA_IGNORED)) {
				compatibleEffects.add(effect.getEffectType());
			}
		}

		if (!compatibleEffects.isEmpty()) {
			StatusEffectInstance selectedEffect = consumer.getStatusEffect(compatibleEffects.get(level.random.nextInt(compatibleEffects.size())));
			// There is no equivalent for MobEffectEvent, people are expected to mixin with instances like this on Fabric, so we don't bother.
			if (selectedEffect != null) {
				consumer.removeStatusEffect(selectedEffect.getEffectType());
			}
		}
	}
}
