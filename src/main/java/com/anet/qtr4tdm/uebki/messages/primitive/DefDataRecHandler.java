package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.TdmMod;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DefDataRecHandler implements IMessageHandler<DefenceDataToPlayer, IMessage> {

    @Override
    public IMessage onMessage(DefenceDataToPlayer message, MessageContext ctx) {
        TdmMod.logger.info("recccccccc");
        return null;
    }
    
}
