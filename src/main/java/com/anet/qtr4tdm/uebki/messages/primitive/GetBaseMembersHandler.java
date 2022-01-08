package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.uebki.gui.GuiWidgetMembers;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetBaseMembersHandler implements IMessageHandler<GetBaseMembersMessage, IMessage> {

    @Override
    public IMessage onMessage(GetBaseMembersMessage message, MessageContext ctx) {
        GuiWidgetMembers.InserMembers(message);
        return null;
    }
    
}
