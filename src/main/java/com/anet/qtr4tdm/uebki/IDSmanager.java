package com.anet.qtr4tdm.uebki;

import java.sql.SQLException;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.entity.player.EntityPlayer;

public class IDSmanager {
    public static IDSmanager instance;

    public IDSmanager() {
        instance = this;
    }

    public int GetPlayerId (EntityPlayer entity) {
        return  SqlHelper.instance.GetPlayerId(entity.getName());
    }
    
}
