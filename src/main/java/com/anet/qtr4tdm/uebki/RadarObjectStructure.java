package com.anet.qtr4tdm.uebki;

import net.minecraft.util.math.BlockPos;

public class RadarObjectStructure {

    public BlockPos pos;
    public String DisplayName;
    public boolean isRadar;
    public boolean isActive;
    public teamState team;

    public RadarObjectStructure(BlockPos pos, String displayName, boolean isradar, boolean isActive) {
        this.pos = pos;
        DisplayName = displayName;
        isRadar = isradar;
        team = teamState.specs;
        if (displayName.charAt(0) == '§') {
            for (teamState t : teamState.values()) {
                if (Teams.GetTeamColorSymbols(t).equals(displayName.substring(0, 2))) {
                    team = t;
                }
            }
        }
        this.isActive = isActive;
    }
}

