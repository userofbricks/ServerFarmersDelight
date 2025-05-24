package vectorwing.farmersdelight.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.UnknownNullability;
import vectorwing.farmersdelight.common.registry.ModEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

@MethodsReturnNonnullByDefault
public class RottenTomatoEntity extends ThrownItemEntity
{
	public RottenTomatoEntity(EntityType<? extends RottenTomatoEntity> entityType, World level) {
		super(entityType, level);
	}

	public RottenTomatoEntity(World level, LivingEntity entity, ItemStack stack) {
		super(ModEntityTypes.ROTTEN_TOMATO.get(), entity, level, stack);
	}

	public RottenTomatoEntity(World level, double x, double y, double z, ItemStack stack) {
		super(ModEntityTypes.ROTTEN_TOMATO.get(), x, y, z, level, stack);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.ROTTEN_TOMATO.get();
	}

	@Override
	public void handleStatus(byte id) {
		ItemStack entityStack = new ItemStack(this.getDefaultItem());
		if (id == 3) {
			ParticleEffect iparticledata = new ItemStackParticleEffect(ParticleTypes.ITEM, entityStack);

			for (int i = 0; i < 12; ++i) {
				this.getWorld().addParticleClient(iparticledata, this.getX(), this.getY(), this.getZ(),
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
			}
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult result) {
		super.onEntityHit(result);
		Entity entity = result.getEntity();
		entity.serverDamage(this.getDamageSources().thrown(this, this.getOwner()), 0);
		this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	protected void onCollision(HitResult result) {
		super.onCollision(result);
		if (!this.getWorld().isClient) {
			this.getWorld().sendEntityStatus(this, (byte) 3);
			this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			this.discard();
		}
	}
}
