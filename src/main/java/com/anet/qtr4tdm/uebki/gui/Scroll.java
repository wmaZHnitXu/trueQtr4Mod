package com.anet.qtr4tdm.uebki.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Scroll extends Gui {
    public final int offset = 1;
    public final int sectionHeight = 15;
    public int sectionWidth;
    private Minecraft mc;
    public String[] sections;
    private IScrollest baseGui;
    int highlightIndex = -1;
    public boolean shouldSelect;
    int selectedIndex;
    public int xPos;
    public int yPos;
    public boolean enabled;
    public boolean cull;
    private int currentSroll;
    public int scrollHeight;
    private boolean mouseOnScroll;

    public Scroll (int xPos, int yPos, String[] sections, IScrollest baseGui, Minecraft mc) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sections = sections;
        this.baseGui = baseGui;
        this.mc = mc;
        this.cull = cull;
        sectionWidth = 60;
        selectedIndex = -1;
    }

    public Scroll (int xPos, int yPos, int width, String[] sections, IScrollest baseGui, Minecraft mc, boolean shouldSelect, int scrollHeight) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sections = sections;
        this.baseGui = baseGui;
        this.mc = mc;
        this.sectionWidth = width;
        this.scrollHeight = scrollHeight;
        this.cull = true;
        this.shouldSelect = true;
        selectedIndex = -1;
    }

    public void draw (int mouseX, int mouseY) {
        if (!enabled) return;
        GlStateManager.pushMatrix();
        if (cull) {
            drawRect(xPos, yPos, xPos + sectionWidth, yPos + scrollHeight, 0xFF111111);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            ScaledResolution r = new ScaledResolution(mc);
            int s = r.getScaleFactor();
            int translatedY = r.getScaledHeight() - yPos - scrollHeight;
            GL11.glScissor(xPos * s, translatedY * s, sectionWidth * s, scrollHeight * s);
        }
        highlightIndex = -1;
        for (int i = 0; i < sections.length; i++) {
            int sectionY = yPos + 18 + i * (offset + sectionHeight) + currentSroll;
            int sectionX = xPos;
            boolean highlight = mouseX > sectionX && mouseX <= sectionX + sectionWidth && mouseY > sectionY && mouseY <= sectionY + sectionHeight;
            if (highlight) {
                highlightIndex = i;
            }
            drawRect(sectionX, sectionY, sectionX + sectionWidth, sectionY + sectionHeight, highlight || (selectedIndex == i && shouldSelect) ? 0xFF666666 : 0xFF333333);
            String finalString = sections[i];
                while (mc.fontRenderer.getStringWidth(finalString) > sectionWidth - 4) {
                    finalString = finalString.substring(0, finalString.length()-1);
                    if (mc.fontRenderer.getStringWidth(finalString) <= sectionWidth - 4) {
                        finalString = finalString.substring(0, finalString.length()-3) + "...";
                    }
                }
                drawString(mc.fontRenderer, finalString, sectionX + 2, sectionY + 4, 0xFFFFFFFF);

        }

        mouseOnScroll = highlightIndex != -1 || !(cull && (mouseX > xPos + sectionWidth || mouseX < xPos) || (mouseY > yPos + scrollHeight || mouseY < yPos));

        if (cull) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        GlStateManager.popMatrix();
    }

    public void Click (int mouseX, int mouseY) {
        if (!enabled) return;
        if (cull && (mouseX > xPos + sectionWidth || mouseX < xPos) || (mouseY > yPos + scrollHeight || mouseY < yPos)) return;

        if (highlightIndex != -1) {
            baseGui.ScrollClick(highlightIndex);
            selectedIndex = highlightIndex;
        }
    }

    public void handleMouseWheel (int mouseX, int mouseY, int delta) {
        if (!cull || !mouseOnScroll) return;
        currentSroll += delta;
        if (currentSroll > 0) currentSroll = 0;
    }
}
