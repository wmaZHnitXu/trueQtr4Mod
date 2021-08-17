package com.anet.qtr4tdm.uebki.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;

public class InputFieldPlayers extends InputFieldDownScroll {

    public InputFieldPlayers(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
            int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void UpdateScrollSections() {
        String query = this.getText();
        ArrayList<String> sectionlist = new ArrayList<String>();
        for (NetworkPlayerInfo info : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()) {
            boolean contains = true;
            if (info.getDisplayName() != null) {
                for (int i = 0; i < query.length(); i++) {
                    if (info.getDisplayName().getFormattedText().charAt(i) != query.charAt(i)) {
                        contains = false;
                    }
                }
                if (contains) sectionlist.add(info.getDisplayName().getFormattedText());
            }
        }

        sectionlist.add("asdads");
        sectionlist.add("aads");

        String[] sections = new String[sectionlist.size()];
        for (int i = 0; i < sections.length; i++) {
            sections[i] = sectionlist.get(i);
        }

        scroll.sections = sections;
    }
    
}
