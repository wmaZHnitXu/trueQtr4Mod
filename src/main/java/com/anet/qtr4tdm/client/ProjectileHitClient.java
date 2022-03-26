package com.anet.qtr4tdm.client;

import com.anet.qtr4tdm.uebki.messages.primitive.ProjectileHitMessage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileHitClient {
    
    public static void doProjectileHit (ProjectileHitMessage hitMessage) {

        

        if (hitMessage.blockPos.equals(BlockPos.ORIGIN)) {

        }

        else {
            SpawnImpactParticles(hitMessage.dir, hitMessage.hitPos, hitMessage.blockPos);
        }

    }

    public static void SpawnImpactParticles (Vec3d impactVel, Vec3d impactPos, BlockPos pos) {

        World world = Minecraft.getMinecraft().world;
        IBlockState state = world.getBlockState(pos);
        EnumParticleTypes particle = EnumParticleTypes.BLOCK_CRACK;
        int iters = Double.valueOf(Math.min(impactVel.distanceTo(Vec3d.ZERO), 40)).intValue();

        for (int i = 0; i < iters; i++) {

            Vec3d particleVelocity = impactVel.scale(world.rand.nextGaussian() * 0.25d);
            particleVelocity = particleVelocity.addVector(world.rand.nextGaussian() * 0.25d, world.rand.nextGaussian() * 0.25d, world.rand.nextGaussian() * 0.25d);
            world.spawnParticle(particle, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z, Block.getStateId(state));

        }

        iters /= 2;

        for (int i = 0; i < iters; i++) {

            Vec3d particleVelocity = new Vec3d(world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextGaussian()).scale(0.1d);
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z);

        }

        for (int i = 0; i < iters; i++) {

            Vec3d particleVelocity = impactVel.scale(world.rand.nextGaussian() * 0.25d);
            particleVelocity = particleVelocity.addVector(world.rand.nextGaussian() * 0.25d, world.rand.nextGaussian() * 0.25d, world.rand.nextGaussian() * 0.25d);
            world.spawnParticle(EnumParticleTypes.LAVA, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z);

        }

    }

}
