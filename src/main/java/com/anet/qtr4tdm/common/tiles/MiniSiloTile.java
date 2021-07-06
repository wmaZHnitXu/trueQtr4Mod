package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.common.blocks.MiniSiloBlock;
import com.anet.qtr4tdm.common.entities.RocketEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;

public class MiniSiloTile extends TileEntity implements net.minecraft.util.ITickable {

    public boolean armed;
    public EntityLivingBase target;
    private int counter;
    
    @Override
    public void onLoad () {
        
    }

    public void Arm () {
        armed = true;
        MiniSiloBlock.SetState(world, pos, true);
    }

    public void Disarm () {
        armed = false;
        MiniSiloBlock.SetState(world, pos, false);
    }

    public void Launch () {
        RocketEntity rocket = new RocketEntity(world);
        rocket.setPosition(pos.getX() + 0.5d, pos.getY(), pos.getZ() + 0.5d);
        world.spawnEntity(rocket);
        Disarm();
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (counter == 20) {
                if (target != null && armed) Launch();
                counter = 0;
            }
            else counter++;
        }
    }
}
