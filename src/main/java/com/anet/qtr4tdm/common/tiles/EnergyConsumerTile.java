package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.blocks.EnergyConsumerBlock;
import com.anet.qtr4tdm.uebki.EnergyTeams;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.player;
import com.anet.qtr4tdm.uebki.teamState;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.prefab.BasicEnergyTe;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;

public class EnergyConsumerTile extends BasicEnergyTe.Sink implements ITickable {

    public EnergyConsumerTile() {
        super(10000, 2);
        state = ConsumerState.inactive;
    }
    
    private teamState team;
    private ConsumerState state;

    public teamState GetTeam () {
        return team;
    }

    public ConsumerState GetState () {
        return state;
    }

    public enum ConsumerState {
        inactive,
        charging,
        full
    }

    private int disableCounter;

    public static int fromServerEnergy;
    public static int fromServerTeamEnergy;
    public static int fromServerMaxTeamEnergy;
    public static teamState teamS;

    public static void InjectDataFromServer (int e, int te, int mte, int tm) {
        fromServerEnergy = e;
        fromServerTeamEnergy = te;
        fromServerMaxTeamEnergy = mte;
        teamS = teamState.values()[tm];
    }

    @Override
    public void onLoad () {
        if (!world.isRemote) {
            player p = Teams.GetClosestPlayer(pos);
            if (p != null) {
                team = p.team;
            }
            markDirty();
        }
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getEnergyBuffer()));
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.getEnergyBuffer()));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        if (!world.isRemote) {
            tagCompound = super.writeToNBT(tagCompound);
            tagCompound.setInteger("team", team.ordinal());
        }
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.team = teamState.values()[tagCompound.getInteger("team")];
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            int oldState = state.ordinal();
            BasicSink buffer = getEnergyBuffer();
            if (EnergyTeams.NeedEnergy(team)) {
                if (buffer.getEnergyStored() > 0) {
                    state = ConsumerState.charging;
                    disableCounter = 0;
                }
                else  {
                    if (disableCounter >= 20) {
                        state = ConsumerState.inactive;
                    }
                    else {
                        disableCounter++;
                    }
                }

                double needed = EnergyTeams.GetNeededEnergy(team);
                if (needed <= buffer.getEnergyStored()) {
                    EnergyTeams.AddTeamEnergy(team, needed);
                    buffer.useEnergy(needed);
                }
                else {
                    double inStock = buffer.getEnergyStored();
                    buffer.useEnergy(inStock);
                    EnergyTeams.AddTeamEnergy(team, inStock);
                }
            }
            if (buffer.getEnergyStored() > 0) state = ConsumerState.full;
            if (oldState != state.ordinal()) {
                EnergyConsumerBlock.SetState(world, pos, state.ordinal());
            }
        }
    }

    public void FireIC2DisconnectEvent () {
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.getEnergyBuffer()));
    }
}
