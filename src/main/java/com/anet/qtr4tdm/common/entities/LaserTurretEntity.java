package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.uebki.WorldAddition;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LaserTurretEntity extends TurretEntity {

    public float charge = 0;

    private final double laserRange = 100;

    public Vec3d clientLaserDist;
    public Vec3d clientLaserOrigin;

    public LaserTurretEntity(World worldIn) {
        super(worldIn);
        setSize(2, 3);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return charge < 0.8f ? super.getRenderBoundingBox() : getEntityBoundingBox().expand(500, 500, 500);
    }

    @Override
    protected int getDetectionRange() {
        return 75;
    }

    @Override
    protected double getGunHeight() {
        return 1.19d;
    }

    @Override
    public long getMaxEnergy() {
        return 10000;
    }

    @Override
    protected double getTickConsumption() {
        return 8;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (target != null) {
            charge += (1 - charge) * 0.05f;
        }
        else {
            charge += (0 - charge) * 0.01f;
        }

        if (charge > 0.8f) {

            //SERVERSIDE LASER
            if (!world.isRemote && getEnergy() > 0) {
                shoot();
            }

            //CLIENTSIDE LASER
            if (world.isRemote && target != null) {
                double pitchSin = Math.sin(pitchTurret);
                double pitchCos = Math.cos(pitchTurret);
                clientLaserDist = new Vec3d(Math.cos(yawTurret), -pitchSin / pitchCos ,Math.sin(yawTurret)).normalize().scale(laserRange);
                Vec3d absoluteVector = target.getPositionVector().subtract(getGunPosition().normalize().scale(laserRange));
                if (clientLaserDist.distanceTo(absoluteVector) < 0.2d) clientLaserDist = absoluteVector;
                clientLaserDist = clientLaserDist.add(getGunPosition());
                clientLaserOrigin = getGunPosition().add(clientLaserDist.subtract(getGunPosition()).scale(0.5d / laserRange));
            }
            
        }
        else {

            //Сброс лазера для, чтобы не рендерился.
            if (world.isRemote) {
                clientLaserDist = null;
                clientLaserOrigin = null;
            }

        }

        
    }

    public double getGaussianRandom () {
        return rand.nextGaussian();
    }

    @Override
    public void shoot () {
        injectEnergy(-120);
        Vec3d direction = getFacingDir().scale(laserRange);
        processLaserBeam(getGunPosition(), direction.add(getGunPosition()));
    }

    protected void processLaserBeam (Vec3d origin, Vec3d destination) {
        List<RayTraceResult> entities = WorldAddition.traceEntities(world, origin, destination, this);
        for (RayTraceResult result : entities) {
            result.entityHit.attackEntityFrom(DamageSource.HOT_FLOOR, Math.max((charge - 0.8f) * 75, 0));
            if (charge > 0.9f) result.entityHit.setFire(1);
        }
    }
    
}
