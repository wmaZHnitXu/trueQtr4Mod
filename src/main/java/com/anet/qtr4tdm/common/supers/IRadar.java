package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface IRadar extends IBaseConnectable {

    public ArrayList<RadarTrackingInfo> ReportTargetsToBase (baseInfo info);

    public boolean isInRange (EntityPlayer e);

    public BlockPos getPosForBase ();
}
