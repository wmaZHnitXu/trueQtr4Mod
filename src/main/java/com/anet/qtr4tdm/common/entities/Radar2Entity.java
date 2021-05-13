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

public class Radar2Entity extends Radar1Entity {

    
    public Radar2Entity (World worldIn, BlockPos position) {
        this(worldIn);
        this.position = position;
        
    }

    public Radar2Entity(World worldIn) {
        super(worldIn);
        rotationSpeed = 0.01f;
        setSize(3, 3);
    }
}
