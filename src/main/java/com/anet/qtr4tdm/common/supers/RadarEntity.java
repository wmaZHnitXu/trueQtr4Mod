package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import com.anet.qtr4tdm.common.supers.RadarTrackingInfo;

public abstract class RadarEntity extends EntityBaseConnectable implements IRadar {

    public ArrayList<RadarTrackingInfo> TrackingEntities;
    public baseInfo base;

    public RadarEntity(World worldIn) {
        super(worldIn);
    }

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

    @Override
    public void Refresh() {
        TrackingEntities = new ArrayList<RadarTrackingInfo>();
    }

    @Override
    public void DisconnectFromBase() {
        if (base != null) {
            base.DisconnectDefenceSystem(this);
        }
        base = null;
    }
}
