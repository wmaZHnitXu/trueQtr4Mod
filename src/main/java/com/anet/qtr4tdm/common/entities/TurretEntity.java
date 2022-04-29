package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.uebki.messages.primitive.TurretRotationMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TurretEntity extends Entity implements IEntityAdditionalSpawnData {

    protected Entity target;
    protected TurretMasterTe baseTile;
    protected int renderCooldown;

    protected double yawTurret;
        public Double getYawTurret () { return yawTurret; }

    protected double pitchTurret;
        public Double getPitchTurret () { return pitchTurret; }

    protected double newYaw;
    protected double newPitch;

    protected double getRotationSpeed () { return 0.05d; }

    protected double energy;
    public float additional;
    protected float oldAdditional;

    public TurretEntity(World worldIn) {
        super(worldIn);
    }

    protected abstract int getDetectionRange ();

    @Override
    protected void entityInit() {
        
    }

    protected void mainInit () {
        TileEntity downTile = world.getTileEntity(getPosition().down());
        TdmMod.logger.info(newPitch + " " + newYaw);
        if (downTile instanceof TurretMasterTe) {
            baseTile = (TurretMasterTe)downTile;
            baseTile.ConnectEntity(this);
        }
        else {
            if (!world.isRemote)
                setDead();
            return;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        yawTurret = compound.getDouble("yaw_turret");
        pitchTurret = compound.getDouble("pitch_turret");
        newYaw = yawTurret;
        newPitch = pitchTurret;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("yaw_turret", yawTurret);
        compound.setDouble("pitch_turret", pitchTurret);
    }

    public abstract void shoot ();

    @Override
    public void onUpdate() {
        super.onUpdate();

        //FIRST TICK INIT

        if (ticksExisted == 1) { //Если онапдейт сверху, то инициализацию на тике 1 производим
            mainInit();
        }

        //LONG INTERVAL UPDATES

        if (ticksExisted % 10 == 0) {
            doUnfrequentUpdate();
        }

        if (ticksExisted % 40 == 0) {
            doVeryUnfrequentUpdate();
        }

        if (!world.isRemote && (baseTile == null || baseTile.isInvalid())) setDead();
        if (target != null && target.isDead) target = null;

        //ROTATING

            //SERVERSIDE
            if (!world.isRemote) {

                double oldYaw = yawTurret;
                double oldPitch = pitchTurret;

                if (target != null) {

                    Vec3d targetVector = target.getPositionEyes(1);
                    Vec3d direction = targetVector.subtract(getGunPosition()).normalize();

                    newYaw = Math.atan2(direction.z, direction.x);
                    newPitch = - Math.asin(direction.y);

                    
                    double yawAddition = Math.toDegrees(newYaw - yawTurret);

                    while(yawAddition < -180.0F)
                    {
                        yawAddition += 360.0F;
                    }
                
                    while(yawAddition >= 180.0F)
                    {
                        yawAddition -= 360.0F;
                    }

                    yawAddition = Math.toRadians(yawAddition);

                    yawTurret += Math.min(Math.max((yawAddition), -getRotationSpeed()), getRotationSpeed());
                    pitchTurret += Math.min(Math.max((newPitch - pitchTurret), -getRotationSpeed()), getRotationSpeed());
                }

                if (oldYaw != yawTurret || oldPitch != pitchTurret || oldAdditional != additional) {
                    oldAdditional = additional;
                    sendRotationsToClients();
                }
                
            }

            //CLIENTSIDE
            else {
                
                if (renderCooldown > 5) {
                    calculatePitchYaw(1);
                }
                if (renderCooldown < 100000)
                    renderCooldown++;
            }

            //ENERGY

            injectEnergy(-getTickConsumption());

    }

    @SideOnly(Side.CLIENT)
    public void calculatePitchYaw (float partialTicks) {
        if (partialTicks != 1) renderCooldown = 0;
        if (target != null) {

            Vec3d targetVector = target.getPositionEyes(1);
            Vec3d direction = targetVector.subtract(getGunPosition()).normalize();

            newYaw = Math.atan2(direction.z, direction.x);
            newPitch = - Math.asin(direction.y);

            double yawAddition = Math.toDegrees(newYaw - yawTurret);

            while(yawAddition < -180.0F)
            {
                yawAddition += 360.0F;
            }
        
            while(yawAddition >= 180.0F)
            {
                yawAddition -= 360.0F;
            }

            yawAddition = Math.toRadians(yawAddition);

            yawTurret += Math.min(Math.max((yawAddition), -getRotationSpeed() * partialTicks), getRotationSpeed() * partialTicks);
            pitchTurret += Math.min(Math.max((newPitch - pitchTurret), -getRotationSpeed() * partialTicks), getRotationSpeed() * partialTicks);
        }
    }

    @SideOnly(Side.SERVER)
    protected void sendRotationsToClients () {
        //List<EntityPlayerMP> playersToSend = world.getPlayers(EntityPlayerMP.class, player -> player.getPositionVector().distanceTo(getPositionVector()) < 400); лол оказца метод для этого есть
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 400);
        TdmMod.wrapper.sendToAllAround(new TurretRotationMessage(getEntityId(), newYaw, newPitch, target == null ? 0 : target.getEntityId(), additional), point);
    }
    //Чзх я хз, пришлось вверх вставить, т.к ошибку выдаёт, хотя всё верно
    //Predicate<EntityPlayerMP> shouldSendTo = player -> player.getPositionVector().distanceTo(getPositionVector()) < 400;

    public void insertTurretRotations (double pitch, double yaw, int targetId, float additional) {
        newPitch = pitch;
        newYaw = yaw;
        this.additional = additional;
        if (targetId == 0) {
            target = null;
            return; //WARNING RETURN
        }
        if (targetId != 0 && (target == null || target.getEntityId() != targetId)) {
            target = world.getEntityByID(targetId);
        }
    }

    protected void doUnfrequentUpdate () {
        if (!world.isRemote) {
            if (target != null) {
                if (energy <= 0 || (!traceEntity(target) || target.getDistance(posX, posY, posZ) > getDetectionRange())) {
                    target = null;
                    sendRotationsToClients();
                }
            }
            if (target == null && energy > 0) target = searchForTarget();
        }
    }

    protected void doVeryUnfrequentUpdate () {
        if (!world.isRemote) {
            
        }
    }

    protected abstract double getGunHeight ();

    public Vec3d getGunPosition () {
        return getPositionVector().addVector(0, getGunHeight(), 0);
    }

    protected boolean traceEntity (Entity entity) {
        //, false, true, false
        RayTraceResult result = world.rayTraceBlocks(getGunPosition(), entity.getPositionEyes(1));
        return result == null;
    }

    protected boolean tracePositions (Vec3d position, Vec3d tracePos) {

        Vec3d incrementor = tracePos.subtract(position).normalize();
        Vec3d tracer = position;
        boolean result = true;

        while (tracePos.distanceTo(tracer) > 0.5d) {
            tracer = tracer.add(incrementor);
            BlockPos currentBlockPos = new BlockPos(tracer);
            IBlockState state = world.getBlockState(currentBlockPos);
            AxisAlignedBB collider = state.getCollisionBoundingBox(world, currentBlockPos);
            if (!world.isAirBlock(currentBlockPos) || (collider != null && collider.offset(currentBlockPos).contains(tracer))) {
                return false;
            }
        }

        return result;

    }

    public Entity searchForTarget () {
        Entity result = null;

        AxisAlignedBB bb = new AxisAlignedBB(getPosition().add(getDetectionRange(), getDetectionRange(), getDetectionRange()),
        getPosition().add(-getDetectionRange(), -getDetectionRange(), -getDetectionRange()));
        ArrayList<Entity> candidates = new ArrayList<Entity>(world.getEntitiesWithinAABB(EntityPlayer.class, bb));
        //DISTANCE CHECK & RAYTRACE CHECK
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getPositionVector().distanceTo(getPositionVector()) > getDetectionRange() ||
                !traceEntity(candidates.get(i))) {

                candidates.remove(i);
                i--;
            }
        }

        if (candidates.size() > 0) result = candidates.get(0);
        
        return result;
    }

    public Vec3d getFacingDir () {
        return new Vec3d(Math.cos(yawTurret), -Math.tan(pitchTurret) ,Math.sin(yawTurret)).normalize();
    }


    public abstract double getMaxEnergy ();

    public double getEnergy () {
        return energy;
    }

    public double getDemandedEnergy () {
        return getMaxEnergy() - energy;
    }

    protected abstract double getTickConsumption ();

    public void injectEnergy (double amount) {
        energy += amount;
        energy = Math.max(Math.min(getMaxEnergy(), energy), 0);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeDouble(getYawTurret());
        buffer.writeDouble(getPitchTurret());        
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        yawTurret = additionalData.readDouble();
        pitchTurret = additionalData.readDouble();
        newYaw = yawTurret;
        newPitch = pitchTurret;
    }
}
