package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiWidget extends Gui {
    public boolean rightSide;
    public boolean deployed;
    public double deployProgress = 0;
    public double alpha = 0;
    public int xPos, yPos;
    private ResourceLocation iconLoc;
    private int YonTexIcon = 75, XonTexIcon = 367;
    public int iconWidth = 32, iconHeight = 32;
    public int DeployedWidth, DeployedHeight;
    public int TexH = 512, TexW = 512;
    public String name = "Участники";
    public Minecraft mc;

    public GuiWidget (boolean rightSide, int x, int y, int dwidth, int dheight, Minecraft mc, ResourceLocation icon) {
        super();
        this.rightSide = rightSide;
        this.xPos = x;
        this.yPos = y;
        this.DeployedWidth = dwidth;
        this.DeployedHeight = dheight;
        this.mc = mc;
        this.iconLoc = icon;
    }

    public void draw (int mouseX, int mouseY) {
        if (deployed) {
            if (deployProgress < 0.99) {
                deployProgress = lerp(deployProgress, 1, 0.05d);
            }
            else {
                deployProgress = 1;
                if (alpha < 0.99) {
                    alpha = lerp(alpha, 1, 0.1d);
                }
                else alpha = 1;
            }
        }
        else {
            if (alpha > 0.01d) {
                alpha = lerp(alpha, 0, 0.1d);
            }
            else {
                alpha = 0;
                if (deployProgress > 0.01d) {
                    deployProgress = lerp(deployProgress, 0, 0.05d);
                }
                else deployProgress = 0;
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        drawRect(xPos, yPos,xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth) * deployProgress).intValue(),
        yPos + Double.valueOf(iconHeight + (DeployedHeight - iconHeight) * deployProgress).intValue(), 0xAA50ff96);

        drawRect(xPos + 2, yPos + 2, xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth) * deployProgress).intValue() - 2,
        yPos + Double.valueOf(iconHeight + (DeployedHeight - iconHeight) * deployProgress).intValue() - 2, 0xFF333333);

        mc.renderEngine.bindTexture(iconLoc);
        drawModalRectWithCustomSizedTexture(xPos, yPos, XonTexIcon, YonTexIcon, iconWidth, iconHeight, TexW, TexH);

        /*GlStateManager.pushMatrix(); ЧЗХ Я ЕБАЛ ПОЧЕМУ НЕ РАБОТАЕТ
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.translate(xPos, yPos, 0);
                drawContent();
                GlStateManager.color(1, 1, 1, Double.valueOf(alpha).floatValue());
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
        */

       
        GlStateManager.pushMatrix();
        GlStateManager.translate(xPos, yPos, 0);
        if (deployProgress == 1) drawContent();
        GlStateManager.popMatrix();
        drawRect(xPos + 2, yPos + 2, xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth) * deployProgress).intValue() - 2, //ДАДА АХУЕННОЕ РЕШЕНИЕ
        yPos + Double.valueOf(iconHeight + (DeployedHeight - iconHeight) * deployProgress).intValue() - 2, 0x00333333 + Double.valueOf((1 - alpha) * 255).intValue() * 16777216);

        if (deployed) {
            drawRect(xPos, yPos,xPos + Double.valueOf(((mc.fontRenderer.getStringWidth(name) + 4) * Math.min(deployProgress * 2, 1))).intValue(),
            yPos + 15, 0xFF111111);
            drawString(mc.fontRenderer, name, xPos + 2, yPos + 2, 0x00AAAAAA + Double.valueOf(Math.min(deployProgress * 1.5, 1) * 255).intValue() * 16777216);
        }

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }

    public void drawContent () {
        drawString(mc.fontRenderer, "СУКА", 70, 10, 0xFFFFFF);
    }

    public void Deploy () {
        deployed = true;
    }

    public void Minimize () {
        deployed = false;
    }

    public void Click () {
        if (deployed) Minimize(); else Deploy(); 
    }

    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }
}
