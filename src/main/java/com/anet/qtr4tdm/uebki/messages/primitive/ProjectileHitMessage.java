package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProjectileHitMessage implements IMessage {

    public Vec3d dir;
    public Vec3d hitPos;
    public BlockPos blockPos;
    public int type;

    public ProjectileHitMessage () {

    }

    public ProjectileHitMessage (Vec3d dir, Vec3d hitPos, BlockPos blockPos, int type) {
        this.dir = dir;
        this.hitPos = hitPos;
        this.blockPos = blockPos;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        
        dir = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

        hitPos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        type = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeDouble(dir.x);
        buf.writeDouble(dir.y);
        buf.writeDouble(dir.z);

        buf.writeDouble(hitPos.x);
        buf.writeDouble(hitPos.y);
        buf.writeDouble(hitPos.z);

        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());

        buf.writeInt(type);

    }
    
}
