package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.client.LaserBeam;
import com.anet.qtr4tdm.common.entities.LaserTurretEntity;
import com.anet.qtr4tdm.common.entities.models.laserturret;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderLaserTurret extends Render<LaserTurretEntity> {

    public final static ResourceLocation texture = new ResourceLocation(TdmMod.MODID + ":textures/entity/laserturret.png");
    public final static ResourceLocation texture_e = new ResourceLocation(TdmMod.MODID + ":textures/entity/laserturret_e.png");
    public final laserturret model = new laserturret();
    public static int counter;
    public static int counter2;

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

        //LASER BEAM RENDER

        if (entity.charge > 0.8d) {
            double pitchSin = Math.sin(entity.getPitchTurret());
            double pitchCos = Math.cos(entity.getPitchTurret());
            Vec3d clientLaserDist = new Vec3d(Math.cos(entity.getYawTurret()), -pitchSin / pitchCos ,Math.sin(entity.getYawTurret())).normalize().scale(500);
            clientLaserDist = clientLaserDist.add(entity.getGunPosition());
            Vec3d clientLaserOrigin = entity.getGunPosition().add(clientLaserDist.subtract(entity.getGunPosition()).scale(0.5d / 500));
            RayTraceResult rtres = entity.world.rayTraceBlocks(clientLaserOrigin, clientLaserDist);

            int ctr2 = Float.valueOf((entity.charge - 0.8f) * 5 * 100).intValue();
            if (rtres != null) {
                clientLaserDist = rtres.hitVec;
                if (rtres.typeOfHit == Type.BLOCK && counter2 > 100) {
                    counter2 = 0;
                    counter++;
                    entity.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, clientLaserDist.x, clientLaserDist.y, clientLaserDist.z, entity.getGaussianRandom() * 0.01d, entity.getGaussianRandom() * 0.01d, entity.getGaussianRandom() * 0.01d);
                    if (counter % 2 == 0)
                        entity.world.spawnParticle(EnumParticleTypes.LAVA, true, clientLaserDist.x, clientLaserDist.y, clientLaserDist.z, entity.getGaussianRandom() * 0.02d, entity.getGaussianRandom() * 0.02d, entity.getGaussianRandom() * 0.02d);
                }
                counter2 += ctr2;
            }

            LaserBeam.RenderLaserBeam(partialTicks, clientLaserOrigin, clientLaserDist, Math.max((entity.charge - 0.8f) * 0.8f, 0), new Vec3d(0,1,0), new Vec3d(0,0,1));
        }
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
