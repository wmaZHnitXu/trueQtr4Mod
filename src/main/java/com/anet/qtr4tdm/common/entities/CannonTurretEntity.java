package com.anet.qtr4tdm.common.entities;

import com.flansmod.common.FlansMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CannonTurretEntity extends TurretEntity {

    private ItemStack ammoInside;
    private int reloadingCooldown;


    //KNOCKBACK

    private float knockBackOffset;
        public Float getKnockBack () { return knockBackOffset; }
    
    public float getMaxKnockBackOffset () {
        return 0.7f;
    }

    private boolean isKnockingBack;

    //END KNOCKBACK


    public CannonTurretEntity(World worldIn) {
        super(worldIn);
        ammoInside = ItemStack.EMPTY;
        setSize(4, 4);
    }

    public int getReloadingCooldown () {
        return 80;
    }

    protected void ConsumeAmmo () {
        if (baseTile != null) {

        }
    }

    @Override
    protected int getDetectionRange() {
        return 50;
    }

    @Override
    protected double getGunHeight() {
        return 1.3d;
    }

    @Override
    public double getMaxEnergy() {
        return 10000;
    }

    @Override
    protected double getTickConsumption() {
        return 8;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote) {

            //Reloading
            if (reloadingCooldown <= 0) {

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

            else { reloadingCooldown--; }

        }
    }

    public void shoot () {

        if (!world.isRemote) {

            //Remove ammo
            ammoInside = ItemStack.EMPTY;

            //Spawn projectile
            CannonProjectileEntity projectile = new CannonProjectileEntity(world);
            Vec3d origin = getProjectileOrigin();
            projectile.setVelocity(getFacingDir().scale(3));
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

            for (int i = 0; i < 20; i++) {
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
                world.spawnParticle(EnumParticleTypes.LAVA, true, origin.x, origin.y, origin.z,
                getFacingDir().x - rand.nextGaussian(),
                getFacingDir().y - rand.nextGaussian(),
                getFacingDir().z - rand.nextGaussian());
            }

        }
    }

    protected void doKnockBack () {
        isKnockingBack = true;
    }

    public void calculateKnockBack (float partialTicks) {
        if (isKnockingBack) {
            knockBackOffset += (getMaxKnockBackOffset() - knockBackOffset) * 0.4f * partialTicks;
            if (getMaxKnockBackOffset() - knockBackOffset < 0.1f) isKnockingBack = false;
        }
        else {
            knockBackOffset += Math.min((0 - knockBackOffset) * 0.01f * partialTicks, -0.005);
            if (knockBackOffset < 0) knockBackOffset = 0;
        }

    }

    protected Item getAmmoType () {
        return FlansMod.workbenchItem;
    }

    protected Vec3d getProjectileOrigin () {
        return getGunPosition().add(getFacingDir().scale(3.25d));
    }
    
}
