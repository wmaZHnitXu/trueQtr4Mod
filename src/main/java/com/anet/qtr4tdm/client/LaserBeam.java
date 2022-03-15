package com.anet.qtr4tdm.client;

import com.anet.qtr4tdm.TdmMod;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class LaserBeam {

    public final static ResourceLocation whiteTexture = new ResourceLocation(TdmMod.MODID, "textures/particle/white.png");

    public static void RenderLaserBeam (float partialTicks, Vec3d origin, Vec3d destination, float width, Vec3d up, Vec3d right) {

        final Entity viewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTexture);
        if (viewEntity == null)
            return;
        Vec3d viewPos = new Vec3d(viewEntity.prevPosX + (viewEntity.posX - viewEntity.prevPosX) * partialTicks,
                viewEntity.prevPosY + (viewEntity.posY - viewEntity.prevPosY) * partialTicks,
                viewEntity.prevPosZ + (viewEntity.posZ - viewEntity.prevPosZ) * partialTicks);

        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        GlStateManager.color(1, 0, 0, 1f);

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            
            Vec3d pos0 = new Vec3d(origin.x - viewPos.x, origin.y - viewPos.y, origin.z - viewPos.z);
            Vec3d pos1 = new Vec3d(destination.x - viewPos.x, destination.y - viewPos.y, destination.z - viewPos.z);
            final Vec3d up0 = up;
            final Vec3d right0 = right;
            final Vec3d up1 = up;
            final Vec3d right1 = right;

            vertexbuffer
                    .pos(pos0.x + (-right0.x) * width, pos0.y + (-right0.y) * width, pos0.z + (-right0.z) * width)
                    .tex(0.0D, 1D).endVertex();
            vertexbuffer.pos(pos0.x + (right0.x) * width, pos0.y + (right0.y) * width, pos0.z + (right0.z) * width)
                    .tex(0.0D, 0D).endVertex();
            vertexbuffer.pos(pos1.x + (right1.x) * width, pos1.y + (right1.y) * width, pos1.z + (right1.z) * width)
                    .tex(1.0D, 0D).endVertex();
            vertexbuffer
                    .pos(pos1.x + (-right1.x) * width, pos1.y + (-right1.y) * width, pos1.z + (-right1.z) * width)
                    .tex(1D, 1D).endVertex();

            vertexbuffer.pos(pos0.x + (-up0.x) * width, pos0.y + (-up0.y) * width, pos0.z + (-up0.z) * width)
                    .tex(0.0D, 1D).endVertex();
            vertexbuffer.pos(pos0.x + (up0.x) * width, pos0.y + (up0.y) * width, pos0.z + (up0.z) * width)
                    .tex(1D, 0D).endVertex();
            vertexbuffer.pos(pos1.x + (up1.x) * width, pos1.y + (up1.y) * width, pos1.z + (up1.z) * width)
                    .tex(1D, 1D).endVertex();
            vertexbuffer.pos(pos1.x + (-up1.x) * width, pos1.y + (-up1.y) * width, pos1.z + (-up1.z) * width)
                    .tex(0.0D, 0D).endVertex();
        
        tessellator.draw();

        GlStateManager.popMatrix();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

    }
    
}
