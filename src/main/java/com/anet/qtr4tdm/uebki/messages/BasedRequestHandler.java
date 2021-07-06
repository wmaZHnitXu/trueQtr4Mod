package com.anet.qtr4tdm.uebki.messages;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BasedRequestHandler implements IMessageHandler<BasedRequest, IMessage> {

    @Override
    public IMessage onMessage(BasedRequest message, MessageContext ctx) {
        if (message.reqId == 2) return new RadarMessage(message.pos);
        return new BasedAnswer(message.pos, message.reqId);
    }
    
}
