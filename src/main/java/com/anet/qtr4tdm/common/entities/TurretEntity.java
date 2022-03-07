package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.uebki.messages.primitive.TurretRotationMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TurretEntity extends Entity {

    protected Entity target;
    protected TurretMasterTe baseTile;

    protected Double yawTurret;
        public Double getYawTurret () { return yawTurret; }

    protected Double pitchTurret;
        public Double getPitchTurret () { return pitchTurret; }

    protected double newYaw;
    protected double newPitch;

    protected double getRotationSpeed () { return 0.1d; }

    public TurretEntity(World worldIn) {
        super(worldIn);
        yawTurret = Double.valueOf(0);
        pitchTurret = Double.valueOf(0);
        setSize(1, 1);
    }

    protected abstract int getDetectionRange ();

    @Override
    protected void entityInit() {
        
    }

    protected void mainInit () {
        TileEntity downTile = world.getTileEntity(getPosition().down());
        if (downTile instanceof TurretMasterTe) {
            baseTile = (TurretMasterTe)downTile;
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
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("yaw_turret", yawTurret);
        compound.setDouble("pitch_turret", pitchTurret);
    }

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
                    Vec3d direction = targetVector.subtract(getPositionVector().add(getGunPosition())).normalize();

                    newYaw = Math.toDegrees(Math.atan2(direction.z, direction.x));
                    newPitch = Math.toDegrees(Math.asin(direction.y));

                    yawTurret += Math.min(Math.max((newYaw - yawTurret), -getRotationSpeed()), getRotationSpeed());
                    pitchTurret += Math.min(Math.max((newPitch - pitchTurret), -getRotationSpeed()), getRotationSpeed());
                }

                if (oldYaw != yawTurret && oldPitch != pitchTurret) sendRotationsToClients();
                
            }

            //CLIENTSIDE
            else {

                yawTurret += Math.min(Math.max((newYaw - yawTurret), -getRotationSpeed()), getRotationSpeed());
                pitchTurret += Math.min(Math.max((newPitch - pitchTurret), -getRotationSpeed()), getRotationSpeed());

            }

    }

    @SideOnly(Side.SERVER)
    protected void sendRotationsToClients () {
        //List<EntityPlayerMP> playersToSend = world.getPlayers(EntityPlayerMP.class, player -> player.getPositionVector().distanceTo(getPositionVector()) < 400); лол оказца метод для этого есть
        TargetPoint point = new TargetPoint(world.provider.getDimension(), posX, posY, posZ, 400);
        TdmMod.wrapper.sendToAllAround(new TurretRotationMessage(getEntityId(), newYaw, newPitch), point);
    }
    //Чзх я хз, пришлось вверх вставить, т.к ошибку выдаёт, хотя всё верно
    //Predicate<EntityPlayerMP> shouldSendTo = player -> player.getPositionVector().distanceTo(getPositionVector()) < 400;

    public void insertTurretRotations (double pitch, double yaw) {
        newPitch = pitch;
        newYaw = yaw;
    }

    protected void doUnfrequentUpdate () {
        if (!world.isRemote) {
            if (target == null) target = searchForTarget();
        }
    }

    protected void doVeryUnfrequentUpdate () {

    }

    protected abstract double getGunHeight ();

    protected Vec3d getGunPosition () {
        return getPositionVector().addVector(0, getGunHeight(), 0);
    }

    protected boolean traceEntity (Entity entity) {
        return tracePositions(getPositionVector().add(getGunPosition()), entity.getPositionEyes(1));
    }

    protected boolean tracePositions (Vec3d position, Vec3d tracePos) {

        Vec3d incrementor = tracePos.subtract(position).normalize();
        Vec3d tracer = position;
        boolean result = true;

        while (tracePos.distanceTo(tracer) > 1) {
            tracer = tracer.add(incrementor);
            BlockPos currentBlockPos = new BlockPos(tracer);
            IBlockState state = world.getBlockState(currentBlockPos);
            AxisAlignedBB collider = state.getCollisionBoundingBox(world, currentBlockPos);
            if (collider != null && collider.offset(currentBlockPos).contains(tracer)) return false;
        }

        return result;

    }

    public Entity searchForTarget () {
        Entity result = null;

        AxisAlignedBB bb = new AxisAlignedBB(getPosition().add(getDetectionRange(), getDetectionRange(), getDetectionRange()),
        getPosition().add(-getDetectionRange(), -getDetectionRange(), -getDetectionRange()));
        ArrayList<Entity> candidates = new ArrayList<Entity>(world.getEntitiesWithinAABB(EntityPlayer.class, bb));
        //DISTANCE CHECK & RAYTRACE CHECK
        /*
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getPositionVector().distanceTo(getPositionVector()) > getDetectionRange() ||
                !traceEntity(candidates.get(0))) {

                candidates.remove(i);
                i--;
            }
        }
        */

        if (candidates.size() > 0) result = candidates.get(0);
        
        return result;
    }
    
}
