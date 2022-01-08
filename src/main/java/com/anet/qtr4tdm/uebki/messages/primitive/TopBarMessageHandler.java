package com.anet.qtr4tdm.uebki.messages.primitive;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TopBarMessageHandler implements IMessageHandler<TopBarMessage, IMessage> {

    @Override
    public IMessage onMessage(TopBarMessage message, MessageContext ctx) {
        return null;
    }
    
}
