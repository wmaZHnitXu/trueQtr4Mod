package com.anet.qtr4tdm.uebki.messages.basic;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IBasicMessage extends IMessage {
    public IBasicMessage getResponse();
}
