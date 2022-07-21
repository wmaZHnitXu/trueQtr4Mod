package com.anet.qtr4tdm.common.supers;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.hbm.interfaces.ISidedConsumer;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public abstract class TEDefenceEnrg extends TileEntityDefence implements ISidedConsumer, ITickable, IHasEnergy {

    public boolean connected;
    public boolean powered;
    protected long energy, maxEnergy;
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
            addedToEnet = false;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setLong("energy", energy);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        super.readFromNBT(compound);
    }

    @Override
    public void update() {
    
    }

    @Override
    public int getEnergy() {
        return (int)getPower();
    }

    @Override
    public int getMaxEnergy() {
        return (int)getMaxPower();
    }

    @Override
    public long getPower() {
        return energy;
    }

    @Override
    public long getMaxPower() {
        return maxEnergy;
    }

    @Override
    public void setPower(long arg0) {
        energy = arg0;
    }

}
