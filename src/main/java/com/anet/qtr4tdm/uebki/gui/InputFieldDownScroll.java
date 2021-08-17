package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public abstract class InputFieldDownScroll extends GuiTextField implements IScrollest {

    public Scroll scroll;

    public InputFieldDownScroll(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
            int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        scroll = new Scroll(x, y, new String[0], this, Minecraft.getMinecraft());
    }

    public void ScrollClick (int id) {
        setText(scroll.sections[id]);
    }

    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        boolean res = super.textboxKeyTyped(typedChar, keyCode);
        UpdateScrollSections();
        return res; 
    }


    public abstract void UpdateScrollSections ();
}
