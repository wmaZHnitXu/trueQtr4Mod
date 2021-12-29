package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.MrkAmmoEntity;
import com.anet.qtr4tdm.common.entities.models.mrkammo1;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderMrkRocket1 extends Render<MrkAmmoEntity> {

    public final ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/mrkammo1.png");
    public final ResourceLocation texture_emission = new ResourceLocation(TdmMod.MODID + ":textures/entity/mrkammo1_e.png");
    public final mrkammo1 model = new mrkammo1();

    protected RenderMrkRocket1(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(MrkAmmoEntity entity) {
        return texture;
    }

    @Override
    public void doRender(MrkAmmoEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            //GlStateManager.rotate(90, 0, 0, 1);
            GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
            GlStateManager.rotate(entity.rotationPitch, 1, 0, 0);
            GlStateManager.pushMatrix();
                model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
            if (entity.trail != null) {
                entity.trail.render(x, y, z, partialTicks);
                entity.trail.onRenderTick();
            }
        GlStateManager.popMatrix();
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

    }

    public static class Factory implements IRenderFactory<MrkAmmoEntity> {

        @Override
        public Render<MrkAmmoEntity> createRenderFor(RenderManager manager) {
            return new RenderMrkRocket1(manager);
        }
        
    }
    
}
