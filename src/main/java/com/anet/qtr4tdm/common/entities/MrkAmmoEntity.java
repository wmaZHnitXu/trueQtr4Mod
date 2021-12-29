package com.anet.qtr4tdm.common.entities;

import java.util.List;

import com.anet.qtr4tdm.common.entities.render.Trail;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class MrkAmmoEntity extends Entity implements IEntityAdditionalSpawnData {

    private Vec3d targetPos;
    private Entity target;
    private double speed;
    final private double maxSpeed = 2d;
    final private double accelleration = 0.1d;
    private final double maxTurnSpeed = 0.2d;
    private double turnSpeed;
    private Vec3d velocityNormal;
    private Vec3d dirNormal;
    private Vec3d velocityActual;
    public Trail trail;
    public static final float damage = 8f;
    private boolean isTargetGone;

    public MrkAmmoEntity(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        velocityNormal = new Vec3d(0, 1, 0);
        speed = maxSpeed * 0.5d;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("xtarget"))
            targetPos = new Vec3d(compound.getDouble("xtarget"), compound.getDouble("ytarget"), compound.getDouble("ztarget"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (target != null) {
            compound.setDouble("xtarget", targetPos.x);
            compound.setDouble("ytarget", targetPos.y);
            compound.setDouble("ztarget", targetPos.z);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        //MOVEMENT
        if (target == null) targetPos = getPositionVector().addVector(0, -255, 0);
        if (getPositionVector().distanceTo(targetPos) < 4) isTargetGone = true;
        

        //VELOCITY
        if (!isTargetGone) {
            dirNormal = targetPos.subtract(getPositionVector()).normalize();
            turnSpeed = ticksExisted > 20 ? maxTurnSpeed : maxTurnSpeed * 0.03d;
            velocityNormal = slerp(velocityNormal, dirNormal, turnSpeed);
        }

        speed = speed + accelleration * (maxSpeed - speed);

        velocityActual = velocityNormal.scale(speed);

        //ROTATION
        float f1 = MathHelper.sqrt(velocityActual.x * velocityActual.x + velocityActual.z * velocityActual.z);
        this.rotationYaw = (float)(MathHelper.atan2(velocityActual.x, velocityActual.z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(velocityActual.y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;

        //APPLY MOVEMENT
        setPositionAndRotation(posX + velocityActual.x, posY + velocityActual.y, posZ + velocityActual.z
        , this.rotationYaw, this.rotationPitch);

        //EXPLODE
        if (ticksExisted > 200) setDead();

        //EFFECT
        if (world.isRemote) {
            
            Vec3d paritcleVel = Vec3d.ZERO.subtract(velocityNormal).scale(maxSpeed * 0.2d);
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, posX, posY, posZ, 
            paritcleVel.x * Math.abs(rand.nextGaussian()),
            paritcleVel.y * Math.abs(rand.nextGaussian()),
            paritcleVel.z * Math.abs(rand.nextGaussian()));
        }

        //IMPACT
        if ((!world.isAirBlock(getPosition()) && ticksExisted > 5) || (target != null && target.getPositionVector().squareDistanceTo(getPositionVector()) <= 1)) {
            Detonate();
        }
    }

    private Vec3d slerp (Vec3d start, Vec3d end, double precent) {
        double dot = start.dotProduct(end);
        dot = clamp(dot, -1, 1);
        double theta = Math.acos(dot) * precent;
        Vec3d relatiVec3d = end.subtract(start.scale(dot)).normalize();
        return (start.scale(Math.cos(theta))).add(relatiVec3d.scale(Math.sin(theta)));
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public void SetTargetAndTargetPos (Entity target) {
        this.target = target;
        if (target != null) {

            Vec3d velocityCorrective = (new Vec3d(motionX, motionY, motionZ)).scale(target.getPositionVector().distanceTo(getPositionVector()) / maxSpeed);

            targetPos = (new Vec3d(target.getPositionVector().x + rand.nextGaussian() * 5, target.getPositionVector().y, target.getPositionVector().z + rand.nextGaussian() * 5))
            .add(velocityCorrective);
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        if (target != null) {
            buffer.writeInt(target.getEntityId());
            buffer.writeDouble(targetPos.x);
            buffer.writeDouble(targetPos.y);
            buffer.writeDouble(targetPos.z);
        }
        else {
            buffer.writeInt(0);
            buffer.writeDouble(getPosition().getX());
            buffer.writeDouble(0);
            buffer.writeDouble(getPosition().getZ());
        }
    }

    public void Detonate () {

        if (world.isRemote) {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, true, getPosition().getX() + 0.5d, getPosition().getY(), getPosition().getZ() + 0.5d, 0, 0, 0);
            for (int i = 0; i < 50; i++) {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, true, getPosition().getX() + 0.5d, getPosition().getY(), getPosition().getZ() + 0.5d,
                rand.nextGaussian() * 0.3d,
                rand.nextGaussian() * 0.3d,
                rand.nextGaussian() * 0.3d);
            }
        }
        else {
            world.playSound((EntityPlayer)null, getPosition().getX() + 0.5d, getPosition().getY(), getPosition().getZ() + 0.5d, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
            List<EntityLivingBase> inImpactZone = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPosition().add(-4, -4, -4), getPosition().add(4,4,4)));
            for (EntityLivingBase entity : inImpactZone) {
                entity.attackEntityFrom(DamageSource.FIREWORKS, damage);
            }
        }

        setDead();
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        int targetId = additionalData.readInt();
        Vec3d targetPos = new Vec3d(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());
        this.target = world.getEntityByID(targetId);
        this.targetPos = targetPos;
        Trail.TrailOn(this, 0.2f, 70, 1, false);
        trail = new Trail(this, 0.3f, 10, 1, true);
    }    
}
