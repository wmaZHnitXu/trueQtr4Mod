package com.anet.qtr4tdm.common.entities;

import java.util.List;

import com.anet.qtr4tdm.uebki.WorldAddition;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData {

    protected Vec3d velocity;
    protected float damage;

    protected int lifetime;

    public ProjectileEntity(World worldIn) {
        super(worldIn);
        damage = getInitDamage();
        lifetime = getInitLifetime();
    }

    public void setVelocity (Vec3d velocity) {
        this.velocity = velocity;
    }

    public int getInitLifetime () {
        return 4000;
    }

    @Override
    protected void entityInit() {

    }

    public abstract float getInitDamage ();

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

        velocity = new Vec3d(compound.getDouble("velx"), compound.getDouble("vely"), compound.getDouble("velz"));      

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) 
    {
        compound.setDouble("velx", velocity.x);
        compound.setDouble("vely", velocity.y);
        compound.setDouble("velz", velocity.z);

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        
        buffer.writeDouble(velocity.x);
        buffer.writeDouble(velocity.y);
        buffer.writeDouble(velocity.z);

    }

    @Override
    public void onUpdate() {

        if (!world.isRemote) {

            super.onUpdate();

            lifetime--;
            if (lifetime <= 0) { setDead(); return; }
        }

        double newX = posX + velocity.x;
        double newY = posY + velocity.y;
        double newZ = posZ + velocity.z;

        Vec3d start = getPositionVector();
        Vec3d end = new Vec3d(newX, newY, newZ);

        //TODO комбинированный рейтрейс.

        List<RayTraceResult> entities = WorldAddition.traceEntities(world, start, end, this);

        if (entities != null && entities.size() > 0) {
            RayTraceResult res = entities.get(0);
            setPosition(res.hitVec.x, res.hitVec.y, res.hitVec.z);
            if (!world.isRemote) {
                doDamage(res.entityHit);
                doHit();
            }
            else {
                setDead();
            }
        }

        RayTraceResult result = world.rayTraceBlocks(start, end);

        if (result != null) {
            if (!world.isRemote) {
                doHit(result);
            }
            else {
                setDead();
            }
        }

        if (!isDead) {
            posX = newX;
            posY = newY;
            posZ = newZ;
        }

    }

    public abstract void doDamage (Entity e);

    public abstract void doHit ();

    public abstract void doHit (RayTraceResult trace);

    @Override
    public void readSpawnData(ByteBuf additionalData) {

        velocity = new Vec3d(additionalData.readDouble(), additionalData.readDouble(), additionalData.readDouble());

    }
    
}
