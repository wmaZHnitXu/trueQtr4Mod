package com.anet.qtr4tdm.uebki.gui;

import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.HarvesterDroneEntity;
import com.anet.qtr4tdm.common.supers.DroneSmallEntity;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class DroneBaseGui extends GuiContainer {

    DroneBaseTile tile;
    DroneSmallEntity drone;
    static final ResourceLocation texture = new ResourceLocation(TdmMod.MODID, "textures/gui/dronebase.png");

    List<DroneSmallEntity> forcedToMakeThisVar;

    public DroneBaseGui(InventoryPlayer inventory, DroneSmallEntity drone, DroneBaseTile tile) {
        super(new DroneContainer(inventory, drone, tile));
        this.tile = tile;
        this.drone = drone;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(texture);
        drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, 176, 166, 176, 166, 256, 256);
        int progress = 0;
        if (tile.NetworkMaxEnergy > 0)
            progress = Double.valueOf(Double.valueOf(tile.NetworkEnergy) / tile.NetworkMaxEnergy * 53).intValue();
        drawScaledCustomSizeModalRect(guiLeft + 31, guiTop + 66, 176, 0, progress, 12, progress, 12, 256, 256);
        if (drone == null) {
            drawScaledCustomSizeModalRect(guiLeft + 7, guiTop + 7, 0, 166, 162, 54, 162, 54, 256, 256);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(this.fontRenderer, tile.NetworkEnergy + " / " + tile.NetworkMaxEnergy + " Eu", 90, 68, 0xAAAAAAAA);
        if ((drone != null && drone.isDead) || tile == null || tile.isInvalid())  Minecraft.getMinecraft().displayGuiScreen(null);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
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
