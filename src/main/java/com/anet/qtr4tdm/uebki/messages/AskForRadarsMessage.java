package com.anet.qtr4tdm.uebki.messages;
import com.anet.qtr4tdm.uebki.teamState;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AskForRadarsMessage implements IMessage {

    public AskForRadarsMessage () {
        
    }

    public AskForRadarsMessage (teamState t) {
        team = t.ordinal();
    }

    public int team;
    @Override
    public void fromBytes(ByteBuf arg0) {
        team = arg0.readInt();        
    }

    @Override
    public void toBytes(ByteBuf arg0) {
        arg0.writeInt(team);
    }
    
}
