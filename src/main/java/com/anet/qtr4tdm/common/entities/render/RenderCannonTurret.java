package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.CannonTurretEntity;
import com.anet.qtr4tdm.common.entities.models.cannonturret;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCannonTurret extends Render<CannonTurretEntity> {

    public final static ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/cannonturret.png");
    public final cannonturret model = new cannonturret();

    protected RenderCannonTurret(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(CannonTurretEntity entity) {
        return texture;
    }

    @Override
    public void doRender(CannonTurretEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        entity.calculatePitchYaw(partialTicks);
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + 1.5d, z);
        GlStateManager.rotate(180, 1, 0, 0);
            model.setGunKnockBack(entity.getKnockBack());
            model.setTurretAndGunRotation(entity.getYawTurret().floatValue(), entity.getPitchTurret().floatValue());
            model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<CannonTurretEntity> {

        @Override
        public Render<? super CannonTurretEntity> createRenderFor(RenderManager manager) {

            return new RenderCannonTurret(manager);
            
        }
        
    }
    
}
