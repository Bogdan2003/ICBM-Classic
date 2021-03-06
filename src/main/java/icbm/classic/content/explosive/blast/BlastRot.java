package icbm.classic.content.explosive.blast;

import com.builtbroken.mc.imp.transform.vector.Pos;
import icbm.classic.ICBMClassic;
import icbm.classic.content.explosive.thread.ThreadLargeExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

/** Creates radiation spawning
 *
 * @author Calclavia */
public class BlastRot extends Blast
{
    private ThreadLargeExplosion thread;
    private float nengLiang;

    public BlastRot(World world, Entity entity, double x, double y, double z, float size)
    {
        super(world, entity, x, y, z, size);
    }

    public BlastRot(World world, Entity entity, double x, double y, double z, float size, float nengLiang)
    {
        this(world, entity, x, y, z, size);
        this.nengLiang = nengLiang;
    }

    @Override
    public void doPreExplode()
    {
        if (!this.oldWorld().isRemote)
        {
            this.thread = new ThreadLargeExplosion(this.position, (int) this.getRadius(), this.nengLiang, this.exploder);
            this.thread.start();
        }
    }

    @Override
    public void doExplode()
    {
        if (!this.oldWorld().isRemote)
        {
            if (this.thread.isComplete)
            {
                for (Pos targetPosition : this.thread.results)
                {
                    /** Decay the blocks. */
                    Block blockID = targetPosition.getBlock(this.oldWorld());

                    if (blockID != blockID)
                    {
                        if (blockID == Blocks.grass || blockID == Blocks.sand)
                        {
                            if (this.oldWorld().rand.nextFloat() > 0.96)
                            {
                                targetPosition.setBlock(this.oldWorld(), ICBMClassic.blockRadioactive);
                            }
                        }

                        if (blockID == Blocks.stone)
                        {
                            if (this.oldWorld().rand.nextFloat() > 0.99)
                            {
                                targetPosition.setBlock(this.oldWorld(), ICBMClassic.blockRadioactive);
                            }
                        }

                        else if (blockID == Blocks.leaves)
                        {
                            targetPosition.setBlock(this.oldWorld(), Blocks.air);
                        }
                        else if (blockID == Blocks.tallgrass)
                        {
                            if (Math.random() * 100 > 50)
                            {
                                targetPosition.setBlock(this.oldWorld(), Blocks.cobblestone);
                            }
                            else
                            {
                                targetPosition.setBlock(this.oldWorld(), Blocks.air);
                            }
                        }
                        else if (blockID == Blocks.farmland)
                        {
                            targetPosition.setBlock(this.oldWorld(), ICBMClassic.blockRadioactive);
                        }
                        else if (blockID.getMaterial() == Material.water)
                        {
                            if (FluidRegistry.getFluid("toxicwaste") != null)
                            {
                                targetPosition.setBlock(this.oldWorld(), FluidRegistry.getFluid("toxicwaste").getBlock());
                            }
                        }
                    }
                }

                this.controller.endExplosion();
            }
        }
    }

    @Override
    public int proceduralInterval()
    {
        return 1;
    }

    @Override
    public long getEnergy()
    {
        return 100;
    }
}
