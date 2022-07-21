package com.anet.qtr4tdm.common.entities;

import com.anet.qtr4tdm.TdmMod;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RailTurretEntity extends CannonTurretEntity {

    public float charge;

    public void calculateCharge (float partialTicks) {
        charge += (additional / Double.valueOf(getMaxEnergy()).floatValue() - charge) * 0.01f * partialTicks;
    }


    public RailTurretEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected int getDetectionRange() {
        return 200;
    }

    @Override
    protected double getGunHeight() {
        return 1.3d;
    }

    @Override
    public long getMaxEnergy() {
        return 100000;
    }

    @Override
    protected double getTickConsumption() {
        return 8;
    }

    @Override
    protected void GunOperations() {
        if (!world.isRemote) {

            additional = Double.valueOf(energy).floatValue();

            
            //Reloading
            reloadingCooldown = Math.max(0, reloadingCooldown - 1);

            if (energy >= getMaxEnergy() - 10 && reloadingCooldown <= 0) {

                if (ammoInside.equals(ItemStack.EMPTY)) {
                    if (baseTile != null && baseTile.containsItemInside(getAmmoType())) {
                        ammoInside = baseTile.transferAmmoInTurret(getAmmoType());
                    }
                }

                //Shoot
                if (target != null && !ammoInside.equals(ItemStack.EMPTY)) { //сюда ещё условие сведения
                    shoot();
                }
            }
        }
        else {
            if (Math.abs(charge - additional / Double.valueOf(getMaxEnergy()).floatValue()) > 0.7f)
            charge = additional / Double.valueOf(getMaxEnergy()).floatValue();
        }
    }

    @Override
    public void shoot() {
        if (!world.isRemote) {

            //Remove ammo
            ammoInside = ItemStack.EMPTY;

            //Energy
            energy = 0;

            //Spawn projectile
            RailProjectileEntity projectile = new RailProjectileEntity(world);
            Vec3d origin = getProjectileOrigin();
            projectile.setVelocity(getFacingDir().scale(40));
            projectile.setPosition(origin.x, origin.y, origin.z);
            projectile.setParent(this);
            world.spawnEntity(projectile);
            reloadingCooldown = getReloadingCooldown();
        }

        if (world.isRemote) {

            //KnockBack

            doKnockBack();
            reloadingCooldown = getReloadingCooldown();
            Vec3d direction = getFacingDir();
            Vec3d origin = getProjectileOrigin();
            Vec3d up = new Vec3d(0,1,0);

            //Side particles

            for (int i = 0; i < 40; i++) {
                Vec3d directionParticle = direction.crossProduct(up);
                directionParticle = directionParticle.scale(rand.nextGaussian() * 0.25d);
                directionParticle.addVector(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, origin.x, origin.y, origin.z,
                directionParticle.x + rand.nextGaussian() * 0.1d,
                directionParticle.y + rand.nextGaussian() * 0.1d,
                directionParticle.z + rand.nextGaussian() * 0.1d);
            }

            //Forward particles
            for (int i = 0; i < 5; i++) {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, origin.x, origin.y, origin.z,
                getFacingDir().x - rand.nextGaussian(),
                getFacingDir().y - rand.nextGaussian(),
                getFacingDir().z - rand.nextGaussian());
            }
        }
    }
    
}
