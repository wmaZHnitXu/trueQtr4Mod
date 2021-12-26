package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.anet.qtr4tdm.common.blocks.ThermalRadarBlock;
import com.anet.qtr4tdm.common.supers.RadarTile;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ThermalBaseTile extends RadarTile {

    private static HashMap<BlockPos, Float> rotatins;

    @Override
    public void onLoad() {
        maxEnergy = 100;
        if (world.isRemote) {
            if (rotatins == null)
                rotatins = new HashMap<BlockPos, Float>();
            else if (!rotatins.containsKey(pos)) rotatins.put(pos, 0f);
        }
        super.onLoad();
    }

    @SideOnly(Side.CLIENT)
    public float getRotation () {
        if (rotatins.containsKey(pos))
            return rotatins.get(pos);
        else return 0f;
    }

    @SideOnly(Side.CLIENT)
    public void SetRotation (float rot) {
        rotatins.put(pos, rot);
    }

    @Override
    public boolean isInRange(EntityPlayer e) {
        return (e.getPosition().distanceSq(pos) < 3000 && powered);
    }

    @Override
    public int getSinkTier() {
        return 2;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return side == EnumFacing.DOWN;
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {
        
    }

    @Override
    public void Refresh() {
        
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            energy = Math.max(0, energy - 16);
            if (powered != energy > 50) {
                powered = energy > 50;
                ThermalRadarBlock.SetActive(world, pos, powered);
            }
        }
        else {
            powered = world.getBlockState(pos).getValue(ThermalRadarBlock.ACTIVE);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }
}
