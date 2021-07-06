package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.Radar3Entity;
import com.anet.qtr4tdm.common.entities.models.Radar3Model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderRadar3 extends Render<Radar3Entity> {
    public static final ResourceLocation textures = new ResourceLocation(TdmMod.MODID + ":textures/entity/radar3.png");
    public final Radar3Model model = new Radar3Model();

    public RenderRadar3(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(Radar3Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5d, y + 1.5d, z + 0.5d);
        GlStateManager.rotate(180, 1, 0, 0);
        entity.rot += entity.getRotSpeed();
        model.SetCrutilkiRotation(entity.rot);
        model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Radar3Entity entity) {
        return textures;
    }
}