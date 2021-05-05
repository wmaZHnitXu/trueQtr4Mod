package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.TdmMod;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskForPlayersHandler implements IMessageHandler<PlayersAskMessage, IMessage> {
    @Override
    public PlayersAnswerMessage onMessage(PlayersAskMessage arg0, MessageContext arg1) {
        TdmMod.logger.info("ClientAskedForPlayers");
        return new PlayersAnswerMessage();
    }
    
}
