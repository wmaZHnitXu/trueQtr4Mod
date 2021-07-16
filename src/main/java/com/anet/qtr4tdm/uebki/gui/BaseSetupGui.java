package com.anet.qtr4tdm.uebki.gui;

import java.io.IOException;

import javax.swing.text.AbstractDocument.BranchElement;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.tiles.BaseTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.MapItemRenderer;

public class BaseSetupGui extends GuiScreen{

    public static final ResourceLocation bgTexture1 = new ResourceLocation(TdmMod.MODID, "textures/gui/basesetupfirst.png");
    public static final ResourceLocation bgTexture2 = new ResourceLocation(TdmMod.MODID, "textures/gui/basesetupsecond.png");
    private final int GuiHeight = 220;
    private final int GuiWidth = 240;
    private int page;
    private final int maxPage = 3;
    String title;
    String[] content;
    String smoll;
    private BaseTile tile;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(page == 0 ? bgTexture1 : bgTexture2);
        int centerX = width / 2 - GuiWidth / 2;
        int centerY = height / 2 - GuiHeight / 2;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, GuiWidth, GuiHeight);
        fontRenderer.drawString(title, centerX + (page == 0 ? 100 : 25), centerY + 15, 0x202020);
        GlStateManager.pushMatrix();
            Float scale = 0.7f;
            GlStateManager.scale(scale, scale, scale);
            for (int i = 0; i < content.length; i++) {
                fontRenderer.drawString(content[i], Float.valueOf((centerX + (page == 0 ? 90 : 15)) / scale).intValue(), Float.valueOf((centerY + 45 + i * 10) / scale).intValue(), 0x202020);
            }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
            scale = 0.5f;
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(smoll, Float.valueOf((centerX + (page == 0 ? 90 : 15)) / scale).intValue(), Float.valueOf((centerY + 173) / scale).intValue(), 0x202020);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int centerX = width / 2;
        int centerY = height / 2;
        buttonList.add(new ShindowsButton(0, centerX + 65, centerY + 85, "Отмена"));
        buttonList.add(new ShindowsButton(1, centerX + 10, centerY + 85, "Далее >>"));
        buttonList.add(new ShindowsButton(2, centerX - 45, centerY + 85, "<< Назад"));
        content = new String[2];
        UpdateContent();
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: mc.displayGuiScreen(null); break;
            case 1: page++; break;
            case 2: page--; break;
        }
        UpdateContent();
    }

    private void UpdateContent () {
        if (page > maxPage) { Conclusion(); return;}
        switch (page) {
            case 0:
                title = "§lМастер установки базы";
                content = new String[]{"Данный мастер поможет установить", "базу на данную землю."};
                smoll = "Для продолжения нажмите \"Далее\"";

            break;
            case 1:
                title = "Начало установки";
                content = new String[]{"Позиция: " + tile.getPos(),
                    "Базовый чанк: " + InWorldBasesManager.ChunkPosFromBlockPos(tile.getPos()),
                    "Владелец: " + mc.getSession().getUsername(),
                    "Первичное количество оборонительных орудий: 8", "вВЫВЫВфывфывфывфыв", "Для продолжения нажмите кнопку \"Далее\"."};
            break;
        }
        if (page == 0) buttonList.get(2).enabled = false; else buttonList.get(2).enabled = true;
        if (page == maxPage) buttonList.get(1).displayString = "Готово"; else buttonList.get(1).displayString = "Далее >>";
    }

    private void Conclusion () {
        mc.displayGuiScreen(null);
    }
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    class ShindowsButton extends GuiButton {

        final static int buttonWidth = 50;
        final static int buttonHeight = 16;

        public ShindowsButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, buttonWidth, buttonHeight, buttonText);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible)
                {
                    FontRenderer fontrenderer = mc.fontRenderer;
                    mc.getTextureManager().bindTexture(bgTexture1);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    int texY = 220;
                    int stateTexX = 0;
                    if (this.hovered) stateTexX = 50;
                    if (!this.enabled) stateTexX = 100;
                    this.drawTexturedModalRect(this.x, this.y, stateTexX, texY, buttonWidth, buttonHeight);
                    this.mouseDragged(mc, mouseX, mouseY);
                    int j = 0x343434;

                    if (packedFGColour != 0)
                    {
                        j = 0x343434;
                    }
                    else
                    if (!this.enabled)
                    {
                        j = 0xacacac;
                    }
                    else if (this.hovered)
                    {
                        j = 0x343434;
                    }

                    fontrenderer.drawString( this.displayString, this.x + this.width / 2 - fontrenderer.getStringWidth(this.displayString) / 2, this.y + (this.height - 8) / 2, j, false);
                }
            }
    }
}