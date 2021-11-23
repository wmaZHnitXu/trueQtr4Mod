package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.common.supers.IHasEnergy;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.prefab.BasicEnergyTe.Sink;
import net.minecraftforge.common.MinecraftForge;

public class ThermalBaseTile extends Sink implements IHasEnergy {

    public boolean hasEnergy;
    public ThermalBaseTile() {
        super(10, 1);
    }
    
    @Override
    public void onLoad () {
        if (!world.isRemote) {
            markDirty();
        }
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getEnergyBuffer()));
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.getEnergyBuffer()));
    }

    public void OnDestroy () {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.getEnergyBuffer()));
    }

    @Override
    public void SetHasEnergy(boolean b) {
        hasEnergy = b;
    }
}
