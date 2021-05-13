package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber(modid = TdmMod.MODID)

public class EnergyTeams {
    
    double[] EnergyStored;
    double[] Generation;
    double[] EnergyMax;

    public static EnergyTeams instance;

    public EnergyTeams () {
        this.EnergyStored = new double[teamState.values().length];
        this.EnergyMax = new double[teamState.values().length];
        this.Generation = new double[teamState.values().length];
        for (int i = 0; i < EnergyMax.length; i++) {
            EnergyMax[i] = 50000;
        }

        instance = this;
    }

    public static boolean NeedEnergy (teamState team) {
        int ord = team.ordinal();
        return instance.EnergyStored[ord] < instance.EnergyMax[ord];
    }

    public static void AddTeamEnergy (teamState team, double amount) {
        int ord = team.ordinal();
        instance.EnergyStored[ord] = instance.EnergyStored[ord] + amount <= instance.EnergyMax[ord] ? instance.EnergyStored[ord] + amount : instance.EnergyMax[ord];
    }

    public static double GetNeededEnergy (teamState team) {
        int ord = team.ordinal();
        return instance.EnergyMax[ord] - instance.EnergyStored[ord];
    }

    public static boolean ConsumeTeamEnergy (teamState team, double amount) {
        int ord = team.ordinal();
        if (instance.EnergyStored[ord] >= amount) {
            instance.EnergyStored[ord] -= amount;
            return true;
        }
        return false;
    }

    public static double GetEnergyOfTeam (teamState team) {
        int ord = team.ordinal();
        return instance.EnergyStored[ord];
    }

    public static double GetMaxEnergyOfTeam (teamState team) {
        int ord = team.ordinal();
        return instance.EnergyMax[ord];
    }

    private boolean tickCounter = false;

    @SubscribeEvent
    public static void OnWorldTick (WorldTickEvent event) {
        if (instance == null || instance.tickCounter) {instance.tickCounter = false; return;}
        instance.ConsumeAll();
        instance.tickCounter = true;
    }

    public void ConsumeAll () {
        for (int t = 0; t < teamState.values().length; t++) {
            ArrayList<RadarInfoStruct> radars = RadarsInfo.instance.GetTeamRadarList(t);
            for (RadarInfoStruct radar : radars) {
                EnergyStored[t] -= radar.WillConsume(EnergyStored[t]);
            }
        }
    }

    public static double GetTeamConsumption (teamState team) {
        int ord = team.ordinal();
        double result = 0;
        ArrayList<RadarInfoStruct> radars = RadarsInfo.instance.GetTeamRadarList(ord);
        for (RadarInfoStruct r : radars) {
            result += r.consumption;
        }
        return result;
    }

    public static double GetTeamGeneration (teamState team) {
        if (instance == null) return 0;
        double result = 0;
        result = instance.Generation[team.ordinal()];
        return result;
    }

    public static void AddGeneration (teamState team, double amount) {
        instance.Generation[team.ordinal()] += amount;
    }
}
