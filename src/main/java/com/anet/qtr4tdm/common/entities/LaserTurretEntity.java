package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.uebki.WorldAddition;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
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
        
    }

    @Override
    protected int getDetectionRange() {
        return 50;
    }

    @Override
    protected double getGunHeight() {
        return 1.19d;
    }

    @Override
    public double getMaxEnergy() {
        return 100000;
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

    protected void shoot () {
        injectEnergy(-128);
        Vec3d direction = new Vec3d(Math.cos(yawTurret), -Math.tan(pitchTurret) ,Math.sin(yawTurret)).normalize().scale(laserRange);
        processLaserBeam(getGunPosition(), direction.add(getGunPosition()));
    }

    protected void processLaserBeam (Vec3d origin, Vec3d destination) {
        List<Entity> entities = WorldAddition.traceEntities(world, origin, destination, this);
        for (Entity result : entities) {
            result.attackEntityFrom(DamageSource.HOT_FLOOR, Math.max((charge - 0.8f) * 75, 0));
            result.setFire(1);
        }
    }
    
}
