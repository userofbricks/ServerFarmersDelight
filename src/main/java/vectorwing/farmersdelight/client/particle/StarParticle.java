package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class StarParticle extends SpriteBillboardParticle
{
	protected StarParticle(ClientWorld level, double posX, double posY, double posZ) {
		super(level, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.velocityX *= 0.01F;
		this.velocityY *= 0.01F;
		this.velocityZ *= 0.01F;
		this.velocityY += 0.1D;
		this.scale *= 1.5F;
		this.maxAge = 16;
		this.collidesWithWorld = false;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public float getSize(float scaleFactor) {
		return this.scale * MathHelper.clamp(((float) this.age + scaleFactor) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
	}

	@Override
	public void tick() {
		this.lastX = this.x;
		this.lastY = this.y;
		this.lastZ = this.z;
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			if (this.y == this.lastY) {
				this.velocityX *= 1.1D;
				this.velocityZ *= 1.1D;
			}

			this.velocityX *= 0.86F;
			this.velocityY *= 0.86F;
			this.velocityZ *= 0.86F;
			if (this.onGround) {
				this.velocityX *= 0.7F;
				this.velocityZ *= 0.7F;
			}

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
			StarParticle particle = new StarParticle(level, x, y + 0.3D, z);
			particle.setSprite(this.spriteSet);
			particle.setColor(1.0F, 1.0F, 1.0F);
			return particle;
		}
	}
}
