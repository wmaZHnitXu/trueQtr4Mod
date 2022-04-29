package com.anet.qtr4tdm.common.entities;
import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.render.Trail;
import com.anet.qtr4tdm.common.entities.render.Vec3f;
import com.anet.qtr4tdm.uebki.messages.primitive.ProjectileHitMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class RailProjectileEntity extends CannonProjectileEntity {

    public Trail innerTrail;

    public RailProjectileEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    public void doHit() {
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, getPositionVector(), BlockPos.ORIGIN, 1), point);
        setDead();
    }

    @Override
    public void doHit(RayTraceResult trace) {
        if (trace.entityHit != null) {
            doDamage(trace.entityHit);
        }
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 200);
        TdmMod.wrapper.sendToAllAround(new ProjectileHitMessage(velocity, trace.hitVec, trace.getBlockPos(), 1), point);
        setDead();
    }

    @Override
    public float getInitDamage() {
        return 1000;
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        super.readSpawnData(additionalData);
        if (world.isRemote && !isDead) {
            Trail.TrailOn(this, 0.2f, 70, 1, false);
            innerTrail = new Trail(this, 0.1f, 70, 1, new Vec3f(0.5f, 1f, 0.7f));
        }
    }
}
