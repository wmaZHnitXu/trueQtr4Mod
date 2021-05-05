package com.anet.qtr4tdm.uebki;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class RadarInfoStruct {
    public teamState team;
    public BlockPos pos;
    public int level;
    public int range;
    public boolean isActive;
    public double consumption;

    public RadarInfoStruct (teamState team, BlockPos pos, int level) {
        this.team = team;
        this.pos = pos;
        this.level = level;
        this.range = getRange(level);
        this.isActive = false;
        this.consumption = getConsumprion(level);
    }

    public static int getRange (int level) {
        switch (level) {
            case 1: return 100;
            case 2: return 150;
            case 3: return 300;
        }
        return 0;
    }

    public static double getConsumprion (int level) {
        switch (level) {
            case 1: return 32;
            case 2: return 128;
            case 3: return 160;
        }
        return 0;
    }

    public boolean insideRange (EntityPlayer player) {
        float distance = (float)player.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
        return distance < range;
    }

    public double WillConsume (double energy) {
        if (energy >= consumption) {
            isActive = true;
            return consumption;
        }
        isActive = false;
        return 0;
    }
}
