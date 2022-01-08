package com.anet.qtr4tdm.uebki.messages.primitive;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.uebki.IDSmanager;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GetBaseMembersMessage implements IMessage {

    public ArrayList<MemberData> members;
    public boolean permission;

    public GetBaseMembersMessage () {

    }

    public GetBaseMembersMessage (baseInfo base, EntityPlayer player) {
        int[] members = base.members;
        this.members = new ArrayList<MemberData>();
        for (int i = 0; i < members.length; i++) {
            int level = 0;
            if (base.OwnerId == members[i]) level = 3;
            this.members.add(new MemberData(IDSmanager.GetName(members[i]), level));
        }
        permission = base.OwnerId == IDSmanager.GetPlayerId(player);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        members = new ArrayList<MemberData>();
        String resString = ByteBufUtils.readUTF8String(buf);
        String[] splited = resString.split(" ");
        if (splited.length > 2) {
            for (int i = 0; i < splited.length-1; i+=2) {
                members.add(new MemberData(splited[i+1], Integer.parseInt(splited[i])));
            }
        }
        permission = splited[splited.length-1].equals("y");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String resString = "";
        for (int i = 0; i < members.size(); i++) {
            MemberData d = members.get(i);
            resString += (i == 0 ? "" : " ") + d.level + " " + d.name;
        }
        resString += " " + (permission ? "y" : "n");
        ByteBufUtils.writeUTF8String(buf, resString);
    }
}
