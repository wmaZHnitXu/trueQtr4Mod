package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetBaseMemberHandler implements IMessageHandler<SetBaseMember, IMessage> {

    @Override
    public IMessage onMessage(SetBaseMember message, MessageContext ctx) {
        InWorldBasesManager.SetMember(new BlockPos(message.x, message.y, message.z), message.name, message.level);
        return new GetBaseMembersMessage(InWorldBasesManager.GetInfo(new BlockPos(message.x, message.y, message.z)), ctx.getServerHandler().player);
    }

}
