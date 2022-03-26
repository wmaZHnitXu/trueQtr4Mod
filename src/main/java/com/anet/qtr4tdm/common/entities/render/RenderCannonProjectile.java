package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.CannonProjectileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCannonProjectile extends Render<CannonProjectileEntity> {

    public static final ResourceLocation whiteTexture = new ResourceLocation(TdmMod.MODID, "textures/particle/white.png");

    ModelBase model = new ModelEnderCrystal(0.0625f, false);

    protected RenderCannonProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(CannonProjectileEntity entity) {
        return whiteTexture;
    }

    @Override
    public void doRender(CannonProjectileEntity entity, double x, double y, double z, float entityYaw,
            float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.translate(entity.posX, entity.posY, entity.posZ);
            GlStateManager.disableLighting();
                model.render(null, 1, 1, 1, 1, 1, 0.0625f);
            GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<CannonProjectileEntity> {

        @Override
        public Render createRenderFor(RenderManager manager) {
            return new RenderCannonProjectile(manager);
        }
        
    }
    
}
