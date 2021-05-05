package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.common.blocks.MiniSiloBlock;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.player;
import com.anet.qtr4tdm.uebki.teamState;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MiniSiloTile extends TileEntity {

    public boolean armed;
    public teamState team;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("armed", armed);
        compound.setInteger("team", team.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        armed = compound.getBoolean("armed");
        team = teamState.values()[compound.getInteger("team")];
        super.readFromNBT(compound);
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
    }

    public void Arm () {
        armed = true;
        MiniSiloBlock.SetState(world, pos, true);
    }

    public void Disarm () {
        armed = false;
        MiniSiloBlock.SetState(world, pos, false);
    }
    
}
