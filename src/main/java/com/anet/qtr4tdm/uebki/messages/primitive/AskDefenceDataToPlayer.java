package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.uebki.messages.basic.IBasicMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class AskDefenceDataToPlayer implements IBasicMessage  {

    public BlockPos basePos;
    public int playerEntityId;

    public AskDefenceDataToPlayer () {

    }

    public AskDefenceDataToPlayer (BlockPos pos, int id) {
        playerEntityId = id;
        basePos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        basePos = new BlockPos(buf.readInt(),buf.readInt(),buf.readInt());
        playerEntityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(basePos.getX());
        buf.writeInt(basePos.getY());
        buf.writeInt(basePos.getZ());
        buf.writeInt(playerEntityId);
    }

    @Override
    public DefenceDataToPlayer getResponse() {
        return null;
    }
    
}
