package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.RadarThermal1Entity;
import com.anet.qtr4tdm.common.entities.models.ThermalRadarModel;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderThermalRadar1 extends Render<RadarThermal1Entity>{

    public static final ResourceLocation textures = new ResourceLocation(TdmMod.MODID + ":textures/entity/thermalradaron.png");
    public final ThermalRadarModel model = new ThermalRadarModel();

    public RenderThermalRadar1(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(RadarThermal1Entity entity) {
        return textures;
    }

    @Override
    public void doRender(RadarThermal1Entity entity, double x, double y, double z, float entityYaw,
            float partialTicks) {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            bindEntityTexture(entity);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5d, y + 1.5d, z + 0.5d);
            //GlStateManager.translate(x + 0.5d, y + 1.5d, z + 0.5d);
            GlStateManager.rotate(180, 1, 0, 0);
            model.SetTopRotation(entity.rot);
            model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
    }
}
