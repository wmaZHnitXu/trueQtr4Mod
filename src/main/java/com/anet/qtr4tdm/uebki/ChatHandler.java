package com.anet.qtr4tdm.uebki;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)
public class ChatHandler {

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
