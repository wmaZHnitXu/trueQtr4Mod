package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.blocks.MiniSiloBlock;
import com.anet.qtr4tdm.common.entities.RocketEntity;
import com.anet.qtr4tdm.common.supers.TileEntityDefence;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class MiniSiloTile extends TileEntityDefence implements net.minecraft.util.ITickable {

    public boolean armed;
    public Entity target;
    private int counter;

    @Override
    public void onLoad() {
        DisconnectFromBase();
        InWorldBasesManager.GetBaseConnection(this);
        armed = world.getBlockState(pos).getValue(MiniSiloBlock.armed);
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
    public void readFromNBT(NBTTagCompound compound) { //vajno
        super.readFromNBT(compound);

    }

    @Override
    public void setPos(BlockPos posIn) {
        super.setPos(posIn);
        //DisconnectFromBase();
        //InWorldBasesManager.GetBaseConnection(this);
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

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {
        if (targets != null && targets.size() > 0) {
            target = targets.get(0);
        }
        else target = null;
    }

    @Override
    public void Refresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getPoints() {
        return 35;
    }

    @Override
    public int getRange() {
        return 100;
    }
}
