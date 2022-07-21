package com.anet.qtr4tdm.common.supers;

import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class DroneSmallEntity extends EntityLiving {

    protected enum DroneMoveStatus {

        Standing,
        Ascent,
        Towards,
        Descent;

        public DroneMoveStatus GetNextState () {

            DroneMoveStatus[] values = DroneMoveStatus.values();
            return this.ordinal()+1 >= values.length ? values[0] : DroneMoveStatus.values()[this.ordinal()+1];
        }
    }

    protected long energy;
    protected baseInfo baseinf;
    protected BlockPos basePos;
    protected DroneMoveStatus movingStatus;
    protected int recomendedHeight = 0;
    protected DroneBaseTile baseTile;

    @Override
    protected void initEntityAI() {

    }

    public DroneSmallEntity(World worldIn) {
        super(worldIn);
        movingStatus = DroneMoveStatus.Standing;
    }

    public long injectEnergy (double amount) {
        energy += amount;
        energy = Math.min(energy, getMaxEnergy());
        return Math.max(energy - getMaxEnergy(), 0);
    }

    public double getCurrentEnergy () {
        return energy;
    }

    public long getEnergy () {
        return energy;
    }

    public abstract long getMaxEnergy ();

    public abstract Item getDroneItem ();
    
}
