package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetBaseMember implements IMessage {

    String name;
    int level;
    int x,y,z;

    public SetBaseMember () {

    }

    public SetBaseMember (String name, int level, BlockPos pos) {
        this.name = name;
        this.level = level;
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String result = ByteBufUtils.readUTF8String(buf);
        String[] arr = result.split(" ");
        x = Integer.parseInt(arr[0]);
        y = Integer.parseInt(arr[1]);
        z = Integer.parseInt(arr[2]);
        level = Integer.parseInt(arr[3]);
        name = arr[4];

    }

    @Override
    public void toBytes(ByteBuf buf) {
        String bytestring = x + " " + y + " " + z + " " + level + " " + name;
        ByteBufUtils.writeUTF8String(buf, bytestring);
    }
}
