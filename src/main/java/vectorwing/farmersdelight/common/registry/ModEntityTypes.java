package vectorwing.farmersdelight.common.registry;

import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;

import java.util.function.Supplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import static vectorwing.farmersdelight.refabricated.RegUtils.regEntity;

public class ModEntityTypes {
    public static final Supplier<EntityType<RottenTomatoEntity>> ROTTEN_TOMATO = regEntity("rotten_tomato", () -> (
            EntityType.Builder.<RottenTomatoEntity>create(RottenTomatoEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25F, 0.25F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(10)
                    .build()));

	public static void touch() {

	}
}
