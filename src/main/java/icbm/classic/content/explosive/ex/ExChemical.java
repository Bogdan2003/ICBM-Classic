package icbm.classic.content.explosive.ex;

import com.builtbroken.mc.api.edit.IWorldChangeAction;
import com.builtbroken.mc.api.event.TriggerCause;
import com.builtbroken.mc.lib.helper.recipe.RecipeUtility;
import icbm.classic.ICBMClassic;
import icbm.classic.content.explosive.Explosives;
import icbm.classic.content.explosive.blast.BlastChemical;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ExChemical extends Explosion
{
    public ExChemical(String mingZi, int tier)
    {
        super(mingZi, tier);
        //chemical
        if (this.getTier() == 1)
        {
            this.missileModelPath = "missiles/tier1/missile_head_chemical.obj";
        }//contagious
        else if (this.getTier() == 2)
        {
            this.missileModelPath = "missiles/tier2/missile_head_contagious.obj";
        }
    }

    @Override
    public void init()
    {
        if (this.getTier() == 1)
        {
            RecipeUtility.addRecipe(new ShapedOreRecipe(Explosives.CHEMICAL.getItemStack(),
                    "@@@", "@?@", "@@@",
                    '@', ICBMClassic.itemPoisonPowder,
                    '?', Explosives.DEBLITATION.getItemStack()), "Chemical", ICBMClassic.INSTANCE.getConfig(), true);
        }
        else if (this.getTier() == 2)
        {
            RecipeUtility.addRecipe(new ShapedOreRecipe(Explosives.CONTAGIOUS.getItemStack(2),
                    " @ ", "@?@", " @ ",
                    '?', Items.rotten_flesh,
                    '@', Explosives.CHEMICAL.getItemStack()), "Contagious", ICBMClassic.INSTANCE.getConfig(), true);
        }
    }

    @Override
    public void doCreateExplosion(World world, double x, double y, double z, Entity entity)
    {
        if (this.getTier() == 1)
        {
            new BlastChemical(world, entity, x, y, z, 20, 20 * 30, false).setPoison().setRGB(0.8f, 0.8f, 0).explode();
        }
        else if (this.getTier() == 2)
        {
            new BlastChemical(world, entity, x, y, z, 20, 20 * 30, false).setContagious().setRGB(0.3f, 0.8f, 0).explode();
        }

    }

    @Override
    public IWorldChangeAction createBlastForTrigger(World world, double x, double y, double z, TriggerCause triggerCause, double size, NBTTagCompound tag)
    {
        return null;
    }
}
