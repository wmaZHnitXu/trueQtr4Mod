package com.anet.qtr4tdm.uebki;

import net.minecraft.util.math.BlockPos;

public class RadarObjectStructure {

    public BlockPos pos;
    public String DisplayName;
    public boolean isRadar;
    public boolean isActive;
    public String owner;

    public RadarObjectStructure(BlockPos pos, String displayName, boolean isradar, boolean isActive) {
        this.pos = pos;
        DisplayName = displayName;
        isRadar = isradar;
        
        this.isActive = isActive;
    }
}

