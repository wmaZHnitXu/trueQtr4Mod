package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.LaserTurretEntity;
import com.anet.qtr4tdm.common.entities.models.laserturret;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderLaserTurret extends Render<LaserTurretEntity> {

    public final static ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/laserturret.png");
    public final static ResourceLocation texture_e = new ResourceLocation(TdmMod.MODID + ":textures/entity/laserturret_e.png");
    public final laserturret model = new laserturret();

    protected RenderLaserTurret(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(LaserTurretEntity entity) {
        return texture;
    }

    @Override
    public void doRender(LaserTurretEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        entity.calculatePitchYaw(partialTicks);
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 1.5d, z);
        GlStateManager.rotate(180, 1, 0, 0);
        model.SetRotationTurret(entity.getYawTurret().floatValue(), entity.getPitchTurret().floatValue());
        model.render(entity, 0.0F, 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.pushMatrix();
                float lightMapSaveX = OpenGlHelper.lastBrightnessX;
                float lightMapSaveY = OpenGlHelper.lastBrightnessY;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.enableAlpha();
                GlStateManager.disableLighting();
                bindTexture(texture_e);
                GlStateManager.color(1.0F, 1.0F, 1.0F, entity.charge);
                model.render(entity, 0.0F, 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.disableAlpha();
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
            GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<LaserTurretEntity>
	{
		@Override
		public Render<LaserTurretEntity> createRenderFor(RenderManager manager)
		{
			return new RenderLaserTurret(manager);
		}
	}
    
}
