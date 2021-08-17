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
    public int TexH = 32, TexW = 32;
    public String name = "Участники";
    private boolean hoverExitButton;
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

    public void keyTyped(char typedChar, int keyCode)  {

    }

    public void Update () {

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
                alpha = lerp(alpha, 0, 0.5d);
            }
            else {
                alpha = 0;
                if (deployProgress > 0.01d) {
                    deployProgress = lerp(deployProgress, 0, 0.5d);
                }
                else deployProgress = 0;
            }
        }

        hoverExitButton = (mouseX > xPos + DeployedWidth - 10) && (mouseX < xPos + DeployedWidth - 5) && mouseY > yPos + 5 && mouseY < yPos + 10;

        if (deployProgress != 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();

            drawRect(xPos + 1, yPos + 1, xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth) * deployProgress).intValue() - 1,
            yPos + Double.valueOf(iconHeight + (DeployedHeight - iconHeight) * deployProgress).intValue() - 1, 0xFF333333);

            

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
            if (deployProgress == 1) drawContent(mouseX, mouseY);
            GlStateManager.popMatrix();
            drawRect(xPos + 1, yPos + 1, xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth  - 1) * deployProgress).intValue(), //ДАДА АХУЕННОЕ РЕШЕНИЕ
            yPos + Double.valueOf(iconHeight + (DeployedHeight - iconHeight  - 1) * deployProgress).intValue(), 0x00333333 + Double.valueOf((1 - alpha) * 255).intValue() * 16777216);

            drawRect(xPos, yPos,xPos + Double.valueOf(iconWidth + (DeployedWidth - iconWidth) * deployProgress).intValue(),
            yPos + 15, 0xFF111111);
            if (deployProgress > 0.60f) drawCenteredString(mc.fontRenderer, name, xPos + DeployedWidth / 2, yPos + 4, 0xFFAAAAAA);
            if (deployProgress > 0.94f) drawRect(xPos + DeployedWidth - 10 , yPos + 5, xPos + DeployedWidth - 5, yPos + 10, Double.valueOf((hoverExitButton ? 0xFFFFDDDD : 0xFFFF1111)).intValue());

            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.popMatrix();
        }
        else {
            mc.renderEngine.bindTexture(iconLoc);
            drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, 32, 32, 32, 32);
        }
    }

    public void drawContent (int mouseX, int mouseY) {
        drawString(mc.fontRenderer, "СУКА", 20, 20, 0xFFFFFF);
    }

    public void Deploy () {
        deployed = true;
    }

    public void Minimize () {
        deployed = false;
    }

    public void MouseClick (int x, int y) {
        if (x > xPos && x < xPos + 32 && y > yPos && y < yPos + 32 && !deployed) Deploy();
        if (hoverExitButton) Minimize();
    }

    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }
}
