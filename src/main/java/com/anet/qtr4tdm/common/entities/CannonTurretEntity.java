package com.anet.qtr4tdm.common.entities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CannonTurretEntity extends TurretEntity {

    private ItemStack ammoInside;
    private int reloadingCooldown;


    //KNOCKBACK

    private float knockBackOffset;
        public Float getKnockBack () { return knockBackOffset; }
    
    public float getMaxKnockBackOffset () {
        return 0.5f;
    }

    private boolean isKnockingBack;

    //END KNOCKBACK


    public CannonTurretEntity(World worldIn) {
        super(worldIn);
        setSize(2, 4);
    }

    public int getReloadingCooldown () {
        return 40;
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
        if (reloadingCooldown <= 0) {
            if (target != null) {
                shoot();
            }
            else {

            }
        }
        else { reloadingCooldown--; }
    }

    protected void shoot () {
        doKnockBack();
        reloadingCooldown = getReloadingCooldown();
    }

    protected void doKnockBack () {
        isKnockingBack = true;
    }

    public void CalculateKnockBack (float partialTicks) {
        if (isKnockingBack) {
            knockBackOffset += (getMaxKnockBackOffset() - knockBackOffset) * 0.1f * partialTicks;
        }
        else {
            knockBackOffset += (0 - knockBackOffset) * 0.01f * partialTicks;
        }

    }

    protected Vec3d getProjectileOrigin () {
        return Vec3d.ZERO;
    }
    
}
