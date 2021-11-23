package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;
import com.anet.qtr4tdm.uebki.gui.KAZGuiMisc.kazContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class KazGui extends GuiContainer {

    private Kaz1Tile te;

    final private ResourceLocation background = new ResourceLocation(TdmMod.MODID, "textures/gui/kaz.png");

    public KazGui(InventoryPlayer playerInventory, Kaz1Tile tile) {
        super(new kazContainer(playerInventory, tile));
        te = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(background);
        drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, 176, 166, 176, 166, 256, 256);
    }

    @Override
    public int getXSize() {
        return 200;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }   
}
