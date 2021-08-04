package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.entity.player.EntityPlayer;

public class IDSmanager {
    public static IDSmanager instance;
    public ArrayList<PlayerIdsInfo> CachedIds;

    public IDSmanager() {
        instance = this;
        CachedIds = new ArrayList<PlayerIdsInfo>();
    }

    public static int GetPlayerId (EntityPlayer entity) {
        return  instance.idByNameWithCaching(entity);
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

    public int idByNameWithCaching (EntityPlayer player) {
        String name = player.getName();
        for (PlayerIdsInfo info : CachedIds) {
            if (name.equals(info.name)) return info.id;
        }
        int idFromBase = SqlHelper.instance.GetPlayerId(name);
        CachedIds.add(new PlayerIdsInfo(name, idFromBase, player));
        return idFromBase;
    }

    public static EntityPlayer GetPlayer (int id) {
        EntityPlayer result = null;
        for (PlayerIdsInfo p : instance.CachedIds) {
            if (p.id == id) {result = p.GetPlayer(); return result;}
        }
        return result;
    }
    
    class PlayerIdsInfo {

        public String name;
        public int id;
        private EntityPlayer player;

        PlayerIdsInfo (String name, int id) {
            this.id = id;
            this.name = name;
        }

        PlayerIdsInfo (String name, int id, EntityPlayer player) {
            this(name, id);
            this.player = player;
        }

        public EntityPlayer GetPlayer() {
            if (player == null) {
                return TdmMod.currentServer.getPlayerList().getPlayerByUsername(name);
            }
            else return player;
        }
    }
}
