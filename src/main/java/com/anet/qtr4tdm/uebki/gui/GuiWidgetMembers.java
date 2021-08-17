package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiWidgetMembers extends GuiWidget {


    private InputFieldDownScroll inp;
    private ArrayList<GuiButton> buttonList;

    public GuiWidgetMembers(boolean rightSide, int x, int y, int dwidth, int dheight, Minecraft mc,
            ResourceLocation icon) {
        super(rightSide, x, y, dwidth, dheight, mc, icon);
        inp = new InputFieldPlayers(0, mc.fontRenderer, xPos + 20, yPos + 20, 60, 15);
        buttonList = new ArrayList<GuiButton>();
        buttonList.add(new SimpleButton(0, xPos + 85, yPos + 20, 15, 15, "+"));
    }
    
    @Override
    public void drawContent(int mouseX, int mouseY) {
        inp.drawTextBox();
        for (GuiButton b : buttonList) {
            b.drawButton(mc, mouseX, mouseY, 1);
        }
        inp.scroll.draw(mouseX, mouseY);
    }

    @Override
    public void Update() {
        inp.updateCursorCounter();
        super.Update();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        inp.setFocused(true);
        inp.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void MouseClick(int x, int y) {
        inp.mouseClicked(x, y, 0);
        for (GuiButton b : buttonList) {
            boolean pr = b.mousePressed(mc, x, y);
            switch (b.id) {
                case 0:
                    inp.setText("");
                break;
            }
        }
        super.MouseClick(x, y);
    }
}
