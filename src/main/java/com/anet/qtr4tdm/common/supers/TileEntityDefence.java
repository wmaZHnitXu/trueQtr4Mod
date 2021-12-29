package com.anet.qtr4tdm.common.supers;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;

public abstract class TileEntityDefence extends TileEntity implements IDefenceSystem {
    
    private int lastBaseId;
    public static int points;
    protected baseInfo base;

    @Override
    public boolean ConnectToBase() {
        if (world.isRemote) return false;
        baseInfo candidate = InWorldBasesManager.GetBaseOfTerritory(getPos());
        if (candidate == null) { System.out.println("nocand"); return false;}
        if (candidate.ConnectDefenceSystem(this)) {
            base = candidate;
            lastBaseId = candidate.id;

            Chunk chunk = world.getChunkFromBlockCoords(candidate.pos);
            if (!chunk.isLoaded()) {
                ForgeChunkManager.Ticket ticket = ForgeChunkManager.requestTicket(TdmMod.instance, world, ForgeChunkManager.Type.NORMAL);
                if (ticket != null) {
                    ForgeChunkManager.forceChunk(ticket, chunk.getPos());
                } else {
                    System.out.println("ALARM ALARM CHUNKI NEZYA PROGRYZIT' EBAT' KON4ILIS NAVERNO");
                }
            }
            markDirty();
            return true;
        }
        System.out.println("noway");
        return false;
    }

    @Override
    public BlockPos getPosForBase() {
        return getPos();
    }

    @Override
    public void DisconnectFromBase() {
        if (base != null)
        base.DisconnectDefenceSystem(this);
        base = null;        
    }

    @Override
    public baseInfo GetBase() {
        return base;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        lastBaseId = compound.getInteger("lastid");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("lastid", lastBaseId);
        return super.writeToNBT(compound);
    }
}
