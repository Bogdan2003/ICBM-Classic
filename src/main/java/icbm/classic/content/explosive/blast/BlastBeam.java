package icbm.classic.content.explosive.blast;

import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.imp.transform.vector.Pos;
import icbm.classic.content.entity.EntityFlyingBlock;
import icbm.classic.content.entity.EntityLightBeam;
import icbm.classic.content.explosive.thread.ThreadExplosion;
import icbm.classic.content.explosive.thread.ThreadSky;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/** Used by Exothermic and Endothermic explosions.
 *
 * @author Calclavia */
public abstract class BlastBeam extends Blast
{
    protected ThreadExplosion thread;
    protected Set<EntityFlyingBlock> feiBlocks = new HashSet<EntityFlyingBlock>();
    protected EntityLightBeam lightBeam;
    protected float red, green, blue;
    /** Radius in which the uplighting of blocks takes place */
    protected int radius = 5;

    public BlastBeam(World world, Entity entity, double x, double y, double z, float size)
    {
        super(world, entity, x, y, z, size);
    }

    /** Called before an explosion happens */
    @Override
    public void doPreExplode()
    {
        if (!this.oldWorld().isRemote)
        {
            this.oldWorld().createExplosion(this.exploder, position.x(), position.y(), position.z(), 4F, true);

            this.lightBeam = new EntityLightBeam(this.oldWorld(), position, 20 * 20, this.red, this.green, this.blue);
            this.oldWorld().spawnEntityInWorld(this.lightBeam);

            this.thread = new ThreadSky(this.position, (int) this.getRadius(), 50, this.exploder);
            this.thread.start();
        }
    }

    @Override
    public void doExplode()
    {
        if (!this.oldWorld().isRemote)
        {
            if (this.callCount > 100 / this.proceduralInterval() && this.thread.isComplete)
            {
                this.controller.endExplosion();
            }

            if (this.canFocusBeam(this.oldWorld(), position))
            {
                Pos currentPos;
                int blockID;
                int metadata;
                double dist;

                int r = radius;

                for (int x = -r; x < r; x++)
                {
                    for (int y = -r; y < r; y++)
                    {
                        for (int z = -r; z < r; z++)
                        {
                            dist = MathHelper.sqrt_double((x * x + y * y + z * z));

                            if (dist > r || dist < r - 3)
                            {
                                continue;
                            }
                            currentPos = new Pos(position.x() + x, position.y() + y, position.z() + z);
                            Block block = currentPos.getBlock(oldWorld());
                            if (block == null || block.isAir(this.oldWorld(), x, y, z) || block.getBlockHardness(this.oldWorld(), x, y, x) < 0)
                            {
                                continue;
                            }

                            metadata = this.oldWorld().getBlockMetadata(currentPos.xi(), currentPos.yi(), currentPos.zi());

                            if (this.oldWorld().rand.nextInt(2) > 0)
                            {
                                this.oldWorld().setBlockToAir(currentPos.xi(), currentPos.yi(), currentPos.zi());

                                currentPos = currentPos.add(0.5D);
                                EntityFlyingBlock entity = new EntityFlyingBlock(this.oldWorld(), currentPos, block, metadata);
                                this.oldWorld().spawnEntityInWorld(entity);
                                this.feiBlocks.add(entity);
                                entity.pitchChange = 50 * this.oldWorld().rand.nextFloat();
                            }
                        }
                    }
                }
            }
            else
            {
                this.controller.endExplosion();
            }

            for (EntityFlyingBlock entity : this.feiBlocks)
            {
                Pos entityPosition = new Pos(entity);
                Pos centeredPosition = entityPosition.add(this.position.multiply(-1));
                centeredPosition.rotate(2);
                Location newPosition = this.position.add(centeredPosition);
                entity.motionX /= 3;
                entity.motionY /= 3;
                entity.motionZ /= 3;
                entity.addVelocity((newPosition.x() - entityPosition.x()) * 0.5 * this.proceduralInterval(), 0.09 * this.proceduralInterval(), (newPosition.z() - entityPosition.z()) * 0.5 * this.proceduralInterval());
                entity.yawChange += 3 * this.oldWorld().rand.nextFloat();
            }
        }
    }

    @Override
    public void doPostExplode()
    {
        if (!this.oldWorld().isRemote)
        {
            if (this.lightBeam != null)
            {
                this.lightBeam.setDead();
                this.lightBeam = null;
            }
        }
    }

    public boolean canFocusBeam(World worldObj, Location position)
    {
        return worldObj.canBlockSeeTheSky(position.xi(), position.yi() + 1, position.zi());
    }

    /** The interval in ticks before the next procedural call of this explosive
     *
     * @return - Return -1 if this explosive does not need proceudral calls */
    @Override
    public int proceduralInterval()
    {
        return 4;
    }

    @Override
    public long getEnergy()
    {
        return 10000;
    }

}
