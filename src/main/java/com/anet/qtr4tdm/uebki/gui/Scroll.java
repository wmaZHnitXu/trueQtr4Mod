package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
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
    private int xPos;
    private int yPos;
    public boolean enabled;

    public Scroll (int xPos, int yPos, String[] sections, IScrollest baseGui, Minecraft mc) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sections = sections;
        this.baseGui = baseGui;
        this.mc = mc;
        sectionWidth = 60;
        selectedIndex = -1;
    }

    public Scroll (int xPos, int yPos, int width, String[] sections, IScrollest baseGui, Minecraft mc, boolean shouldSelect) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.sections = sections;
        this.baseGui = baseGui;
        this.mc = mc;
        this.sectionWidth = width;
        selectedIndex = -1;
    }

    public void draw (int mouseX, int mouseY) {
        if (!enabled) return;
        
        GlStateManager.pushMatrix();
        highlightIndex = -1;
        for (int i = 0; i < sections.length; i++) {
            int sectionY = yPos + 18 + i * (offset + sectionHeight);
            int sectionX = xPos;
            boolean highlight = mouseX > sectionX && mouseX <= sectionX + sectionWidth && mouseY > sectionY && mouseY <= sectionY + sectionHeight;
            if (highlight) {
                highlightIndex = i;
            }
            drawRect(sectionX, sectionY, sectionX + sectionWidth, sectionY + sectionHeight, highlight || (selectedIndex == i && shouldSelect) ? 0xFF666666 : 0xFF111111);
            String finalString = sections[i];
                while (mc.fontRenderer.getStringWidth(finalString) > sectionWidth - 4) {
                    finalString = finalString.substring(0, finalString.length()-1);
                    if (mc.fontRenderer.getStringWidth(finalString) <= sectionWidth - 4) {
                        finalString = finalString.substring(0, finalString.length()-3) + "...";
                    }
                }
                drawString(mc.fontRenderer, finalString, sectionX + 2, sectionY + 4, 0xFFFFFFFF);

        }
        GlStateManager.popMatrix();
    }

    public void Click (int mouseX, int mouseY) {
        if (!enabled) return;

        if (highlightIndex != -1) {
            baseGui.ScrollClick(highlightIndex);
            selectedIndex = highlightIndex;
        }
    }
}
