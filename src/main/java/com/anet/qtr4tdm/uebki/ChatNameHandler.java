package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.swing.TextComponent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class ChatNameHandler {

    @SubscribeEvent
    public static void getDisplayName(PlayerEvent.NameFormat nameEvt) {
        if (nameEvt.getUsername() != null) {
            nameEvt.setDisplayname(Teams.GetTeamColorSymbols(Teams.GetTeamOfPlayer(nameEvt.getEntityPlayer()))+ nameEvt.getUsername());
        }
    }
    @SubscribeEvent
    public static void formatChatMessage (ClientChatReceivedEvent event) {
        String result;
        result = event.getMessage().getFormattedText();
        int divider = result.indexOf(">");
        if (divider == -1 && result.indexOf("<") == -1) return;
        String nickpart = result.substring(0, divider);
        String messagepart = result.substring(divider+2);
        nickpart = nickpart.replaceFirst("<", "");
        messagepart = messagepart.replaceFirst(">", "§2>");
        result = nickpart + " : " + messagepart;
        event.setMessage(new TextComponentString(result));
    }
}
