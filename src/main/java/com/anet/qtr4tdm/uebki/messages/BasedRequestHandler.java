package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TerminalRadarTile;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BasedRequestHandler implements IMessageHandler<BasedRequest, IMessage> {

    @Override
    public IMessage onMessage(BasedRequest message, MessageContext ctx) {
        if (message.reqId == 2) return new RadarMessage(((TerminalRadarTile)TdmMod.currentServer.getWorld(0).getTileEntity(message.pos)).myTeam);
        return new BasedAnswer(message.pos, message.reqId);
    }
    
}
