package com.anet.qtr4tdm.uebki.messages;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.RadarInfoStruct;
import com.anet.qtr4tdm.uebki.RadarObjectStructure;
import com.anet.qtr4tdm.uebki.teamState;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RadarInfoHandler implements IMessageHandler<RadarMessage, IMessage>{

    public static ArrayList<RadarObjectStructure> objects;

    @Override
    public IMessage onMessage(RadarMessage message, MessageContext ctx) {
        return null;
    }
    
}
