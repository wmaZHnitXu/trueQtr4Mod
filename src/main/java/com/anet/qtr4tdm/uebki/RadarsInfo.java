package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.RadarMessage;

import ibxm.Player;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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
        teamRadars = new ArrayList[teamState.values().length];
        for (int i = 0; i < teamState.values().length; i++) {
            teamRadars[i] = new ArrayList<RadarInfoStruct>();
        }
    }


    public static void AddRadar (RadarInfoStruct radarInfoStruct) {
        instance.teamRadars[radarInfoStruct.team.ordinal()].add(radarInfoStruct);
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

    private int counter;

    @SubscribeEvent
    public static void OnWorldTick (WorldTickEvent event) {
        if (instance == null) return;

        if (instance.counter == 20) {
            for (player p : Teams.instance.GetPlayers()) {
                if (p.playerEntity instanceof EntityPlayerMP) {
                TdmMod.wrapper.sendTo(new RadarMessage(p.team), (EntityPlayerMP)p.playerEntity);
                }
            }
            instance.counter = 0;
        }
        else instance.counter++;
    }
    
    public static ArrayList<RadarObjectStructure> GetinfoForTeam (teamState team) {
        ArrayList<RadarObjectStructure> result = new ArrayList<RadarObjectStructure>();
        ArrayList<RadarInfoStruct> teamradars = team != teamState.specs ? instance.teamRadars[team.ordinal()] : new ArrayList<RadarInfoStruct>();
        for (player p : Teams.instance.GetPlayers()) {
            for (int i = 0; i < teamradars.size(); i++) {
                if (teamradars.get(i).insideRange(p.playerEntity) && teamradars.get(i).isActive) {
                    result.add(new RadarObjectStructure(p.playerEntity.getPosition(), p.playerEntity.getDisplayNameString(), false, true));
                    i = teamradars.size();
                }
            }
        }
        for (RadarInfoStruct radar : teamradars) {
            String radarName = Teams.GetTeamColorSymbols(radar.team) + "Радар lvl:" + radar.level;
            if (!radar.isActive) radarName = "§fРадар обесточен.";
            result.add(new RadarObjectStructure(radar.pos, radarName, true, radar.isActive));
        }
        return result;
    }
}

