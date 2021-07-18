package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;

public class IDSmanager {
    public static IDSmanager instance;
    public ArrayList<PlayerIdsInfo> CachedIds;

    public IDSmanager() {
        instance = this;
        CachedIds = new ArrayList<PlayerIdsInfo>();
    }

    public static int GetPlayerId (EntityPlayer entity) {
        return  instance.idByNameWithCaching(entity.getName());
    }

    public static int GetPlayerId (String name) {
        return instance.idByNameWithCaching(name);
    }

    public int idByNameWithCaching (String name) {
        for (PlayerIdsInfo info : CachedIds) {
            if (name.equals(info.name)) return info.id;
        }
        int idFromBase = SqlHelper.instance.GetPlayerId(name);
        CachedIds.add(new PlayerIdsInfo(name, idFromBase));
        return idFromBase;
    }
    
    class PlayerIdsInfo {

        String name;
        int id;

        PlayerIdsInfo (String name, int id) {
            this.id = id;
            this.name = name;
        }
    }
}
