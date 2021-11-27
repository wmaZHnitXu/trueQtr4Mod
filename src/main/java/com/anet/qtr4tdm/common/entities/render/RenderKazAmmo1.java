package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.KazAmmoEntity;
import com.anet.qtr4tdm.common.entities.models.kazammo1;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderKazAmmo1 extends Render<KazAmmoEntity> {

    public static final ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/kazammo1.png");
    public static final ResourceLocation texture1 = new ResourceLocation(TdmMod.MODID + ":textures/entity/kazammo1_e.png");
    public final kazammo1 model = new kazammo1();
    
    protected RenderKazAmmo1(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(KazAmmoEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        {
            entity.currentY += entity.verticalSpeed * partialTicks;
            entity.verticalSpeed = entity.verticalSpeed + 0.2f * partialTicks * (0 - entity.verticalSpeed);

        }
        boolean exploding = entity.countdown <= 2;
        bindEntityTexture(entity);
        float lightMapSaveX = 0;
        float lightMapSaveY = 0;
        if (exploding) {
            lightMapSaveX = OpenGlHelper.lastBrightnessX;
            lightMapSaveY = OpenGlHelper.lastBrightnessY;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
            GlStateManager.disableLighting();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + entity.currentY, z);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.rotate(entity.rotationYaw,0,1,0);
        model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        if (exploding) {
            GlStateManager.enableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(KazAmmoEntity entity) {
        return entity.countdown > 2 ? texture : texture1;
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
