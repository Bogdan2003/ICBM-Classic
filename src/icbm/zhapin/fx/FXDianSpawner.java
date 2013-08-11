package icbm.zhapin.fx;

import java.util.Random;

import universalelectricity.core.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

/**
 * A spawner used to spawn in multiple electrical bolts for a specific duration.
 */
public class FXDianSpawner extends EntityFX
{
	private Vector3 start;
	private Vector3 end;

	public FXDianSpawner(World world, Vector3 startVec, Vector3 targetVec, long seed, int duration)
	{
		super(world, startVec.x, startVec.y, startVec.z, 0.0D, 0.0D, 0.0D);

		if (seed == 0)
		{
			this.rand = new Random();
		}
		else
		{
			this.rand = new Random(seed);
		}

		this.start = startVec;
		this.end = targetVec;
		this.particleMaxAge = duration;
	}

	@Override
	public void onUpdate()
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new FXDian(this.worldObj, this.start, this.end, 0));
		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}
	}

}