package com.anet.qtr4tdm.common.tiles;

import com.anet.qtr4tdm.common.blocks.MiniSiloBlock;
import com.anet.qtr4tdm.common.entities.RocketEntity;
import com.anet.qtr4tdm.uebki.RadarsInfo;
import com.anet.qtr4tdm.uebki.Teams;
import com.anet.qtr4tdm.uebki.player;
import com.anet.qtr4tdm.uebki.teamState;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MiniSiloTile extends TileEntity implements net.minecraft.util.ITickable {

    public boolean armed;
    public teamState team;
    public EntityLivingBase target;
    private int counter;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("armed", armed);
        compound.setInteger("team", team.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        armed = compound.getBoolean("armed");
        team = teamState.values()[compound.getInteger("team")];
        super.readFromNBT(compound);
    }
    
    @Override
    public void onLoad () {
        if (!world.isRemote) {
            player p = Teams.GetClosestPlayer(pos);
            if (p != null) {
                team = p.team;
            }
            markDirty();
        }
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
                target = RadarsInfo.GetTargetForSilo(this);
                if (target != null && armed) Launch();
                counter = 0;
            }
            else counter++;
        }
    }
}
