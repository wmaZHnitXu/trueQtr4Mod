package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;

import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BaseUpgradeHandler implements IMessageHandler<BaseUpgradeMessage, IMessage> {

    @Override
    public IMessage onMessage(BaseUpgradeMessage message, MessageContext ctx) {
        InWorldBasesManager.DoBaseUpgrade(message.Baseid, new ChunkPos(message.ChunkX, message.ChunkZ));
        return new baseInfoMessage(InWorldBasesManager.GetInfo(message.Baseid));
    }
    
}
