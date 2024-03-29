package com.anet.qtr4tdm.common.tiles;


import ic2.api.energy.prefab.BasicEnergyTe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public class EnergyConsumerTile extends BasicEnergyTe.Sink implements ITickable {

    public EnergyConsumerTile() {
        super(10000, 2);
        state = ConsumerState.inactive;
    }
    
    private ConsumerState state;

    public ConsumerState GetState () {
        return state;
    }

    public enum ConsumerState {
        inactive,
        charging,
        full
    }

    public static int fromServerEnergy;
    public static int fromServerTeamEnergy;
    public static int fromServerMaxTeamEnergy;
    public static int fromServerConsumption;
    public static int fromServerGeneration;

    public static void InjectDataFromServer (int e, int te, int mte, int tm, int c, int g) {
        fromServerEnergy = e;
        fromServerTeamEnergy = te;
        fromServerMaxTeamEnergy = mte;
        fromServerConsumption = c;
        fromServerGeneration = g;
    }

    @Override
    public void onLoad () {
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
    }

    @Override
    public void update() {
        
    }

    public void FireIC2DisconnectEvent () {
    }
}
