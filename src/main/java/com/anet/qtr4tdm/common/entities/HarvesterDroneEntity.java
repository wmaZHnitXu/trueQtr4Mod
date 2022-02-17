package com.anet.qtr4tdm.common.entities;

import java.util.ArrayList;
import java.util.List;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.bases.InWorldBasesManager;
import com.anet.qtr4tdm.common.bases.baseInfo;
import com.anet.qtr4tdm.common.supers.DroneSmallEntity;
import com.anet.qtr4tdm.common.tiles.DroneBaseTile;
import com.anet.qtr4tdm.init.BlocksInit;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class HarvesterDroneEntity extends DroneSmallEntity implements IInventory {

    private Vec3d targetPos;
    private ArrayList<ItemStack> items;
    
    private HarvesterStatus status;
    private ArrayList<Vec3d> targets;
    private int harvestTimer;
    private double speed;
    private int range;
    private double acceleration;
    private Vec3d landingPos;
    private Path currentPath;
    
    
    public double vaintRotation;

    enum HarvesterStatus {
        Harvesting,
        FlyingTo,
        Returning,
        Charging
    }
    

    public HarvesterDroneEntity(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        range = 10;
        items = new ArrayList<ItemStack>();
        for (int i = 0; i < GetMaxItemStacks(); i++) items.add(ItemStack.EMPTY);
        targets = new ArrayList<Vec3d>();
        status = HarvesterStatus.Charging;
    }

    protected PathNavigate createNavigator (World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        navigator = new PathNavigateFlying(this, world);
    }

    protected void applyEntityAttributes () {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        BlockPos basePosCandidate = new BlockPos(compound.getInteger("base_x"), compound.getInteger("base_y"), compound.getInteger("base_z"));
        if (!basePosCandidate.equals(BlockPos.ORIGIN)) basePos = basePosCandidate;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (basePos != null && !basePos.equals(BlockPos.ORIGIN)) {
            compound.setInteger("base_x", basePos.getX());
            compound.setInteger("base_y", basePos.getY());
            compound.setInteger("base_z", basePos.getZ());
        }
    }

    private void Init () {
        TryConnectToBaseTile();
        if (baseTile == null) {
            FindNext();
        }
        else {
            basePos = getPosition();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        
        if (world.isRemote) return;

        if (baseTile != null && baseTile.isInvalid()) {
            baseTile.DisconnectDrone(this);
            baseTile = null;
        }

        if (ticksExisted == 1) {
            Init();
        }

        if (status == HarvesterStatus.Harvesting && !isFull()) {
            Harvest();
        }

        //MOVEMENT
        if (status == HarvesterStatus.FlyingTo || status == HarvesterStatus.Returning) {

            if (currentPath != null) {
                if (currentPath.isFinished()) {
                    switch (movingStatus) {
                        case Ascent:

                            BlockPos pos = new BlockPos(landingPos);
                            BlockPos nextPos1 = new BlockPos(landingPos.x, world.getHeight(pos.getX(), pos.getZ()) + 25, landingPos.z);
                            currentPath = new Path(new PathPoint[]{new PathPoint(getPosition().getX(), getPosition().getY(), getPosition().getZ()),
                            new PathPoint(nextPos1.getX(), nextPos1.getY(), nextPos1.getZ()) });
                            //TdmMod.logger.info(targets.size());
                            //TdmMod.logger.info("AscentDone");
                            
                        break;
                        case Towards:

                            currentPath = navigator.getPathToPos(new BlockPos(landingPos));
                            //TdmMod.logger.info(targets.size());
                            //TdmMod.logger.info("TowardstDone");
                            
                        break;
                        case Descent:
                            setPosition(landingPos.x, landingPos.y, landingPos.z);

                            //TdmMod.logger.info(targets.size());
                            if (targets.size() > 0) StartHarvesting();
                            else TryConnectToBaseTile();

                            currentPath = null;
                            this.setNoGravity(false);

                            //TdmMod.logger.info("DescentDone");
                        break;
                    }

                    movingStatus = movingStatus.GetNextState();

                }

                if (currentPath != null) {//см. блок кейса descent, там ему нулл присваивается.
                    Vec3d position = getPositionVector();
                    Vec3d addition = Vec3d.ZERO;
                    if (!currentPath.isFinished()) { 
                        addition = currentPath.getCurrentPos().add(new Vec3d(0.5d, 0.5d, 0.5d)).subtract(position).normalize().scale(speed);
                    }
                    motionX = addition.x;
                    motionY = addition.y;
                    motionZ = addition.z;
                    setLocationAndAngles(position.x, position.y, position.z, 0, 0);
                    //TdmMod.logger.info(position.distanceTo(currentPath.getCurrentPos()));
                    if (!currentPath.isFinished() && position.distanceTo(currentPath.getCurrentPos()) < 0.9d) {
                        currentPath.incrementPathIndex();
                        //if (!currentPath.isFinished())
                        //TdmMod.logger.info(currentPath.getCurrentPos().toString());
                    }
                }
            }
 
        }

        //CHARGING
        /*
        if (status == HarvesterStatus.Charging) {
            if (baseTile != null && !baseTile.isInvalid) {
                baseTile.chargeTheDrone(this);
            }
        }
        */

        //CONSUMPTION

        if (status != HarvesterStatus.Charging) {
            energy = Math.max(energy - 16, 0);
        }

        if (ticksExisted % 20 == 0) {
            //TdmMod.logger.info(status + " " + getPosition());
            if ((status == HarvesterStatus.Returning || status == HarvesterStatus.Charging) && baseTile == null) {
                TryConnectToBaseTile();

            }
        }
    }

    public void StartSortie (ArrayList<Vec3d> targetPositions) {
        //TdmMod.logger.info(isFull());
        //TdmMod.logger.info(status);
        if (isFull() || status != HarvesterStatus.Charging || targetPositions.isEmpty()) return;
        targets = new ArrayList<Vec3d>(targetPositions);
        baseTile = null;
        FindNext();
        TdmMod.logger.info("started sortie on " + targets.size() + " positions");
    }

    private void FindNext () {
        //TdmMod.logger.info("find next");
        speed = GetMaxSpeed();
        targetPos = GetNearestTarget();
        if (targetPos == null) { 
            targetPos = new Vec3d(basePos).addVector(0.5d, 0.5d, 0.5d);
            //TdmMod.logger.info("no pos found, go to base");
            status = HarvesterStatus.Returning;
        }
        landingPos = GetLandingPosFromD(targetPos);
        TdmMod.logger.info(targets.size());
        TakeOff();
    }

    protected void TakeOff () {

        recomendedHeight = world.getHeight(getPosition().getX(), getPosition().getZ()) + 25;
        currentPath = navigator.getPathToPos(getPosition().up(25));
        if (status != HarvesterStatus.Returning) status = HarvesterStatus.FlyingTo;
        movingStatus = DroneMoveStatus.Ascent;

        BlockPos nextPos = new BlockPos(getPosition().getX(), world.getHeight(getPosition().getX(), getPosition().getZ()) + 25, getPosition().getZ());
        currentPath = navigator.getPathToPos(nextPos);

        baseTile = null;

        this.setNoGravity(true);

    }

    private Vec3d GetNearestTarget () {
        if (targets == null) return null;
        Vec3d result = null;
        if (targets.size() == 0) {
            //TdmMod.logger.info("target list is empty");
            return null;
        }
        result = targets.get(0);
        for (Vec3d potentialTarget : targets) {
            if (result.distanceTo(getPositionVector()) > potentialTarget.distanceTo(getPositionVector())) {
                result = potentialTarget;
            }
        }
        return result.addVector(0, 1, 0);
    }

    private Vec3d GetLandingPosFromD (Vec3d vector) {
        Vec3d result = vector;
        double height = world.getHeight(Double.valueOf(vector.x).intValue(), Double.valueOf(vector.z).intValue());
        result = new Vec3d(result.x, height, result.z);
        //TdmMod.logger.info("landing pos: " + result);
        return result;
    }

    private void StartHarvesting () {
        //TdmMod.logger.info("started harvest");
        harvestTimer = GetHarvestingTime();
        status = HarvesterStatus.Harvesting;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.FALL || damageSrc == DamageSource.IN_WALL) return;
        super.damageEntity(damageSrc, damageAmount);
    }

    private void StopHarvesting () {
        //TdmMod.logger.info("stopped harvest");
        targets.remove(0);
        if (isFull()) targets.clear();
        targetPos = null;
        FindNext();
    }

    private void TryConnectToBaseTile () {
        TileEntity te = world.getTileEntity(getPosition().down());
        if (te == null) te = world.getTileEntity(getPosition().down(2));
        if (te instanceof DroneBaseTile) {
            baseTile = (DroneBaseTile)te;
            baseTile.ConnectDrone(this);
            setPosition(te.getPos().getX() + 0.5d, te.getPos().getY() + 1.5d, te.getPos().getZ() + 0.5d);
            status = HarvesterStatus.Charging;

            if (baseinf == null) {
                baseinf = baseTile.GetBase();
                baseinf.ConnectDrone(this);
            }

            if (basePos == null) {
                basePos = getPosition();
            }

            //TdmMod.logger.info("connected to base tile");
        }
    }

    public List<EntityItem> GetItemEntitiesToHarvest () {
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPosition().add(-range, -range, -range), getPosition().add(range, range, range)));
        return items;
    }

    protected void Harvest () {
        harvestTimer--;
        List<EntityItem> items = GetItemEntitiesToHarvest();
        if (items != null) {
            for (EntityItem item : items) {
                Vec3d velocity = getPositionVector().subtract(item.getPositionVector()).scale(0.05);
                item.motionX = velocity.x;
                item.motionY = velocity.y;
                item.motionZ = velocity.z;
                if (item.getPositionVector().distanceTo(getPositionVector()) < 2) {
                    ConsumeItem(item);
                }
            }
        }
        if (items == null || harvestTimer <= 0 || isFull()) {
            StopHarvesting();
            //TdmMod.logger.info(harvestTimer);
            //TdmMod.logger.info(items == null);
            //TdmMod.logger.info(items.size());
        }
    }

    private void ConsumeItem (EntityItem item) {
        boolean result = AddStackToList(item.getItem());
        if (result) item.setDead();
        else if (harvestTimer > 10) harvestTimer = 10;
    }

    private boolean AddStackToList (ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            if (stack.getItem().equals(item.getItem())) {
                int newCount = item.getCount() + stack.getCount();
                item.setCount(Math.min(item.getCount() + stack.getCount(), 64));
                stack.setCount(Math.max(newCount - item.getCount(), 0));
                if (stack.getCount() == 0) return true;
            }
        }

        for (int j = 0; j < items.size(); j++) {
            if (items.get(j).equals(ItemStack.EMPTY)) { items.set(j, stack); return true; }
        }

        return false;
    }

    public boolean isFull () {
        for (ItemStack i : items) {
            if (i.equals(ItemStack.EMPTY)) return false;
        }
        return true;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (!world.isRemote) {
            InventoryHelper.dropInventoryItems(world, getPosition(), this);
            if (baseTile != null) {
                baseTile.DisconnectDrone(this);
                baseTile = null;
            }
        }
    }

    @Override
    protected boolean isMovementBlocked() {
        return super.isMovementBlocked() || baseTile != null;
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
        if (baseTile == null) {
            super.knockBack(entityIn, strength, xRatio, zRatio);
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (baseTile == null) {
            super.applyEntityCollision(entityIn);
        }
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        if (baseTile == null) {
            super.addVelocity(x, y, z);
        }
    }

    public boolean isMoving () {
        return baseTile == null;
    }

    @Override
    public boolean isNotColliding() {
        return baseTile != null;
    }

    protected int GetMaxItemStacks () {
        return 27;
    }

    protected int GetHarvestingTime () {
        return 200;
    }

    protected double GetMaxSpeed () {
        return 0.5d;
    }

    @Override
    public double getMaxEnergy() {
        return 5000;
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.items.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getFieldCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public Item getDroneItem() {
        return BlocksInit.DRONEHARVESTER;
    }
}
