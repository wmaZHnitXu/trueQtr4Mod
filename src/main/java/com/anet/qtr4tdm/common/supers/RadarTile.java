package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.entity.player.EntityPlayer;

public abstract class RadarTile extends TEDefenceEnrg implements IRadar {

    @Override
    public ArrayList<RadarTrackingInfo> ReportTargetsToBase (baseInfo info) {
        base = info;
        ArrayList<RadarTrackingInfo> result = new ArrayList<RadarTrackingInfo>();
        for (EntityPlayer player : world.playerEntities) {
            if (player != null && isInRange(player)) {
                result.add(new RadarTrackingInfo(player, InWorldBasesManager.GetPlayerRelationsWithBase(player, base)));
            }
        }
        return result;
    }
    
}
