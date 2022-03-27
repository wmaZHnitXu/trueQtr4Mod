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
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderCannonProjectile extends Render<CannonProjectileEntity> {

    public static final ResourceLocation whiteTexture = new ResourceLocation(TdmMod.MODID, "textures/particle/white.png");

    ModelBase model = new ProjModel();

    class ProjModel extends ModelBase {

        private final ModelRenderer cube_r1;

        public ProjModel ()  {

            cube_r1 = new ModelRenderer(this);
		    cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);


            cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -4, -4, -4, 4, 4, 4, 0.0F, false));
        }

    }

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
        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(entity.posX, entity.posY, entity.posZ);
            GlStateManager.disableLighting();
                model.render((Entity)entity, 0.0F, 0.0F, 0F, 0.0F, 0.0F, 0.0625F);
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
