package vectorwing.farmersdelight.common.item;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.refabricated.ItemAbility;

import java.util.Set;

public class KnifeItem extends DiggerItem
{
    //uhmm whats this for??
	public static final Set<ItemAbility> KNIFE_ACTIONS = Set.of(ItemAbility.SHEARS_CARVE, ItemAbility.SWORD_DIG);

    public KnifeItem(Tier tier, Properties properties) {
        super(tier, ModTags.MINEABLE_WITH_KNIFE, properties);
    }

    public static void init() {
        UseBlockCallback.EVENT.register(KnifeEvents::onCakeInteraction);
    }

    @Override
    public boolean canAttackBlock(BlockState state, World level, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        if (enchantment.matchesKey(Enchantments.SWEEPING_EDGE)) {
            return false;
        }
        return super.canBeEnchantedWith(stack, enchantment, context);
    }

    public static class KnifeEvents
    {
        public static double onKnifeKnockback(double strength, LivingEntity entity) {
            LivingEntity attacker = entity.getPrimeAdversary();
            ItemStack toolStack = attacker != null ? attacker.getStackInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
            if (toolStack.getItem() instanceof KnifeItem) {
                strength = strength - 0.1F;
            }
            return strength;
        }

        public static ActionResult onCakeInteraction(PlayerEntity player, World level, Hand hand, BlockHitResult hitResult) {
            if (player.isSpectator()) // Fabric does not check spectator.
                return ActionResult.PASS;

            ItemStack toolStack = player.getStackInHand(hand);

            if (!toolStack.isIn(ModTags.KNIVES)) {
                return ActionResult.PASS;
            }

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (state.isIn(ModTags.DROPS_CAKE_SLICE)) {
                level.setBlockState(pos, Blocks.CAKE.getDefaultState().with(CakeBlock.BITES, 1), 3);
                Block.dropStacks(state, level, pos);
                ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()),
                        pos.getX(), pos.getY() + 0.2, pos.getZ() + 0.5,
                        -0.05, 0, 0);
                level.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);

                return ActionResult.SUCCESS;
            }

            if (block == Blocks.CAKE) {
                int bites = state.get(CakeBlock.BITES);
                if (bites < 6) {
                    level.setBlockState(pos, state.with(CakeBlock.BITES, bites + 1), 3);
                } else {
                    level.removeBlock(pos, false);
                }
                ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()),
                        pos.getX() + (bites * 0.1), pos.getY() + 0.2, pos.getZ() + 0.5,
                        -0.05, 0, 0);
                level.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);

                return ActionResult.sidedSuccess(level.isClient);
            }
            return ActionResult.PASS;
        }
    }

    @Override
    public ActionResult useOn(ItemUsageContext context) {
        World level = context.getWorld();
        ItemStack toolStack = context.getStack();
        BlockPos pos = context.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Direction facing = context.getSide();

        if (state.getBlock() == Blocks.PUMPKIN && toolStack.isIn(ModTags.KNIVES)) {
            PlayerEntity player = context.getPlayer();
            if (player != null && !level.isClient) {
                Direction direction = facing.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : facing;
                level.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                level.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction), 11);
                ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5D + (double) direction.getOffsetX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction.getOffsetZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                itemEntity.setVelocity(0.05D * (double) direction.getOffsetX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction.getOffsetZ() + level.random.nextDouble() * 0.02D);
                level.spawnEntity(itemEntity);
                toolStack.damage(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }
            return ActionResult.sidedSuccess(level.isClient);
        } else {
            return ActionResult.PASS;
        }
    }
}
