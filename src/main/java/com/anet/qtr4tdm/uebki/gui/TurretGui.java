package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class TurretGui extends GuiContainer {

    public static final ResourceLocation texture = new ResourceLocation(TdmMod.MODID, "textures/gui/dronebase.png");
    private TurretMasterTe tile;

    public TurretGui(Container inventorySlotsIn, TurretMasterTe tile) {
        super(inventorySlotsIn);
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, 176, 166, 176, 166, 256, 256);
        int progress = 0;
        if (tile.NetworkMaxEnergy > 0)
            progress = Double.valueOf(Double.valueOf(tile.NetworkEnergy) / tile.NetworkMaxEnergy * 53).intValue();
        drawScaledCustomSizeModalRect(guiLeft + 31, guiTop + 66, 176, 0, progress, 12, progress, 12, 256, 256);
    }
}
