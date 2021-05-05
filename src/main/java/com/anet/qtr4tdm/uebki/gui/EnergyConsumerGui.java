package com.anet.qtr4tdm.uebki.gui;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.EnergyConsumerTile;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.teamState;
import com.anet.qtr4tdm.uebki.messages.BasedRequest;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class EnergyConsumerGui extends GuiScreen {
    
    private EnergyConsumerTile tile;
    private static ResourceLocation background_texture;
    private static int screenWidth;
    private static int screenHeight;
    private static int offsetLeft;
    private static int offsetTop;

    private int counter;

    public EnergyConsumerGui(EnergyConsumerTile tile) {
        this.tile = tile;
        background_texture = new ResourceLocation(TdmMod.MODID+":textures/gui/consumer_background.png");
        counter = 20;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (counter == 20) {
            TdmMod.logger.info("sendbasedreq");
            TdmMod.wrapper.sendToServer(new BasedRequest(tile.getPos(), 0));
            counter = 0;
        }
        else counter++;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glColor4f(1.0F, 1.0f, 1.0f, 1.0F);
        mc.getTextureManager().bindTexture(background_texture);
        screenWidth = 256;
        screenHeight = screenWidth;
        offsetLeft = (int)(width * 0.5 - screenWidth * 0.5f);
        offsetTop = height/5;
        drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, screenWidth, screenHeight);
        teamState team = EnergyConsumerTile.teamS;
        if (team == null) team = teamState.specs;
        int consumption = 10;
        String[] column1 = {
            "§fКоманда: " + Teams.GetTeamColorSymbols(team) + team.toString(),
            "§fЭнергия: " + EnergyConsumerTile.fromServerEnergy + "/10000" + " EU",
            "§fСтатус: " + (EnergyConsumerTile.fromServerEnergy == 10000 ? "Заполнен" : EnergyConsumerTile.fromServerTeamEnergy < EnergyConsumerTile.fromServerMaxTeamEnergy ? "Подача энергии в командный буфер" : "Подзарядка"),
            "§fЭнергия команды: " + EnergyConsumerTile.fromServerTeamEnergy + "/" + EnergyConsumerTile.fromServerMaxTeamEnergy + " EU",
            "§fПотребление: " + consumption,
            "§fПроизводительность: 10000"
        };
        for (int i = 0; i < column1.length; i++) {
            GL11.glColor4f(1.0F, 1.0f, 1.0f, 1.0F);
            fontRenderer.drawString(column1[i], offsetLeft + 20, offsetTop + 20 + i * 20, 0);
        }
    }
}
