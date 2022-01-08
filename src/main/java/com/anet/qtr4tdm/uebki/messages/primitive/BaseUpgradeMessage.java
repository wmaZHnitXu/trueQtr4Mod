package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.common.bases.baseInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BaseUpgradeMessage implements IMessage {

    int Baseid;
    int ChunkX;
    int ChunkZ;

    public BaseUpgradeMessage (int Baseid, ChunkPos pos) {
        this.Baseid = Baseid;
        ChunkX = pos.x;
        ChunkZ = pos.z;
    }

    public BaseUpgradeMessage () {}

    @Override
    public void fromBytes(ByteBuf buf) {
        Baseid = buf.readInt();
        ChunkX = buf.readInt();
        ChunkZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(Baseid);
        buf.writeInt(ChunkX);
        buf.writeInt(ChunkZ);
    }

}
