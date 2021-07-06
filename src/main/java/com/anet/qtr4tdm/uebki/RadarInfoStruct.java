package com.anet.qtr4tdm.uebki;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RadarInfoStruct {
    public BlockPos pos;
    public int level;
    public int range;
    public boolean isActive;
    public double consumption;

    public RadarInfoStruct (BlockPos pos, int level) {
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
            case 1: return 4;
            case 2: return 8;
            case 3: return 32;
        }
        return 0;
    }

    public boolean insideRange (EntityPlayer player) {
        float distance = (float)player.getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ());
        return distance < range;
    }

    public boolean canBeTraced (EntityPlayer player) {
        Vec3d origin = new Vec3d(pos.add(0,3,0));
        Vec3d target = player.getPositionVector().addVector(0, 100, 0);
        Vec3d step = target.subtract(origin).normalize();
		while (origin.distanceTo(target) > 2) {
			origin = origin.add(step);
			if (player.world.getBlockState(new BlockPos((int)origin.x,(int)origin.y,(int)origin.z)).isNormalCube()) return false;
		}
        return true;
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
