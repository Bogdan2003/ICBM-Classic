package icbm.classic.content.explosive.ex.missiles;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.lib.transform.vector.Pos;
import icbm.classic.content.entity.EntityMissile;
import icbm.classic.content.entity.EntityMissile.MissileType;
import icbm.classic.content.explosive.blast.BlastRepulsive;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/** @author Calclavia */
public class MissileCluster extends Missile
{
    public static final int MAX_CLUSTER = 12;
    protected double spread = 20;

    public MissileCluster(String name, int tier)
    {
        super(name, tier);
        this.hasBlock = false;
        this.modelName = "missile_cluster.tcn";
    }

    @Override
    public void update(EntityMissile missileObj)
    {
        if (missileObj.motionY < -0.5)
        {
            if (missileObj.missileCount < MAX_CLUSTER)
            {
                if (!missileObj.worldObj.isRemote)
                {
                    Pos position = new Pos(missileObj);
                    EntityMissile clusterMissile = new EntityMissile(missileObj.worldObj, position.clone(), position.clone(), 0);

                    double radius = spread;
                    double theta = 0;
                    double x = 0;
                    double y = 0;
                    double z = 0;
                    //TODO make spread equal to a 30 degree angle from center point

                    if (missileObj.missileCount > 0)
                    {
                        theta = (missileObj.missileCount / 12.0) * Math.PI * 2;

                        x = radius * Math.cos(theta);
                        clusterMissile.posX += Math.cos(theta) * 5;

                        z = radius * Math.sin(theta);
                        clusterMissile.posZ += Math.sin(theta) * 5;
                    }

                    clusterMissile.missileType = MissileType.CruiseMissile;
                    clusterMissile.protectionTime = 20 + missileObj.targetHeight - 1;

                    clusterMissile.launch(missileObj.targetVector.add(new Pos(x, y, z)));
                    missileObj.worldObj.spawnEntityInWorld(clusterMissile);
                }
                missileObj.protectionTime = 20;
                missileObj.missileCount++;
            }
            else
            {
                missileObj.setDead();
            }
        }
    }

    @Override
    public void doCreateExplosion(World world, double x, double y, double z, Entity entity)
    {
        new BlastRepulsive(world, entity, x, y, z, 6).setDestroyItems().explode();
    }

    @Override
    public boolean isCruise()
    {
        return false;
    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double size, NBTTagCompound tag)
    {
        return null;
    }
}