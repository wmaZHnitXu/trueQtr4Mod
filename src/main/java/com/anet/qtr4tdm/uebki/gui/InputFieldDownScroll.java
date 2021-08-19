package com.anet.qtr4tdm.uebki.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public abstract class InputFieldDownScroll extends GuiTextField implements IScrollest {

    public Scroll scroll;
    public ITextFieldFocus master;

    public InputFieldDownScroll(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
            int par6Height, ITextFieldFocus master) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        this.master = master;
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

    @Override
    public void setFocused(boolean isFocusedIn) {
        master.SetFocused(isFocusedIn);
        super.setFocused(isFocusedIn);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        scroll.Click(mouseX, mouseY);
        if (scroll.highlightIndex == -1)
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        else return true;
    }

    public abstract void UpdateScrollSections ();
}
