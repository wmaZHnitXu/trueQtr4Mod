package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.internal.Trees.Return;

import com.anet.qtr4tdm.TdmMod;

import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class OverlayGui {

    public final static ResourceLocation iconsLocation = new ResourceLocation(TdmMod.MODID, "textures/gui/overlayicons.png");
    private static TopBarContentInfo topBar = null;
    public static OverlayGui instance;
    private static FontRenderer renderer;
    private static Minecraft mc;
    private static TextureManager textureManager;
    private static int Xcenter;
    private static int Ycenter;
    private static float alpha;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void Render (RenderGameOverlayEvent evt) {
        if (evt.getType() == ElementType.TEXT) {
            ScaledResolution r = evt.getResolution();
            Xcenter = r.getScaledWidth() / 6;
            Ycenter = r.getScaledHeight() / 5;

            if (topBar != null) { topBar.countdown--;
                
                if (mc == null) mc = Minecraft.getMinecraft();
                if (renderer == null) renderer = Minecraft.getMinecraft().fontRenderer;
                if (textureManager == null) textureManager = mc.getTextureManager();

                renderTopBar(topBar);

                if (topBar.countdown <= 100) alpha = lerp(alpha, 0, 0.03f);
                else alpha = lerp(alpha, 1, 0.05f);
                if (topBar.countdown <= 0) topBar = null;
            }
        }
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static void SetTopBar (String title, String sub, TopBarIcon icon) {
        SetTopBar(title, sub, icon, 600);
    }

    public static void SetTopBar (String title, String sub, TopBarIcon icon, int time) {
        topBar = new TopBarContentInfo(title, sub, time, icon);
    }

    public static void renderTopBar (TopBarContentInfo bar) {
        GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1, 1, 1, alpha);
            int iconX = (bar.countdown / 4 % 16) * 16;
            int iconY = bar.icon.getTexCoord() * 16;
            textureManager.bindTexture(iconsLocation);
            Gui.drawModalRectWithCustomSizedTexture(Xcenter, Ycenter, iconX, iconY, 16, 16, 256, 256);
            renderer.drawString("§l" + bar.title, Xcenter, Ycenter + 20, bar.icon.getTextColor() + Float.valueOf(0xFF * alpha).intValue() * 16777216, true);
            renderer.drawString(bar.subtitle, Xcenter, Ycenter + 35, 0xFFFFFF + Float.valueOf(0xFF * alpha).intValue() * 16777216, false);
            //GlStateManager.disableBlend();
            //GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }

    static class TopBarContentInfo {

        public String title;
        public String subtitle;
        public TopBarIcon icon;
        public int countdown;

        public TopBarContentInfo (String tit, String sub, int time) {
            title = tit;
            subtitle = sub;
            countdown = time;
            icon = TopBarIcon.None;
        }

        public TopBarContentInfo (String tit, String sub, int time, TopBarIcon icon) {
            this(tit, sub, time);
            this.icon = icon;
        }
    }

    public enum TopBarIcon {
        None,
        Alarm,
        Attention;

        public int getTexCoord () {
            if (this == None) return 240;
            return this.ordinal() - 1;
        }

        public static TopBarIcon getIconFromId (int id) {
            return TopBarIcon.values()[id];
        }

        public int getTextColor () {
            switch (this) {
                case Alarm: return 0xFF4444;
                case Attention: return 0xDDDDDD;
                default: return 0xFFFFFF;
            }
        }
    }
}
