package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.RadarObjectStructure;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.teamState;
import com.ibm.icu.util.BytesTrie.Result;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TerminalRadarTile extends TileEntity {
    public teamState myTeam;

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            myTeam = Teams.GetTeamOfPlayer(world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 10000, false));
        }
        super.onLoad();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("team", myTeam.ordinal());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        myTeam = teamState.values()[compound.getInteger("team")];
        super.readFromNBT(compound);
    }
}

