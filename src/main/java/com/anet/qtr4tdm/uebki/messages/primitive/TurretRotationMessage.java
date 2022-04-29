package com.anet.qtr4tdm.uebki.messages.primitive;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TurretRotationMessage implements IMessage {

    public int entityId;
    public double yaw;
    public double pitch;
    public int targetId;
    public float additional;
    
    public TurretRotationMessage () {}

    public TurretRotationMessage (int entityId, double yaw, double pitch, int targetId) {
        this.entityId = entityId;
        this.yaw = yaw;
        this.pitch = pitch;
        this.targetId = targetId;
    }

    public TurretRotationMessage (int entityId, double yaw, double pitch, int targetId, float additional) {
        this.entityId = entityId;
        this.yaw = yaw;
        this.pitch = pitch;
        this.targetId = targetId;
        this.additional = additional;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        yaw = buf.readDouble();
        pitch = buf.readDouble();
        targetId = buf.readInt();
        additional = buf.readFloat();
        
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(yaw);
        buf.writeDouble(pitch);
        buf.writeInt(targetId);  
        buf.writeFloat(additional);
    }
    
}
