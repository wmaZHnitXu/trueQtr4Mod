package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.common.supers.Radar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class RadarThermal1Entity extends Radar {

    public boolean active;
    public float rot;

    public RadarThermal1Entity(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean isInRange(EntityPlayer e) {
        return (e.getPosition().distanceSq(getPosition()) < 100);
    }

    @Override
    public void onUpdate() {
        rot += 0.1f;
        super.onUpdate();
    }
}
