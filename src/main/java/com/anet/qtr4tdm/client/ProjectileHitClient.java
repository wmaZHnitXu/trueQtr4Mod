package com.anet.qtr4tdm.client;

import com.anet.qtr4tdm.TdmMod;
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

        switch (hitMessage.type) {
            case 0:
                SpawnCannonImpactParticles(hitMessage.dir, hitMessage.hitPos, hitMessage.blockPos);
                break;
            case 1:
                SpawnRailImpactParticles(hitMessage.dir, hitMessage.hitPos, hitMessage.blockPos);
            default:
                SpawnCannonImpactParticles(hitMessage.dir, hitMessage.hitPos, hitMessage.blockPos);
        }

    }

    public static void SpawnCannonImpactParticles (Vec3d impactVel, Vec3d impactPos, BlockPos pos) {

        TdmMod.logger.info(impactVel + " : " + impactPos + " : " + pos);

        int iters = Double.valueOf(Math.min(impactVel.distanceTo(Vec3d.ZERO) * 40, 100)).intValue();
        World world = Minecraft.getMinecraft().world;

        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, true, impactPos.x, impactPos.y, impactPos.z, 0, 0, 0);

        if (!pos.equals(BlockPos.ORIGIN)) {

            
            IBlockState state = world.getBlockState(pos);
            EnumParticleTypes particle = EnumParticleTypes.BLOCK_CRACK;
            

            for (int i = 0; i < iters * 2; i++) {

                Vec3d particleVelocity = impactVel.scale(-2d);
                particleVelocity = particleVelocity.addVector(world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextGaussian());
                world.spawnParticle(particle, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z, Block.getStateId(state));

            }
        }

        iters /= 2;

        for (int i = 0; i < iters; i++) {

            Vec3d particleVelocity = new Vec3d(world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextGaussian()).scale(0.5d);
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z);

        }

        for (int i = 0; i < iters; i++) {

            Vec3d particleVelocity = impactVel.scale(world.rand.nextGaussian());
            particleVelocity = particleVelocity.addVector(world.rand.nextGaussian(), world.rand.nextGaussian(), world.rand.nextGaussian());
            world.spawnParticle(EnumParticleTypes.LAVA, true, impactPos.x, impactPos.y, impactPos.z, particleVelocity.x, particleVelocity.y, particleVelocity.z);

        }

    }

    public static void SpawnRailImpactParticles (Vec3d impactVel, Vec3d impactPos, BlockPos pos) {
        SpawnCannonImpactParticles(impactVel, impactPos, pos);
    }

}
