package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TurretRotationMessage implements IMessage {

    public int entityId;
    public double yaw;
    public double pitch;
    
    public TurretRotationMessage () {}

    public TurretRotationMessage (int entityId, double yaw, double pitch) {
        this.entityId = entityId;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        yaw = buf.readDouble();
        pitch = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(yaw);
        buf.writeDouble(pitch);        
    }
    
}
