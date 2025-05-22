package vectorwing.farmersdelight.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class RottenTomatoItem extends Item implements ProjectileItem
{
	public RottenTomatoItem(net.minecraft.item.Item.Settings properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack heldStack = player.getStackInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_ROTTEN_TOMATO_THROW.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
		if (!level.isClient) {
			RottenTomatoEntity projectile = new RottenTomatoEntity(level, player);
			projectile.setItem(heldStack);
			projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
			level.spawnEntity(projectile);
		}

		player.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!player.getAbilities().creativeMode) {
			heldStack.decrement(1);
		}

		return InteractionResultHolder.sidedSuccess(heldStack, level.isClient());
	}

	@Override
	public ProjectileEntity createEntity(World level, Position position, ItemStack itemStack, Direction direction) {
		RottenTomatoEntity rottenTomato = new RottenTomatoEntity(level, position.getX(), position.getY(), position.getZ());
		rottenTomato.setItem(itemStack);
		return rottenTomato;
	}
}
