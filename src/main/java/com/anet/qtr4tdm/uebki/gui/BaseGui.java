package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.BaseTile;
import com.anet.qtr4tdm.uebki.gui.baseGuiMisc.BaseContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class BaseGui extends GuiContainer {

    public final BaseTile te;
    public final InventoryPlayer player;
    private final ResourceLocation background = new ResourceLocation(TdmMod.MODID, "textures/gui/furnace.png");

    public BaseGui(InventoryPlayer playerInventory, BaseTile baseTe) {
        super(new BaseContainer(playerInventory, baseTe));
        player = playerInventory;
        te = baseTe;

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(background);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    
}
