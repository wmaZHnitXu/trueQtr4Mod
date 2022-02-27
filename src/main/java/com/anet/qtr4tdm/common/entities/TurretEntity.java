package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.common.tiles.TurretMasterTe;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TurretEntity extends Entity {

    protected Entity target;
    protected TurretMasterTe baseTile;

    public TurretEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        
    }

    protected void mainInit () {
        TileEntity downTile = world.getTileEntity(getPosition().down());
        if (downTile instanceof TurretMasterTe) {
            baseTile = (TurretMasterTe)downTile;
        }
        else {
            setDead();
            return;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {
            if (ticksExisted == 1) {
                mainInit();
            }
        }
    }
    
}
