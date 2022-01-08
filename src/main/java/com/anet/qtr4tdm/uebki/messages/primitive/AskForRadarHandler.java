package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.TdmMod;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AskForRadarHandler implements IMessageHandler<AskForRadarsMessage, IMessage> {
    @Override
    public RadarMessage onMessage(AskForRadarsMessage arg0, MessageContext arg1) {
        TdmMod.logger.info("ClientAskedForRadars");
        return new RadarMessage(arg0.pos);
    }
    
}
