package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.tiles.TurretMasterTe;
import com.anet.qtr4tdm.init.BlocksInit;
import com.anet.qtr4tdm.uebki.messages.primitive.TurretRotationMessage;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
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

        RayTraceResult result = rayTraceBlocks(getGunPosition(), entity.getPositionEyes(1), false, true, false);
        return result == null || result.typeOfHit == Type.MISS;
    }

    protected boolean tracePositions (Vec3d position, Vec3d tracePos) { //PEREPISAT' 6bl

        Vec3d incrementor = tracePos.subtract(position).normalize(); //NE OPTIMAL'No
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

    public void setEnergy (long energy) {
        this.energy = energy;
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

    //FROM net.minecraft.world
    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)
                && !block.equals(BlocksInit.TURRETCOLLIDER) && !block.equals(BlocksInit.TURRETCOLLIDERCAP))
                {
                    RayTraceResult raytraceresult = iblockstate.collisionRayTrace(world, blockpos, vec31, vec32);

                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid) && !block.equals(BlocksInit.TURRETCOLLIDER) && !block.equals(BlocksInit.TURRETCOLLIDERCAP))
                        {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);

                            if (raytraceresult1 != null)
                            {
                                return raytraceresult1;
                            }
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}
