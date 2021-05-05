package com.anet.qtr4tdm.common.entities.render;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.Radar2Entity;
import com.anet.qtr4tdm.common.entities.Radar3Entity;
import com.anet.qtr4tdm.common.entities.models.Radar2Model;
import com.anet.qtr4tdm.common.entities.models.Radar3Model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class RenderRadar2 extends Render<Radar2Entity> {
    public static final ResourceLocation textures = new ResourceLocation(TdmMod.MODID + ":textures/entity/radar2.png");
    public final Radar2Model model = new Radar2Model();

    public RenderRadar2(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(Radar2Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
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
    protected ResourceLocation getEntityTexture(Radar2Entity entity) {
        return textures;
    }
}