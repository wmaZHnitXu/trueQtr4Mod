package com.anet.qtr4tdm.common.entities;

import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.MiniSiloTile;
import com.anet.qtr4tdm.uebki.Teams;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow;

public class RocketEntity extends Entity {

    public EntityLivingBase target;
    private Vec3d targetVector;
    private Vec3d currentVector;
    private double accell;
    private double speed;
    int countdown;

    double dispX;
    double dispZ;

    public RocketEntity(World worldIn) {
        super(worldIn);
    }

    public void SetTarget (EntityLivingBase target) {
        this.target = target;
    }

    @Override
    protected void entityInit() {
        if (world.isRemote) {
           // Minecraft.getMinecraft().getSoundHandler().playSound(sound);
           Vec3d pos = getPositionVector();
           world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.x, pos.y, pos.z, motionX, motionY, motionZ);
        }
        else {
            dispX = rand.nextGaussian();
            dispZ = rand.nextGaussian();
            currentVector = new Vec3d(0,2d,0);
            countdown = 40;
            speed = 1.5d;
            accell = 0.03d;            
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onUpdate() {
        if (countdown == 40) target = ((MiniSiloTile)world.getTileEntity(getPosition())).target;
        Vec3d pos = getPositionVector();
        if (!world.isRemote) {
            if (target == null) {super.onUpdate(); setDead(); return;}
            targetVector = target.getPositionVector().addVector(dispX, 0, dispZ).subtract(pos).normalize();
            targetVector = new Vec3d(targetVector.x * speed, targetVector.y * speed, targetVector.z * speed);
            currentVector = LinearInterp(currentVector, targetVector, accell);
            setLocationAndAngles(pos.x + currentVector.x, pos.y + currentVector.y, pos.z + currentVector.z, 0, 0);
            if (!world.getBlockState(getPosition()).getBlock().isAir(world.getBlockState(getPosition()), world, getPosition()) && countdown <= 0) {
                Boom();
                setDead();
            }
            if (countdown > 0) countdown--;
        }
        else {
            for (int i = 0; i < 5; i++) {
                double motionX = rand.nextGaussian() * 0.02D;
                double motionY = rand.nextGaussian() * 0.02D;
                double motionZ = rand.nextGaussian() * 0.02D;
                world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.x, pos.y, pos.z, motionX, motionY, motionZ);
            }
            motionX = rand.nextGaussian() * 0.02D;
            motionY = rand.nextGaussian() * 0.02D;
            motionZ = rand.nextGaussian() * 0.02D;
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, motionX, motionY, motionZ);
            world.spawnParticle(EnumParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0, 0);
        }
        super.onUpdate();
    }

    public Vec3d LinearInterp (Vec3d a, Vec3d b, double speed) {
        Vec3d result;
        double x = a.x;
        double y = a.y;
        double z = a.z;
        if (x < b.x) { x += speed; if (x > b.x) x = b.x; } else { x -= speed; if (x < b.x) x = b.x;}
        if (countdown == 0) {
            if (y < b.y) { y += speed; if (y > b.y) y = b.y; } else { y -= speed; if (y < b.y) y = b.y;}
        }
        if (z < b.z) { z += speed; if (z > b.z) z = b.z; } else { z -= speed; if (z < b.z) z = b.z;}
        result = new Vec3d(x, y, z);
        return result;
    }
    
    public void Boom () {
        TdmMod.logger.info("BOOM");
        BlockPos a = getPosition().add(4,4,4);
        BlockPos b = getPosition().add(-4,-4,-4);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(b,a));
        for (EntityLivingBase entity : entities) {
            entity.attackEntityFrom(DamageSource.FIREWORKS, 20);
        }
        BlockPos pos = getPosition();
        world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
    }
    @Override
    public void setDead() {
        if (world.isRemote) {
            BlockPos pos = getPosition();
            pos = pos.add(new BlockPos(0,1,0));
            for (int i = 0; i < 10; i++) {
                motionX = rand.nextGaussian() * 0.2D;
                motionY = rand.nextGaussian() * 0.2D;
                motionZ = rand.nextGaussian() * 0.2D;
                if (!world.isAirBlock(pos)) pos = pos.add(new BlockPos(0,1,0));
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX(), pos.getY(), pos.getZ(), motionX, motionY, motionZ);
            }
            world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
        }
        super.setDead();
    }
}
