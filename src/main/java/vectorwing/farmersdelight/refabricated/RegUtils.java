
package vectorwing.farmersdelight.refabricated;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.particle.ParticleType;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class RegUtils {

    public static <R, T extends R> Supplier<T> register(String name, Supplier<T> supplier, Registry<R> reg) {
        T object = supplier.get();
        Registry.register(reg, FarmersDelight.res(name), object);
        return () -> object;
    }

    public static <B extends Entity> Supplier<EntityType<B>> regEntity(String name, Supplier<EntityType.Builder<B>> supplier) {
        return register(name, () -> supplier.get().build(RegistryKey.of(Registries.ENTITY_TYPE.getKey(), FarmersDelight.res(name))), Registries.ENTITY_TYPE);
    }

    public static <B extends ScreenHandlerType<?>> Supplier<B> regMenu(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.SCREEN_HANDLER);
    }

    public static <B extends PlacementModifierType<?>> Supplier<B> regPlacementMod(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.PLACEMENT_MODIFIER_TYPE);
    }

    public static <B extends RecipeSerializer<?>> Supplier<B> regRecipeSerializer(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.RECIPE_SERIALIZER);
    }

    public static <B extends RecipeType<?>> Supplier<B> regRecipe(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.RECIPE_TYPE);
    }

    public static <B extends ParticleType<?>> Supplier<B> regParticle(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.PARTICLE_TYPE);
    }

    public static <B extends SoundEvent> Supplier<B> regSound(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.SOUND_EVENT);
    }

    public static <B extends LootFunctionType<?>> Supplier<B> regLootFunction(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.LOOT_FUNCTION_TYPE);
    }

    public static <B extends Feature<?>> Supplier<B> regFeature(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.FEATURE);
    }

    public static <T extends BlockEntity> BlockEntityType<T> regBlockEntity(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = FarmersDelight.res(name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static <B extends ItemGroup> Supplier<B> regTab(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.ITEM_GROUP);
    }

    public static <B extends ComponentType<?>> Supplier<B> regComponent(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.DATA_COMPONENT_TYPE);
    }

    public static <A> Supplier<ComponentType<A>> regComponent(String name, Consumer<ComponentType.Builder<A>> stuff) {
        ComponentType.Builder<A> builder = ComponentType.builder();
        stuff.accept(builder);
        return register(name, builder::build, Registries.DATA_COMPONENT_TYPE);
    }

    public static <B extends StatusEffect> Supplier<B> regEffect(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.STATUS_EFFECT);
    }

    public static <B extends LootFunctionType<?>> Supplier<B> regLootFunc(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.LOOT_FUNCTION_TYPE);
    }

    public static <B extends Item> Supplier<B> regItem(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.ITEM);
    }

    public static <B extends Block> Supplier<B> regBlock(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.BLOCK);
    }

    public static <B extends Criterion<?>> Supplier<B> regTrigger(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.CRITERION);
    }

    public static <B extends LootConditionType> Supplier<B> regLootCond(String name, Supplier<B> supplier) {
        return register(name, supplier, Registries.LOOT_CONDITION_TYPE);
    }
}
