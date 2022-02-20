package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.client.BaseBordersRender;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BaseBorderHandler implements IMessageHandler<BaseBorderMessage, IMessage> {

    @Override
    public IMessage onMessage(BaseBorderMessage message, MessageContext ctx) {
        BaseBordersRender.InsertBorderData(message.getBase());
        return null;
    }
    
}
