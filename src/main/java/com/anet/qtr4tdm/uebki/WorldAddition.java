package com.anet.qtr4tdm.uebki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WorldAddition {
    public static List<RayTraceResult> traceEntities (World world, Vec3d start, Vec3d end, Entity excluded) {
        {
            HashMap<Entity, RayTraceResult> result = new HashMap<Entity, RayTraceResult>();
            double d0 = start.x;
            double d1 = start.y;
            double d2 = start.z;
            double d3 = end.x - start.x;
            double d4 = end.y - start.y;
            double d5 = end.z - start.z;
            Vec3d vec3d = new Vec3d(d0, d1, d2);
            Vec3d vec3d1 = new Vec3d(d0 + d3, d1 + d4, d2 + d5);
            RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1, false, true, false);

            if (true)
            {
                if (raytraceresult != null)
                {
                    vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
                }

                AxisAlignedBB bb = new AxisAlignedBB(new BlockPos(start), new BlockPos(end));
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(excluded, bb.expand(d3, d4, d5).grow(1.0D));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity1 = list.get(i);

                    if (entity1.canBeCollidedWith() && excluded != entity1)
                    {
                        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                        RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                        if (raytraceresult1 != null)
                        {
                            if (!result.containsKey(entity1)) {
                                raytraceresult1.entityHit = entity1;
                                result.put(entity1, raytraceresult1);
                            }
                        }
                    }
                }
            }
            return new ArrayList<RayTraceResult>(result.values());
        }
    }
}
