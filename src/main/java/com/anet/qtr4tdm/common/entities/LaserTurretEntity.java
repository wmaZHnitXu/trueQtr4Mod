package com.anet.qtr4tdm.common.entities;

import net.minecraft.world.World;

public class LaserTurretEntity extends TurretEntity {

    public LaserTurretEntity(World worldIn) {
        super(worldIn);
        
    }

    @Override
    protected int getDetectionRange() {
        return 50;
    }

    @Override
    protected double getGunHeight() {
        // TODO Auto-generated method stub
        return 1;
    }
    
}
