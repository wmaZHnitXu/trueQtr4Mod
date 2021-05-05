package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.teamState;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Radar3Entity extends Entity {

    BlockPos position;
    public float rot;
    public Radar3Entity (World worldIn, BlockPos position) {
        super(worldIn);
        this.position = position;
        
    }

    public Radar3Entity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void entityInit() {
        
    }
}
