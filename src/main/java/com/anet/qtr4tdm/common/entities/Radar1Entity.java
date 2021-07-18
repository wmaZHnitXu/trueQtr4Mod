package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.BasedRequest;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Radar1Entity extends Entity {

    public BlockPos position;
    public float rot;
    public boolean isActive;
    public float rotationSpeed;

    protected int counter;

    public Radar1Entity (World worldIn, BlockPos position) {
        super(worldIn);
        this.position = position;
        rotationSpeed = 0;
    }

    public Radar1Entity(World worldIn) {
        super(worldIn);
        setSize(3, 10);
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

    @Override
    public void onUpdate() {
        if (world.isRemote) {
            if (counter >= 20) {
                TdmMod.wrapper.sendToServer(new BasedRequest(getPosition(), 1, world.provider.getDimension()));
                counter = 0;
            }
            else counter++;
        }
        super.onUpdate();
    }

    public float getRotSpeed () {
        return isActive ? rotationSpeed : 0;
    }
}
