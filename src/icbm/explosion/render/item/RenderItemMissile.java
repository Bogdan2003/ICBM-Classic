package icbm.explosion.render.item;

import icbm.explosion.zhapin.ExplosiveRegistry;
import icbm.explosion.zhapin.daodan.Missile;
import icbm.explosion.zhapin.daodan.ItemMissile;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderItemMissile implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return this.shouldUseRenderHelper(type, item, null);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return item.getItem() instanceof ItemMissile;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		if (this.shouldUseRenderHelper(type, item, null))
		{
			Missile daoDan = (Missile) ExplosiveRegistry.get(item.getItemDamage());

			float scale = 0.7f;
			float right = 0f;

			if (type == ItemRenderType.INVENTORY)
			{
				scale = 0.4f;
				right = 0.15f;

				if (daoDan.getTier() == 2 || !daoDan.hasBlockForm())
				{
					scale = scale / 1.5f;
				}
				else if (daoDan.getTier() == 3)
				{
					scale = scale / 1.7f;
					right = 0.5f;
				}
				else if (daoDan.getTier() == 4)
				{
					scale = scale / 1.4f;
					right = 0.2f;
				}

				GL11.glTranslatef(right, 0f, 0f);
			}

			if (type == ItemRenderType.EQUIPPED)
			{
				GL11.glTranslatef(1.15f, 1f, 0.5f);
				GL11.glRotatef(180, 0, 0, 1f);
			}
			else
			{
				GL11.glRotatef(-90, 0, 0, 1f);
			}

			if (type == ItemRenderType.ENTITY)
			{
				scale = scale / 1.5f;
			}

			GL11.glScalef(scale, scale, scale);

			FMLClientHandler.instance().getClient().renderEngine.bindTexture(daoDan.getMissileResource());
			daoDan.getMissileModel().render(0.0625F);
		}
	}
}