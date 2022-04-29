package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.client.BaseBordersRender;
import com.anet.qtr4tdm.common.bases.baseInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BaseBorderMessage implements IMessage {

    private baseInfo base;

    public baseInfo getBase () {
        return base;
    }

    public BaseBorderMessage () {

    }

    public BaseBorderMessage (baseInfo base) {
        this.base = base;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int id = buf.readInt();
        int dimension = buf.readInt();
        int length = buf.readInt();
        ChunkPos[] chunks = new ChunkPos[length];
        for (int i = 0; i < length; i++) {
            ChunkPos pos = new ChunkPos(buf.readInt(), buf.readInt());
            chunks[i] = pos;
        }
        this.base = new baseInfo(BlockPos.ORIGIN, 0, id, 0, chunks, dimension, "");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(base.id);
        buf.writeInt(base.dimenision);
        buf.writeInt(base.chunks.length);
        for (int i = 0; i < base.chunks.length; i++) {
            ChunkPos pos = base.chunks[i];
            buf.writeInt(pos.x);
            buf.writeInt(pos.z);
        }
    }
    
}
