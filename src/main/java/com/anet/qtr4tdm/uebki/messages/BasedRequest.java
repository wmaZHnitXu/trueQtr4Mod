package com.anet.qtr4tdm.uebki.messages;

import com.anet.qtr4tdm.TdmMod;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BasedRequest implements IMessage {

    public BlockPos pos;
    public int reqId;

    public BasedRequest () {

    }

    public BasedRequest (BlockPos pos, int reqId) {
        this.pos = pos;
        this.reqId = reqId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int x, y, z;
        x = buf.getInt(0);
        y = buf.getInt(4);
        z = buf.getInt(8);
        pos = new BlockPos(x,y,z);
        reqId = buf.getInt(12);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(reqId);
    }
    
}
