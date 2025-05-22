package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class SteamParticle extends SpriteBillboardParticle
{
	protected SteamParticle(ClientWorld level, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(level, x, y, z);
		this.scale(2.0F);
		this.setBoundingBoxSpacing(0.25F, 0.25F);

		this.maxAge = this.random.nextInt(50) + 80;

		this.gravityStrength = 3.0E-6F;
		this.velocityX = motionX;
		this.velocityY = motionY + (double) (this.random.nextFloat() / 500.0F);
		this.velocityZ = motionZ;
	}

	@Override
	@NotNull
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;
		if (this.age++ < this.maxAge && !(this.alpha <= 0.0F)) {
			this.velocityX += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
			this.velocityZ += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
			this.velocityY -= this.gravityStrength;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			if (this.age >= this.maxAge - 60 && this.alpha > 0.01F) {
				this.alpha -= 0.02F;
			}
		} else {
			this.markDead();
		}
	}

	public static class Factory implements ParticleFactory<SimpleParticleType>
	{
		private final SpriteProvider spriteSet;

		public Factory(SpriteProvider sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientWorld level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SteamParticle particle = new SteamParticle(level, x, y + 0.3D, z, xSpeed, ySpeed, zSpeed);
			particle.setAlpha(0.6F);
			particle.setSprite(this.spriteSet);
			return particle;
		}
	}
}
