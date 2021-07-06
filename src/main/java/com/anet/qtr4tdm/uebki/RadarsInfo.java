package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)

public class RadarsInfo {

    public ArrayList<RadarInfoStruct>[] teamRadars;
    public ArrayList<RadarInfoStruct>[] allInfo;
    public static RadarsInfo instance;

    public ArrayList<RadarInfoStruct> GetTeamRadarList (int t) {
        return teamRadars[t];
    }

    public RadarsInfo () { //Инициализировать через создание нового экземпляра при старте игры.
        instance = this;
    }

    public static boolean IsRadarActive (BlockPos pos) {
        boolean result = false;
        for (int i = 0; i < instance.teamRadars.length; i++) {
            for (RadarInfoStruct radar : instance.teamRadars[i]) {
                if (pos.getDistance(radar.pos.getX(), radar.pos.getY(), radar.pos.getZ()) < 2f) {
                    result = radar.isActive;
                }
            }
        }
        return result;
    }


    public static void AddRadar (RadarInfoStruct radarInfoStruct) {

    }

    public static void RemoveRadar (BlockPos pos) {
        for (ArrayList<RadarInfoStruct> list : instance.teamRadars) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).pos.equals(pos)) {
                    list.remove(i);
                }
            }
        }
    }


    public ArrayList<RadarObjectStructure> GetInfoForPos (BlockPos pos) {
        return new ArrayList<RadarObjectStructure>();
    }

    @SubscribeEvent
    public static void OnWorldTick (WorldTickEvent event) {
        if (instance == null) return;
        /*
        if (instance.counter == 20) {
            for (player p : Teams.instance.GetPlayers()) {
                if (p.playerEntity instanceof EntityPlayerMP) {
                    try {
                        TdmMod.wrapper.sendTo(new RadarMessage(p.team), (EntityPlayerMP)p.playerEntity);
                    }
                    catch (Exception e) {
                        TdmMod.logger.info("Network error");
                        TdmMod.logger.info(e.toString());
                    }
                }
            }
            instance.counter = 0;
        }
        */
    }
}

