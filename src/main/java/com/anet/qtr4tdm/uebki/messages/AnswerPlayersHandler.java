package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AnswerPlayersHandler implements IMessageHandler<PlayersAnswerMessage, IMessage> {

    @Override
    public IMessage onMessage(PlayersAnswerMessage message, MessageContext context) {
        IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TdmMod.logger.info("received");
                }
            });
            return null; // no response in this case
    }
    
}
