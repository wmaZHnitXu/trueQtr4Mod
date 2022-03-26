package com.anet.qtr4tdm.uebki.messages.primitive;

import com.anet.qtr4tdm.client.ProjectileHitClient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProjectileHitHandler implements IMessageHandler<ProjectileHitMessage, IMessage> {

    @Override
    public IMessage onMessage(ProjectileHitMessage message, MessageContext ctx) {

        ProjectileHitClient.doProjectileHit(message);

        return null;
    }
    
}
