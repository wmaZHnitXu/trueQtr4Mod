package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public abstract class TEDefenceEnrg extends TileEntityDefence implements IEnergySink, ITickable, IHasEnergy {

    public boolean connected;
    public boolean powered;
    protected double energy, maxEnergy;
    protected boolean addedToEnet;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            DisconnectFromBase();
            InWorldBasesManager.GetBaseConnection(this);
        }
    }

    public void Destruction () {
        if (!world.isRemote) {
            DisconnectFromBase();
        }
    }

    @Override
    public void invalidate() {
        onChunkUnload();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (!world.isRemote && addedToEnet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnet = false;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble("energy", energy);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getDouble("energy");
        super.readFromNBT(compound);
    }

    @Override
    public double getDemandedEnergy() {
        return maxEnergy - energy;
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        energy = Math.min(energy + amount, maxEnergy);
        markDirty();
        //return Math.max(energy - maxEnergy, 0); V DOKAX SKAZALI, 4TO TAK LY$WE DLYA PROIZVODITELNOSTI
        return 0;
    }

    @Override
    public void update() {
        if (!world.isRemote && !addedToEnet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToEnet = true;
        }
    }

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public double getMaxEnergy() {
        return maxEnergy;
    }

}
