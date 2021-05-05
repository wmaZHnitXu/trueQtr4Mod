package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class ChatNameHandler {
    @SubscribeEvent
    public static void getDisplayName(PlayerEvent.NameFormat nameEvt) {
        if (nameEvt.getUsername() != null) {
            nameEvt.setDisplayname(Teams.GetTeamColorSymbols(Teams.GetTeamOfPlayer(nameEvt.getEntityPlayer()))+ nameEvt.getUsername());
        }
    }
}
