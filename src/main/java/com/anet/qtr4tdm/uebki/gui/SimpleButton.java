package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.gui.GuiButton;

public class SimpleButton extends GuiButton {

    public SimpleButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.width = 15;
        this.height = 15;
    }
    
}
