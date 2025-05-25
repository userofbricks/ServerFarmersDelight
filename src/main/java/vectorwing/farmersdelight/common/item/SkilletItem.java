package vectorwing.farmersdelight.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Optional;

public class SkilletItem extends BlockItem {

    public static final float FLIP_TIME = 12;

    public static final ToolMaterial SKILLET_TIER = ToolMaterial.IRON;
    protected static final Identifier FD_ATTACK_KNOCKBACK_UUID = Identifier.of(FarmersDelight.MODID, "base_attack_knockback");

    public SkilletItem(Block block, net.minecraft.item.Item.Settings properties) {
        super(block, properties.maxDamage(SKILLET_TIER.durability()).component(DataComponentTypes.ENCHANTABLE, new EnchantableComponent(SKILLET_TIER.enchantmentValue()))
                .repairable(SKILLET_TIER.repairItems()));
        float attackDamage = 5.0F + SKILLET_TIER.attackDamageBonus();
    }

    public static AttributeModifiersComponent createAttributes(ToolMaterial tier, float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + tier.attackDamageBonus(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_KNOCKBACK, new EntityAttributeModifier(FD_ATTACK_KNOCKBACK_UUID, 1, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        if (oldStack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())
                != newStack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get()) ||
                oldStack.get(ModDataComponents.COOKING_TIME_LENGTH.get())
                        != newStack.get(ModDataComponents.COOKING_TIME_LENGTH.get()) ||
                oldStack.get(ModDataComponents.SKILLET_INGREDIENT.get()) !=
                        newStack.get(ModDataComponents.SKILLET_INGREDIENT.get())) {
            return false;
        }

        return super.allowComponentsUpdateAnimation(player, hand, oldStack, newStack);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    private static boolean isPlayerNearHeatSource(PlayerEntity player, WorldView level) {
        if (player.isOnFire()) {
            return true;
        }
        BlockPos pos = player.getBlockPos();
        for (BlockPos nearbyPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (level.getBlockState(nearbyPos).isIn(ModTags.HEAT_SOURCES)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity entity) {
        Optional<RegistryEntry.Reference<Enchantment>> fireAspect = entity.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOptional(Enchantments.FIRE_ASPECT);
        if (fireAspect.isEmpty()) {
            return 0;
        }
        int fireAspectLevel = fireAspect.map(stack.getEnchantments()::getLevel).orElse(0);
        int cookingTime = stack.getOrDefault(ModDataComponents.COOKING_TIME_LENGTH.get(), 0);
        return SkilletBlock.getSkilletCookingTime(cookingTime, fireAspectLevel);
    }

    @Override
    public ActionResult use(World level, PlayerEntity player, Hand hand) {
        ItemStack skilletStack = player.getStackInHand(hand);
        if (!skilletStack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY).getStack().isEmpty()) {
            skilletStack.set(ModDataComponents.COOKING.get(), true);
        } else {
            skilletStack.set(ModDataComponents.COOKING.get(), false);
        }
        if (isPlayerNearHeatSource(player, level)) {
            Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack cookingStack = player.getStackInHand(otherHand);

            if (!skilletStack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY).getStack().isEmpty()) {
                player.setCurrentHand(hand);
                return ActionResult.PASS;
            }

            Optional<RecipeEntry<CampfireCookingRecipe>> recipe = level instanceof ServerWorld serverWorld ? getCookingRecipe(cookingStack, serverWorld) : Optional.empty();
            if (recipe.isPresent()) {
                if (player.isSubmergedInWater()) {
                    player.sendMessage(TextUtils.getTranslation("item.skillet.underwater"), true);
                    return ActionResult.PASS;
                }
                ItemStack cookingStackCopy = cookingStack.copy();
                ItemStack cookingStackUnit = cookingStackCopy.split(1);
                skilletStack.set(ModDataComponents.SKILLET_INGREDIENT.get(), new ItemStackWrapper(cookingStackUnit));
                skilletStack.set(ModDataComponents.COOKING_TIME_LENGTH.get(), recipe.get().value().getCookingTime());
                skilletStack.set(ModDataComponents.SKILLET_FLIPPED.get(), false);
                player.setCurrentHand(hand);
                player.setStackInHand(otherHand, cookingStackCopy);
                return ActionResult.CONSUME;
            } else {
                player.sendMessage(TextUtils.getTranslation("item.skillet.how_to_cook"), true);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void usageTick(World level, LivingEntity entity, ItemStack stack, int count) {
        if (!stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY).getStack().isEmpty()) {
            stack.set(ModDataComponents.COOKING.get(), true);
        } else {
            stack.set(ModDataComponents.COOKING.get(), false);
        }
        if (entity instanceof PlayerEntity player) {
            if (stack.contains(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
                long flipTimeStamp = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
                long l = level.getTime() - flipTimeStamp;
                if (l > FLIP_TIME) {
                    stack.remove(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
                    stack.set(ModDataComponents.SKILLET_FLIPPED.get(), !stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false));
                } else if (l == FLIP_TIME - 8 && level.isClient) {
                    //why does it need to play early? idk
                    //plays instantly right before it lands & on client only so its instant. cant be done in statement above as that might not run fo player as stack is sent when updated
                    level.playSoundFromEntity(player, entity, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.PLAYERS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F);
                } else if (level.isClient && level.random.nextInt(50) == 0 && l < FLIP_TIME - 8 || l > FLIP_TIME - 3) {
                    level.playSoundFromEntity(null, entity, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.PLAYERS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F);
                }
            } else if (level.isClient && level.random.nextInt(50) == 0) {
                level.playSoundFromEntity(null, entity, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.PLAYERS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F);
            }
        }
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World level, LivingEntity entity, int timeLeft) {
        stack.set(ModDataComponents.COOKING.get(), false);
        if (entity instanceof PlayerEntity player) {
            ItemStackWrapper storedStack = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY);
            if (!storedStack.getStack().isEmpty()) {
                ItemStack cookingStack = storedStack.getStack();
                player.getInventory().offerOrDrop(cookingStack);
                stack.remove(ModDataComponents.SKILLET_INGREDIENT.get());
                stack.remove(ModDataComponents.COOKING_TIME_LENGTH.get());
                stack.remove(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
                stack.remove(ModDataComponents.SKILLET_FLIPPED.get());
            }
        }
        return super.onStoppedUsing(stack, level, entity, timeLeft);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World level, LivingEntity entity) {
        stack.set(ModDataComponents.COOKING.get(), false);
        if (entity instanceof PlayerEntity player) {
            ItemStackWrapper storedStack = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY);
            if (!storedStack.getStack().isEmpty()) {
                ItemStack cookingStack = storedStack.getStack();
                Optional<RecipeEntry<CampfireCookingRecipe>> cookingRecipe = level instanceof ServerWorld serverWorld ? getCookingRecipe(cookingStack, serverWorld) : Optional.empty();

                cookingRecipe.ifPresent((recipe) -> {
                    ItemStack resultStack = recipe.value().craft(new SingleStackRecipeInput(cookingStack), level.getRegistryManager());
                    if (!player.getInventory().insertStack(resultStack)) {
                        player.dropItem(resultStack, false);
                    }
                    if (player instanceof ServerPlayerEntity) {
                        Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
                    }
                });
                stack.remove(ModDataComponents.SKILLET_INGREDIENT.get());
                stack.remove(ModDataComponents.COOKING_TIME_LENGTH.get());
                stack.remove(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
                stack.remove(ModDataComponents.SKILLET_FLIPPED.get());
            }
        }

        return stack;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.contains(ModDataComponents.COOKING_TIME_LENGTH.get())) {
            return Math.round(13.0F - (float) getClientPlayerHack().getItemUseTimeLeft() * 13.0F / (float) this.getMaxUseTime(stack, getClientPlayerHack()));
        } else {
            return super.getItemBarStep(stack);
        }
    }

    // hack
    @Environment(EnvType.CLIENT)
    private static PlayerEntity getClientPlayerHack() {
        return MinecraftClient.getInstance().player;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (stack.contains(ModDataComponents.COOKING_TIME_LENGTH.get())) {
            return 0xFF8B4F;
        } else return super.getItemBarColor(stack);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || stack.contains(ModDataComponents.COOKING_TIME_LENGTH.get());
    }

    public static Optional<RecipeEntry<CampfireCookingRecipe>> getCookingRecipe(ItemStack stack, ServerWorld level) {
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        return level.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SingleStackRecipeInput(stack), level);
    }

    @Override
    protected boolean postPlacement(BlockPos pos, World level, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        super.postPlacement(pos, level, player, stack, state);
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof SkilletBlockEntity skillet) {
            skillet.setSkilletItem(stack);
            return true;
        }
        return false;
    }

    public boolean postMine(ItemStack stack, World level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClient && state.getHardness(level, pos) != 0.0F) {
            stack.damage(1, entity, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null && player.isSneaking()) {
            return super.place(context);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.matchesKey(Enchantments.SWEEPING_EDGE)) {
            return false;
        }
        return super.canBeEnchantedWith(stack, enchantment, context);
    }


    public static class SkilletEvents {
        /*
         This is modfiied before the player loses their attack power, and is unmodified as soon as the Skillet sound is played.
         This doesn't exist on Forge because they moved the resetting of attack power to after the events are fired.
         */
        public static float attackPower = 0.0F;

        public static void playSkilletAttackSound(LivingEntity entity, DamageSource source) {
            Entity attacker = source.getSource();

            if (!(attacker instanceof LivingEntity livingEntity)) return;
            if (!livingEntity.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.SKILLET.get())) return;

            float pitch = 0.9F + (livingEntity.getRandom().nextFloat() * 0.2F);
            if (livingEntity instanceof PlayerEntity player) {
                if (attackPower > 0.8F) {
                    player.playSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), 1.0F, pitch);
                } else {
                    player.playSound(ModSounds.ITEM_SKILLET_ATTACK_WEAK.get(), 0.8F, 0.9F);
                }
            } else {
                livingEntity.playSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), 1.0F, pitch);
            }
            attackPower = 0.0F;
        }
    }
}