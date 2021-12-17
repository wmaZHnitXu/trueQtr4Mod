package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.blocks.Kaz1Block;
import com.anet.qtr4tdm.common.tiles.Kaz1Tile;
import com.anet.qtr4tdm.uebki.Sounds;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class KazAmmoEntity extends Entity implements IEntityAdditionalSpawnData {

    private Entity target;
    private int targetId;
    public double verticalSpeed;
    public int countdown;
    public double finalY;
    public double currentY;

    public KazAmmoEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        countdown = 8;
        verticalSpeed = 2f;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }

    @Override
    public void onUpdate() {
        if (countdown == 8) FirstUpdate();
        if (countdown > 0) countdown--;
        else Explode();

        //setPosition(getPositionVector().x, getPositionVector().y + verticalSpeed, getPositionVector().z);
        world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY + currentY, posZ, 0, -1, 0);
        if (target != null) {
            Vec3d direction = target.getPositionVector().subtract(getPositionVector());
            setRotation(Double.valueOf(Math.atan2(direction.z, direction.x)).floatValue(), rotationPitch);
        }
        super.onUpdate();
    }

    private void FirstUpdate () {
        if (world.isRemote) {
            TileEntity te = world.getTileEntity(getPosition());
            if (te instanceof Kaz1Tile) {
                Kaz1Tile kaz = (Kaz1Tile)te;
                kaz.highlightTime = 40;
            }
        }
    }

    private void Explode () {
        if (target != null) {
            if (!world.isRemote) {
                target.setDead();
                world.playSound(null, getPosition(), Sounds.active_protection, SoundCategory.BLOCKS, 5f, (1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F) * 0.7F);
                Kaz1Tile.RefreshLocked(); 
            }
            else {
                GenerateParticleExplosion();
            }
        }
        setDead();
    }

    private void GenerateParticleExplosion () {
        Vec3d origin = getPositionVector().addVector(0, currentY, 0);
        Vec3d end = target.getPositionVector();
        Vec3d normalizedDirection = end.subtract(origin).normalize();
        double distance = origin.distanceTo(end);
        for (int i = 0; i < distance + 3; i++) {
            for (int j = 0; j < 5; j++) {
                Vec3d currentParticleOrigin = origin.add(normalizedDirection.scale(i));
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, 
                currentParticleOrigin.x + rand.nextGaussian(), 
                currentParticleOrigin.y + rand.nextGaussian(), 
                currentParticleOrigin.z + rand.nextGaussian(),
                normalizedDirection.x, normalizedDirection.y, normalizedDirection.z);
            }
        }
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, end.x, end.y, end.z, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, origin.x, origin.y, origin.z, 0, 0, 0);
    }

    public void SetTarget (Entity target) {
        this.target = target;
        if (target != null) {
            targetId = target.getEntityId();
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(targetId);  
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        targetId = additionalData.readInt();
        target = world.getEntityByID(targetId);
    }
}
