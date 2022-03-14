package com.anet.qtr4tdm.common.entities;

import net.minecraft.world.World;

public class LaserTurretEntity extends TurretEntity {

    public float charge = 0;

    public LaserTurretEntity(World worldIn) {
        super(worldIn);
        
    }

    @Override
    protected int getDetectionRange() {
        return 50;
    }

    @Override
    protected double getGunHeight() {
        return 1;
    }

    @Override
    public double getMaxEnergy() {
        return 100000;
    }

    @Override
    protected double getTickConsumption() {
        return 8;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (target != null) {
            charge += (1 - charge) * 0.05f;
        }
        else {
            charge += (0 - charge) * 0.01f;
        }
    }
    
}
