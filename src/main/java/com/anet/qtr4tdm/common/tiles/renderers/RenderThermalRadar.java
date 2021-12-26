package com.anet.qtr4tdm.common.tiles.renderers;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.models.ThermalRadarModel;
import com.anet.qtr4tdm.common.tiles.ThermalBaseTile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderThermalRadar extends TileEntitySpecialRenderer<ThermalBaseTile> {
    
    public static final ResourceLocation textureon = new ResourceLocation(TdmMod.MODID + ":textures/entity/thermalradaron.png");
    public static final ResourceLocation textureoff = new ResourceLocation(TdmMod.MODID + ":textures/entity/thermalradaroff.png");
    public static final ResourceLocation texture_e = new ResourceLocation(TdmMod.MODID + ":textures/entity/thermalradaron_e.png");
    public final ThermalRadarModel model = new ThermalRadarModel();

    @Override
    public void render(ThermalBaseTile te, double x, double y, double z, float partialTicks, int destroyStage,
            float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);
        bindTexture(te.powered ? textureon : textureoff);
        GlStateManager.pushMatrix(); {

            GlStateManager.translate(x + 0.5d, y + 1.5d, z + 0.5d);
            GlStateManager.rotate(180, 1, 0, 0);
            model.bone.render(0.0625F);
            GlStateManager.pushMatrix(); {
                if (te.powered) te.SetRotation(te.getRotation() + 1f * partialTicks);
                GlStateManager.rotate(te.getRotation(), 0, 1, 0);
                model.rotatin.render(0.0625F);
                if (te.powered) {
                GlStateManager.pushMatrix(); {

                    float lightMapSaveX = OpenGlHelper.lastBrightnessX;
                    float lightMapSaveY = OpenGlHelper.lastBrightnessY;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
                    GlStateManager.disableLighting();
                    bindTexture(texture_e);
                    model.rotatin.render(0.0625F);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.enableLighting();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);

                } GlStateManager.popMatrix();
                }

            } GlStateManager.popMatrix();

        } GlStateManager.popMatrix();
    }
    
}
