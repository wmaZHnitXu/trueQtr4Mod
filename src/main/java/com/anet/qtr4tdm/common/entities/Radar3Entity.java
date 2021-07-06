package com.anet.qtr4tdm.common.entities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Radar3Entity extends Radar1Entity {

    public Radar3Entity (World worldIn, BlockPos position) {
        this(worldIn);
        this.position = position;
        
    }

    public Radar3Entity(World worldIn) {
        super(worldIn);
        rotationSpeed = 0.01f;
        setSize(3, 3);
    }
}
