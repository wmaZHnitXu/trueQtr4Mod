package com.anet.qtr4tdm.common.entities;

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
