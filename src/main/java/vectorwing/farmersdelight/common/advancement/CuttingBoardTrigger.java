package vectorwing.farmersdelight.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import vectorwing.farmersdelight.common.registry.ModAdvancements;

import java.util.Optional;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

public class CuttingBoardTrigger extends AbstractCriterion<CuttingBoardTrigger.TriggerInstance>
{
	@Override
	public Codec<TriggerInstance> getConditionsCodec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, TriggerInstance::test);
	}

	public static record TriggerInstance(
			Optional<LootContextPredicate> player) implements SimpleInstance
	{
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
				builder -> builder.group(
								EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
						.apply(builder, TriggerInstance::new)
		);

		public static AdvancementCriterion<TriggerInstance> simple() {
			return ModAdvancements.USE_CUTTING_BOARD.get().create(
					new TriggerInstance(Optional.empty())
			);
		}

		public boolean test() {
			return true;
		}
	}
}
