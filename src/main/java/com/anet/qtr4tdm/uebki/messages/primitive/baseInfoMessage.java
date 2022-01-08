package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.bases.baseStatus;
import com.anet.qtr4tdm.uebki.gui.BaseGui;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class baseInfoMessage implements IMessage {

    baseInfo info;

    public baseInfoMessage () {

    }

    public baseInfoMessage (baseInfo info) {
        this.info = info;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        BlockPos pos = new BlockPos(x,y,z);
        int owner = buf.readInt();
        int id = buf.readInt();
        int level = buf.readInt();
        int dim = buf.readInt();

        int chl = buf.readInt();
        ChunkPos[] chunks = new ChunkPos[chl];
        for (int i = 0; i < chl; i++) {
            int cx = buf.readInt();
            int cz = buf.readInt();
            chunks[i] = new ChunkPos(cx, cz);
        }

        int mel = buf.readInt();
        int[] members = new int[mel];
        for (int i = 0; i < mel; i++) {
            members[i] = buf.readInt();
        }

        int defcount = buf.readInt();
        int status = buf.readInt();

        this.info = new baseInfo(pos, owner, id, level, chunks, dim, "", members);
        info.defcount = defcount;
        info.status = baseStatus.values()[status];
        BaseGui.InsertInfo(info);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(info.pos.getX());
        buf.writeInt(info.pos.getY());
        buf.writeInt(info.pos.getZ());
        buf.writeInt(info.OwnerId);
        buf.writeInt(info.id);
        buf.writeInt(info.level);
        buf.writeInt(info.dimenision);
        buf.writeInt(info.chunks.length);   
        for (ChunkPos chunkPos : info.chunks) {
            buf.writeInt(chunkPos.x);
            buf.writeInt(chunkPos.z);
        }
        buf.writeInt(info.members.length);
        for (int i : info.members) {
            buf.writeInt(i);
        }
        buf.writeInt(info.defenders.size());
        buf.writeInt(info.status.ordinal());
    }
}
