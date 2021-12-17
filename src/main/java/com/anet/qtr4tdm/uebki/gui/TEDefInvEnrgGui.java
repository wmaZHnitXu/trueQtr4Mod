package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.supers.TEDefenceInvEnrg;
import com.anet.qtr4tdm.uebki.gui.TEDefInvEnrgGuiMisc.TEDefContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class TEDefInvEnrgGui extends GuiContainer {
    private TEDefenceInvEnrg te;

    final private ResourceLocation background = new ResourceLocation(TdmMod.MODID, "textures/gui/kaz.png");

    public TEDefInvEnrgGui(InventoryPlayer playerInventory, TEDefenceInvEnrg tile) {
        super(new TEDefContainer(playerInventory, tile));
        te = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        mc.renderEngine.bindTexture(background);
        drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, 176, 166, 176, 166, 256, 256);
        drawString(fontRenderer, te.getField(0) == 1 ? "Подключен к базе." : "§cОтключен от базы.", guiLeft + 7, guiTop + 5, 0xFFFFFFFF);
        drawString(fontRenderer, te.getField(1) == 1 ? "Запитан (1eu/t)." : "§cОбесточен.", guiLeft + 7, guiTop + 15, 0xFFFFFFFF);
    }

    @Override
    public int getXSize() {
        return 200;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }   
}
