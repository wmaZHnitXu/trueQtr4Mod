package com.anet.qtr4tdm.uebki.messages.primitive;


import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DefDataHandler implements IMessageHandler<AskDefenceDataToPlayer, IMessage> {

    @Override
    public DefenceDataToPlayer onMessage(AskDefenceDataToPlayer message, MessageContext ctx) {
        return new DefenceDataToPlayer(message.basePos, message.playerEntityId);
    }
    
}
