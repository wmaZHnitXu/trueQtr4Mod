package com.anet.qtr4tdm.common.supers;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class RadarTrackingInfo {
    public Entity ent;
    public int type;
    public BlockPos lastKnownPosition;

    public RadarTrackingInfo (Entity ent, int type) {
        this.ent = ent;
        this.type = type;
        lastKnownPosition = ent.getPosition();
    }
}
