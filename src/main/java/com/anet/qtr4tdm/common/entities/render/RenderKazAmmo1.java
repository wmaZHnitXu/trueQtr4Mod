package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.KazAmmoEntity;
import com.anet.qtr4tdm.common.entities.models.kazammo1;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderKazAmmo1 extends Render<KazAmmoEntity> {

    public static final ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/kazammo1.png");
    public final kazammo1 model = new kazammo1();
    
    protected RenderKazAmmo1(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(KazAmmoEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5d, y + 1.5d, z + 0.5d);
        GlStateManager.rotate(180, 1, 0, 0);
        model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(KazAmmoEntity entity) {
        return texture;
    }
    
    public static class Factory implements IRenderFactory<KazAmmoEntity>
	{
		@Override
		public Render<KazAmmoEntity> createRenderFor(RenderManager manager)
		{
			return new RenderKazAmmo1(manager);
		}
	}
}
