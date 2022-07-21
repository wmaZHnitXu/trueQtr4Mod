package com.anet.qtr4tdm.common.tiles;

import java.util.ArrayList;

import com.anet.qtr4tdm.TdmMod;
import com.anet.qtr4tdm.common.entities.HarvesterDroneEntity;
import com.anet.qtr4tdm.common.items.HarvesterDroneItem;
import com.anet.qtr4tdm.common.supers.DroneSmallEntity;
import com.anet.qtr4tdm.common.supers.TEDefenceEnrg;
import com.anet.qtr4tdm.uebki.gui.DroneContainer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

public class DroneBaseTile extends TEDefenceEnrg implements IInventory {

    private double energy;
    private ArrayList<ItemStack> items;
    private DroneSmallEntity drone;
    public DroneContainer container;
    public int NetworkEnergy;
    public int NetworkMaxEnergy;

    public DroneBaseTile () {
        items = new ArrayList<ItemStack>();
        items.add(ItemStack.EMPTY);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!world.isRemote) {
            addedToEnet = true;
        }
    }

    public DroneSmallEntity GetDrone () {
        if (drone != null && drone.isDead) drone = null;
        return drone;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getDouble("energy");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setDouble("energy", energy);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean canConnectEnergy(EnumFacing side) {
        if (side != EnumFacing.UP) return true;
        return false;
    }

    @Override
    public void setPower(long arg0) {
        if (drone != null);
        drone.injectEnergy(arg0);
    }

    @Override
    public long getPower() {
        if (drone != null && !drone.isDead)
            return drone.getEnergy();
        else return 0;
    }

    @Override
    public long getMaxPower() {
        if (drone != null && !drone.isDead)
            return drone.getMaxEnergy();
        else return 0;
    }

    protected double getChargeSpeed () {
        return 32;
    }

    @Override
    public int getMaxEnergy() {
        return 10000;
    }

    public void chargeTheDrone (DroneSmallEntity dronetocharge) {
        energy -= getChargeSpeed() + dronetocharge.injectEnergy(getChargeSpeed());
    }

    @Override
    public int getConsumptionPerTick() {
        return 0;
    }

    @Override
    public void SetTargetsFromBase(ArrayList<Entity> targets) {}


    @Override
    public int getPoints() {
        return 10;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCustomName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void Refresh() {
        
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
        if (stack != null && stack.getItem() instanceof HarvesterDroneItem && items.get(0).isEmpty()) {
            InsertDrone(stack);
        }
        else if (stack.equals(ItemStack.EMPTY)) {
            items.set(0, stack);
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
        player.openGui(TdmMod.instance, TdmMod.GUI_DRONEHARVESTER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void ConnectDrone (DroneSmallEntity drone) {
        this.drone = drone;
        items.set(0, new ItemStack(drone.getDroneItem()));
    }

    public void DisconnectDrone (DroneSmallEntity drone) {
        if (this.drone == drone) this.drone = null;
        items.set(0, ItemStack.EMPTY);
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
        switch (id) {
            case 0: return drone != null && !drone.isDead ? Double.valueOf(drone.getCurrentEnergy()).intValue() : 0;
            case 1: return drone != null && !drone.isDead ? Double.valueOf(drone.getMaxEnergy()).intValue() : 0;
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                NetworkEnergy = value;
            break;
            case 1:
                NetworkMaxEnergy = value;
            break;
        }
        
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void invalidate() {
        onChunkUnload();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if (!world.isRemote && addedToEnet) {
            addedToEnet = false;
        }
    }

    public void InsertDrone (ItemStack droneStack) { //Чтобы другие дроны, это поменяй. Там только харвестер создается.
        if (!world.isRemote) {
            if (items.get(0).isEmpty()) {
                Entity e = new HarvesterDroneEntity(world);
                e.setPosition(pos.getX() + 0.5d, pos.getY() + 1.5d, pos.getZ() + 0.5d);
                world.spawnEntity(e);
                items.set(0, ItemStack.EMPTY);
            }
        }
        items.set(0, droneStack);
        
    }

    public void OnContainerClosed () {
        if (!world.isRemote) {
            if (items.get(0).isEmpty() && drone != null && !drone.isDead) {
                drone.setDead();
            }
            else if (drone != null && !drone.isDead &&  !drone.getDroneItem().equals(items.get(0).getItem())) {
                drone.setDead();
                InsertDrone(items.get(0));
            }
        }
    }

    public void PullOutDrone (ItemStack droneStack) {

    }
}
