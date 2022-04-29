package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.primitive.ProjectileHitMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class CannonProjectileEntity extends ProjectileEntity {

    TurretEntity parent;
    private boolean initialized;

    public CannonProjectileEntity(World worldIn) {
        super(worldIn);
    }

    public void setParent (TurretEntity parent) {
        this.parent = parent;
    }

    @Override
    public float getInitDamage() {
        return 50;
    }

    @Override
    public void doDamage(Entity e) {
        e.attackEntityFrom(DamageSource.ANVIL, damage);
    }

    @Override
    public void doHit() {
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, getPositionVector(), BlockPos.ORIGIN, 0), point);
        setDead();
    }

    @Override
    public void doHit(RayTraceResult trace) {
        if (trace.entityHit != null) {
            doDamage(trace.entityHit);
        }
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, trace.hitVec, trace.getBlockPos(), 0), point);
        setDead();
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        super.readSpawnData(additionalData);
        if (!initialized && !isDead) {
            Entity e = world.getEntityByID(additionalData.readInt());
            if (e instanceof TurretEntity) {
                ((TurretEntity)e).shoot();
            }
            initialized = true;
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        super.writeSpawnData(buffer);
        if (parent != null) {
            buffer.writeInt(parent.getEntityId());
        }
        else {
            buffer.writeInt(0);
        }
    }
    
}
