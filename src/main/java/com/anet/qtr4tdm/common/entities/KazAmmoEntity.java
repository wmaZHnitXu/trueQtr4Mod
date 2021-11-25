package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.common.tiles.Kaz1Tile;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class KazAmmoEntity extends Entity {

    private Entity target;
    private double verticalSpeed;
    public int countdown;

    public KazAmmoEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        countdown = 20;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        
    }

    @Override
    public void onUpdate() {
        if (countdown == 20) FineInit();
        if (countdown > 0) countdown--;
        else Explode();

        setPosition(getPositionVector().x, getPositionVector().y + verticalSpeed, getPositionVector().z);
        verticalSpeed = verticalSpeed + 0.05f * (0 - verticalSpeed);
        world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, getPosition().getX(), getPosition().getY(), getPosition().getZ(), 0, 0, 0);
        super.onUpdate();
    }

    private void Explode () {
        if (target != null && !world.isRemote) target.setDead();
        setDead();
    }

    private void FineInit () {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(getPosition());
            if (te instanceof Kaz1Tile) {
                target = ((Kaz1Tile)te).target;
            }
        }
        verticalSpeed = 1f;
    }
}
