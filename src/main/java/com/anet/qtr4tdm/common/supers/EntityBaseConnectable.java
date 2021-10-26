package com.anet.qtr4tdm.common.supers;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;

public abstract class EntityBaseConnectable extends Entity implements IBaseConnectable {

    public EntityBaseConnectable(World worldIn) {
        super(worldIn);
    }

    public baseInfo base;
    public int lastBaseId;

    @Override
    public boolean ConnectToBase() {
        if (world.isRemote) return false;
        baseInfo candidate = InWorldBasesManager.GetBaseOfTerritory(getPosition());
        if (candidate == null) return false;
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
            return true;
        }
        return false;
    }

    @Override
    public void DisconnectFromBase() {
        if (base != null) {
            base.DisconnectDefenceSystem(this);
        }
        base = null;
        
    }

    @Override
    public baseInfo GetBase() {
        return base;
    }

    @Override
    public void setDead() {
        DisconnectFromBase();
        super.setDead();
    }
    

    @Override
    public BlockPos getPosForBase () {
        return getPosition();
    }

    @Override
    public void onUpdate() {
        if (base == null && ticksExisted % 10 == 0) {
            InWorldBasesManager.GetBaseConnection(this);
        }
        super.onUpdate();
    }
    
}
