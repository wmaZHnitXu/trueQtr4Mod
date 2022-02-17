package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.HarvesterDroneEntity;
import com.anet.qtr4tdm.common.entities.models.harvesterdrone1;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderHarvesterDrone1 extends Render<HarvesterDroneEntity> {

    private final ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/harvesterdrone1.png");
    private final harvesterdrone1 model = new harvesterdrone1();

    protected RenderHarvesterDrone1(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(HarvesterDroneEntity entity) {
        return texture;
    }

    @Override
    public void doRender(HarvesterDroneEntity entity, double x, double y, double z, float entityYaw,
            float partialTicks) {
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y+1.5d, z);
        GlStateManager.rotate(180, 1, 0, 0);
        if (entity.isMoving()) {
            entity.vaintRotation += partialTicks;
        }
        model.setVaintsRotation(Double.valueOf(entity.vaintRotation).floatValue());
        model.render((Entity)entity, 0, 0f, 0, entityYaw, partialTicks, 0.0625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<HarvesterDroneEntity>
	{
		@Override
		public Render<HarvesterDroneEntity> createRenderFor(RenderManager manager)
		{
			return new RenderHarvesterDrone1(manager);
		}
	}
    
}
