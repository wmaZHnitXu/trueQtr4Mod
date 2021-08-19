package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;

public class InputFieldPlayers extends InputFieldDownScroll {

    public InputFieldPlayers(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
            int par6Height, ITextFieldFocus master) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height, master);
    }

    @Override
    public void UpdateScrollSections() {
        String query = this.getText();
        query = query.toLowerCase();
        ArrayList<String> sectionlist = new ArrayList<String>();
        for (NetworkPlayerInfo info : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
            String playerName = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info);
            boolean contains = true;
                for (int i = 0; i < query.length(); i++) {
                    
                    System.out.println(playerName.toLowerCase() + "  " + query);
                    if (query.length() < playerName.length() && playerName.toLowerCase().charAt(i) != query.charAt(i)) {
                        contains = false;
                    }
                }
                if (contains) sectionlist.add(playerName);
        }

        String[] sections = new String[sectionlist.size()];
        for (int i = 0; i < sections.length; i++) {
            sections[i] = sectionlist.get(i);
        }

        scroll.sections = sections;
    }
    

    @Override
    public void setFocused(boolean isFocusedIn) {
        scroll.enabled = isFocusedIn;
        super.setFocused(isFocusedIn);
    }
}
