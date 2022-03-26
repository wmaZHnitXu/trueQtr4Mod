package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.uebki.messages.primitive.ProjectileHitMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class CannonProjectileEntity extends ProjectileEntity {

    TurretEntity parent;

    public CannonProjectileEntity(World worldIn) {
        super(worldIn);
    }

    public void setParent (TurretEntity parent) {
        this.parent = parent;
    }

    @Override
    public void doDamage(Entity e) {
        
    }

    @Override
    public void doHit() {
        //TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        //TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, getPositionVector(), BlockPos.ORIGIN, 0), point);
    }

    @Override
    public void doHit(RayTraceResult trace) {
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, trace.hitVec, trace.getBlockPos(), 0), point);
        setDead();
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        super.readSpawnData(additionalData);
        Entity e = world.getEntityByID(additionalData.readInt());
        if (e instanceof CannonTurretEntity) {
            ((CannonTurretEntity)e).shoot();
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        super.writeSpawnData(buffer);
        buffer.writeInt(parent.getEntityId());
    }
    
}
