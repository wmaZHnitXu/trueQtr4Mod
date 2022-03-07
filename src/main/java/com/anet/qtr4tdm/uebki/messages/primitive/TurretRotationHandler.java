package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.TurretEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TurretRotationHandler implements IMessageHandler<TurretRotationMessage, IMessage> {

    @Override
    public IMessage onMessage(TurretRotationMessage message, MessageContext ctx) {
        Entity e = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
        if (e instanceof TurretEntity) {
            ((TurretEntity)e).insertTurretRotations(message.pitch, message.yaw);
        }
        return null;
    }
    
}
