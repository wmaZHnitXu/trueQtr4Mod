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

    public ProjectileEntity(World worldIn) {
        super(worldIn);
    }

    public void setVelocity (Vec3d velocity) {
        this.velocity = velocity;
    }

    @Override
    protected void entityInit() {

    }

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
        super.onUpdate();

        double newX = posX + velocity.x;
        double newY = posY + velocity.y;
        double newZ = posZ + velocity.z;

        if (!world.isRemote) {

            Vec3d start = getPositionVector();
            Vec3d end = new Vec3d(newX, newY, newZ);

            //TODO комбинированный рейтрейс.

            List<Entity> entities = WorldAddition.traceEntities(world, start, end, this);

            if (entities != null && entities.size() > 0) {
                doDamage(entities.get(0));
                doHit();
            }

            RayTraceResult result = world.rayTraceBlocks(start, end);

            if (result != null) {
                doHit(result);
            }

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
