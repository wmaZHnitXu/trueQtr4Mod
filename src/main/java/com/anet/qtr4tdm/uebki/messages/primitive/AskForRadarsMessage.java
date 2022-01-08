package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AskForRadarsMessage implements IMessage {

    public AskForRadarsMessage () {
        
    }

    public AskForRadarsMessage (BlockPos pos) {

    }

    public BlockPos pos;
    @Override
    public void fromBytes(ByteBuf arg0) {
        pos = new BlockPos(arg0.readInt(),arg0.readInt(),arg0.readInt());       
    }

    @Override
    public void toBytes(ByteBuf arg0) {
        arg0.writeInt(pos.getX());
        arg0.writeInt(pos.getY());
        arg0.writeInt(pos.getZ());
    }
    
}
