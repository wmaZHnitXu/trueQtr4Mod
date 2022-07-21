package com.anet.qtr4tdm.common.supers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class EnergySink extends TileEntity implements cofh.redstoneflux.api.IEnergyReceiver {

    private int maxEnergy;
    private int storedEnergy;

    public EnergySink (int a, int b) {
        this.maxEnergy = a;
    }

    @Override
    public int getEnergyStored(EnumFacing arg0) {
        return storedEnergy;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing arg0) {
        return maxEnergy;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing arg0) {
        return true; //TODO
    }

    @Override
    public int receiveEnergy(EnumFacing arg0, int arg1, boolean arg2) {
        return 0;
    }
    
}
