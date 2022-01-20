package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.common.bases.baseInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class HarvesterDroneEntity extends Entity {

    private Vec3d targetPos;
    private ArrayList<ItemStack> items;
    private boolean isOnBase;
    private boolean isHarvesting;
    private ArrayList<Vec3d> targets;
    private int harvestTimer;
    private double speed;
    private int range;
    private double acceleration;
    private Vec3d landingPos;
    private baseInfo baseinf;
    private BlockPos basePos;
    private double recomendedHeight;
    private Vec3d wayPoint;

    public HarvesterDroneEntity(World worldIn) {
        super(worldIn);
        items = new ArrayList<ItemStack>();
        targets = new ArrayList<Vec3d>();
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
    }

    @Override
    public void onUpdate() {
        if (world.isRemote) return;

        if (isHarvesting && items.size() < GetMaxItemStacks()) {
            Harvest();
            if (!isHarvesting) {
                FindNext();
            }
        }

        //MOVEMENT
        if (targetPos != null && !isHarvesting) {

        }
        super.onUpdate();
    }

    private void GoToPointWithRecomendedHeight (Vec3d point) {
        Vec3d velocity = point.subtract(getPositionVector()).normalize().scale(speed);
        setPosition(getPositionVector().x + velocity.x, getPositionVector().y + velocity.y, getPositionVector().z + velocity.z);
    }

    private void StartSortie (ArrayList<Vec3d> targetPositions) {
        if (items.size() >= GetMaxItemStacks()) return;
        targets = targetPositions;
        isOnBase = false;
    }

    private void FindNext () {
        speed = GetMaxSpeed();
        targetPos = GetNearestTarget();
        landingPos = GetLandingPosFromD(targetPos);
    }

    private Vec3d GetNearestTarget () {
        Vec3d result = null;
        if (targets.size() > 0) return null;
        result = targets.get(0);
        for (Vec3d potentialTarget : targets) {
            if (result.distanceTo(getPositionVector()) > potentialTarget.distanceTo(getPositionVector())) {
                result = potentialTarget;
            }
        }
        return result;
    }

    private Vec3d GetLandingPosFromD (Vec3d vector) {
        Vec3d result = vector;
        double height = world.getHeight(Double.valueOf(vector.x).intValue(), Double.valueOf(vector.z).intValue());
        result = new Vec3d(result.x, height, result.z);
        return result;
    }

    private void StartHarvesting () {
        harvestTimer = GetHarvestingTime();
    }

    private void StopHarvesting () {
        targets.remove(targetPos);
        targetPos = null;
    }

    public List<EntityItem> GetItemEntitiesToHarvest () {
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPosition().add(-range, -range, -range), getPosition().add(range, range, range)));
        if (items.size() > 0) return items;
        return null;
    }

    protected void Harvest () {
        harvestTimer--;
        List<EntityItem> items = GetItemEntitiesToHarvest();
        for (EntityItem item : items) {
            Vec3d velocity = getPositionVector().subtract(item.getPositionVector());
            item.setVelocity(velocity.x, velocity.y, velocity.z);
            if (item.getPositionVector().distanceTo(getPositionVector()) < 2) {
                ConsumeItem(item);
            }
        }
        if (harvestTimer <= 0 || this.items.size() >= GetMaxItemStacks() || items.size() == 0) isHarvesting = false;
    }

    private void ConsumeItem (EntityItem item) {
        if (items.size() < GetMaxItemStacks()) {
            items.add(item.getItem());
            item.setDead();
        }
    }

    protected int GetMaxItemStacks () {
        return 27;
    }

    protected int GetHarvestingTime () {
        return 100;
    }

    protected double GetMaxSpeed () {
        return 1d;
    }
}
